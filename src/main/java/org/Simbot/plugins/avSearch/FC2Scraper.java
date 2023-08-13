package org.Simbot.plugins.avSearch;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.message.MessagesBuilder;
import love.forte.simbot.resources.Resource;
import org.Simbot.config.threadpool.IOThreadPool;
import org.Simbot.plugins.avSearch.entity.FC2SearchEntity;
import org.Simbot.utils.AsyncHttpClientUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author ：ycvk
 * @description ：搜索FC2视频信息
 * @date ：2023/08/13 16:09
 */
@Slf4j
public class FC2Scraper {

    private static final String FC2_SEARCH_URL = "https://adult.contents.fc2.com/article_search.php?id=";
    private static final String FC2_CUSTOM_SEARCH_URL = "https://jav.mcjoker.xyz/v1/movies/search?provider=FC2&q=";

    /**
     * 根据番号搜索FC2视频
     *
     * @param avNum 番号
     * @return 视频信息
     */
    public static List<FC2SearchEntity> searchByAvNum(final String avNum) {
        final var pair = AsyncHttpClientUtil.doGet(FC2_CUSTOM_SEARCH_URL + avNum);
        final String body = pair.getValue().getResponseBody();
        final JSONObject entries = JSONUtil.parseObj(body);
        return entries.getBeanList("data", FC2SearchEntity.class);
    }

    @SneakyThrows
    private static List<ByteArrayInputStream> getFC2ImgById(final String id) {
        final Document document = Jsoup.connect(FC2_SEARCH_URL + id).get();
        final Elements imageLinks = document.select("ul.items_article_SampleImagesArea > li > a");
        for (final Element link : imageLinks) {
            final String imageUrl = link.attr("href");
        }
        return imageLinks.parallelStream()
                .map(link -> link.attr("href"))
                .filter(StrUtil::isNotBlank)
                .map(AsyncHttpClientUtil::downloadImage)
                .toList();
    }

    @SneakyThrows
    public static MessagesBuilder buildFC2Message(final FC2SearchEntity entity) {
        if (BeanUtil.isEmpty(entity)) {
            return null;
        }
        //用另外的线程去获取图片
        final var listFuture = IOThreadPool.submit(() -> getFC2ImgById(entity.getId()));
        final MessagesBuilder builder = new MessagesBuilder();
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("标题：").append(entity.getTitle()).append("\n")
                .append("番号：").append(entity.getNumber()).append("\n")
                .append("分数：").append(entity.getScore()).append("\n")
                .append("发行日期：").append(DateUtil.parseDate(entity.getReleaseDate())).append("\n")
                .append("预览图：").append("\n");
        builder.text(stringBuilder.toString());
        final List<ByteArrayInputStream> list = listFuture.get(15, TimeUnit.SECONDS);
        list.parallelStream()
                .forEach(inputStream -> {
                    try {
                        builder.image(Resource.of(inputStream));
                    } catch (final Exception e) {
                        log.error("下载预览图失败", e);
                    }
                });
        return builder;
    }
}
