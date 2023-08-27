package org.Simbot.plugins.avSearch;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.Simbot.utils.AsyncHttpClientUtil;
import org.Simbot.utils.CaffeineUtil;
import org.redisson.api.RLock;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ：ycvk
 * @description ：netflav详情页爬虫
 * @date ：2023/07/25 18:22
 */
@Component
@Slf4j
public class NetflavDetailsScraper {

    @Resource
    private CaffeineUtil caffeineUtil;

    @Resource
    private RedissonClient redissonClient;

    final String searchUrl = "https://netflav.com/api98/video/advanceSearchVideo?type=title&page=1&keyword=";
    final String videoUrl = "https://netflav.com/api98/video/v2/retrieveVideo/";

    //匹配magnet是否包含[SUB]
    final String patternString = "\\[HD](\\[SUB])?(.*)";
    final Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);

    /**
     * 获取视频详情
     *
     * @param avNum 番号
     * @return 视频详情
     */
    public JSONObject getVideoResponse(final String avNum) {
        final JSONObject entries;
        final RLock lock = redissonClient.getLock("lock::" + avNum);
        try {
            lock.lock();
            //先从缓存中获取
            final Optional<JSONObject> cache = caffeineUtil.get(avNum, JSONObject.class);
            if (cache.isPresent()) {
                log.info("从缓存中获取 {} 的详情", avNum);
                return cache.get();
            }
            //从redis中获取
            final RMapCache<String, JSONObject> mapCache = redissonClient.getMapCache("netflav");
            final var redisCache = mapCache.get(avNum);
            if (redisCache != null) {
                log.info("从redis中获取 {} 的详情", avNum);
                return redisCache;
            }
            log.info("从网络中获取 {} 的详情", avNum);
            //发送请求
            final var responsePair = AsyncHttpClientUtil.doGet(searchUrl + avNum);
            final JSONObject obj = JSONUtil.parseObj(responsePair.getValue().getResponseBody());
            final JSONObject jsonObject = obj.getJSONObject("result");
            if (jsonObject.getInt("total") <= 0) {
                log.info("没有找到相关信息");
                return null;
            }
            //获取搜索结果
            final JSONArray docs = jsonObject.getJSONArray("docs");
            //获取第一个结果,即最匹配的结果
            final JSONObject videoEntity = docs.getJSONObject(0);
            //获取视频id
            final String videoId = videoEntity.getStr("videoId");
            //发送请求
            final var videoResp = AsyncHttpClientUtil.doGet(videoUrl + videoId);
            //获取返回结果
            entries = JSONUtil.parseObj(videoResp.getValue().getResponseBody());
            //缓存
            mapCache.fastPutAsync(avNum, entries, 1, TimeUnit.HOURS);
            caffeineUtil.put(avNum, entries, 1, TimeUnit.HOURS);
            return entries;
        } catch (final Exception e) {
            log.error("获取视频详情失败", e);
            return null;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 获取视频链接
     *
     * @param avNum 番号
     * @return 视频链接
     */
    @SneakyThrows
    public List<String> getVideoUrl(final String avNum) {
        //获取返回结果
        final JSONObject entries = getVideoResponse(avNum);
        //获取视频链接
        final String str = entries.getStr("result");
        final String[] split = str.split("\\|");
        return Arrays.stream(split)
                .parallel()
                .filter(s -> s.contains("streamtape") || s.contains("vidoza") || s.contains("streamsb") || s.contains("embedgram"))
                .toList();
    }

    /**
     * 获取视频封面
     *
     * @param avNum 番号
     * @return 视频封面
     */
    public String getPreviewHp(final String avNum) {
        //获取返回结果
        final JSONObject videoResponse = getVideoResponse(avNum);
        //获取视频链接
        final String str = videoResponse.getStr("result");
        final String[] split = str.split("\\|");
        for (int i = 0; i < split.length; i++) {
            final String s = split[i];
            if (s.contains("preview_hp")) {
                return split[i + 1];
            }
        }
        return null;
    }

    /**
     * 获取预览图
     *
     * @param avNum 番号
     * @return 预览图
     */
    public List<String> getPreviewImages(final String avNum) {
        //获取返回结果
        final JSONObject videoResponse = getVideoResponse(avNum);
        //获取视频链接
        final String str = videoResponse.getStr("result");
        final String[] split = str.split("\\|");
        int start = 0;
        int end = start;
        for (int i = 0; i < split.length; i++) {
            final String s = split[i];
            if (s.equals("previewImages")) {
                start = i + 1;
            }
            if (s.equals("previewVideo")) {
                end = i;
                break;
            }
        }
        return new ArrayList<>(Arrays.asList(split).subList(start, end));
    }

    /**
     * 获取磁力链接
     *
     * @param avNum 番号
     * @return 磁力链接
     */
    public Map<String, Set<String>> getMagnetLink(final String avNum) {
        //获取返回结果
        final JSONObject entries = getVideoResponse(avNum);
        //获取视频链接
        final String str = entries.getStr("result");
        final String[] split = str.split("\\|");
        //匹配magnet是否包含[SUB]
        final var map = new HashMap<String, Set<String>>();
        final var originalSet = new HashSet<String>();
        final var filteredSet = new HashSet<String>();
        for (int i = 0, splitLength = split.length; i < splitLength - 1; i++) { // changed splitLength to splitLength - 1 to avoid ArrayIndexOutOfBoundsException
            final String s = split[i];
            final Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                final String subFlag = matcher.group(1);
                if (subFlag == null) {
//                    log.info("Doesn't contain [SUB]");
                    originalSet.add(split[i + 1]);
                } else {
//                    log.info("Contains [SUB]");
                    filteredSet.add(split[i + 1]);
                }
            }
        }
        if (CollUtil.isNotEmpty(originalSet)) {
            map.put("HD", originalSet);
        }
        if (CollUtil.isNotEmpty(filteredSet)) {
            map.put("HD[SUB]", filteredSet);
        }
        log.info("map:{}", map);
        return map;
    }

    /**
     * 获取视频时长
     *
     * @param avNum 番号
     * @return 视频时长
     */
    public int getDuration(final String avNum) {
        //获取返回结果
        final JSONObject entries = getVideoResponse(avNum);
        //获取视频链接
        final String str = entries.getStr("result");
        final String[] split = str.split("\\|");
        for (int i = 0; i < split.length; i++) {
            final String s = split[i];
            if (s.contains("duration")) {
                final String s1 = extractNumber(split[i + 1]);
                if (StrUtil.isNotBlank(s1)) {
                    return Integer.parseInt(s1);
                }
                return 0;
            }
        }
        return 0;
    }

    /**
     * 获取视频标题, 翻译版
     *
     * @param avNum 番号
     * @return 视频标题
     */
    public String getDescription(final String avNum) {
        //获取返回结果
        final JSONObject entries = getVideoResponse(avNum);
        //获取视频链接
        final String str = entries.getStr("result");
        final String[] split = str.split("\\|");
        for (int i = 0; i < split.length; i++) {
            final String s = split[i];
            if (s.equals("description")) {
                return split[i + 1];
            }
        }
        return null;
    }


    /**
     * 提取字符串中的数字 例如: "123abc456" -> "123456"
     *
     * @param input 输入字符串
     * @return 提取后的字符串
     */
    public String extractNumber(final String input) {
        final Pattern pattern = Pattern.compile("\\d+");
        final Matcher matcher = pattern.matcher(input);
        final StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            result.append(matcher.group());
        }
        return result.toString();
    }
}
