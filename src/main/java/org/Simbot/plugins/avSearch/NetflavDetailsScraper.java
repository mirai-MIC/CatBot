package org.Simbot.plugins.avSearch;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.Simbot.utils.OK3HttpClient;
import org.springframework.stereotype.Component;

import java.util.*;
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

    final OkHttpClient httpClient = OK3HttpClient.okHttpClient;

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
        //构建请求
        final Request request = new Request.Builder()
                .url(searchUrl + avNum)
                .build();
        //发送请求
        try (final Response response = httpClient.newCall(request).execute()) {
            final JSONObject obj = JSONUtil.parseObj(response.body().string());
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
            final Request videoRequest = new Request.Builder()
                    .url(videoUrl + videoId)
                    .build();
            //发送请求
            try (final Response videoResponse = httpClient.newCall(videoRequest).execute()) {
                //获取返回结果
                return JSONUtil.parseObj(videoResponse.body().string());
            }
        } catch (final Exception e) {
            log.error("请求失败", e);
            return null;
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
                .filter(s -> s.contains("streamtape") || s.contains("vidoza") || s.contains("streamsb"))
                .toList();

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
        map.put("HD", originalSet);
        map.put("HD[SUB]", filteredSet);
        log.info("map:{}", map);
        return map;
    }


}
