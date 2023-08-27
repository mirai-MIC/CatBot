package org.Simbot.plugins.avSearch;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.Simbot.plugins.avSearch.entity.CustomDetailEntity;
import org.Simbot.utils.AsyncHttpClientUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.Map;

/**
 * @author ：ycvk
 * @description ：Arzon详情页爬虫
 * @date ：2023/08/27 09:41
 */
@Slf4j
public class ArzonScraper {
    private static final String ARZON_SEARCH_URL = "http://tuic.ycvk.app:12345/v1/movies/search?provider=ARZON&q=";
    private static final String ARZON_URL = "https://www.arzon.jp/";

    public static List<CustomDetailEntity> searchByAvNum(final String avNum) {
        final var pair = AsyncHttpClientUtil.doGet(ARZON_SEARCH_URL + avNum);
        final String body = pair.getValue().getResponseBody();
        final JSONObject entries = JSONUtil.parseObj(body);
        final JSONArray array = entries.getJSONArray("data");
        if (array.isEmpty()) {
            log.info("array is empty");
            return null;
        }
        return array.toList(CustomDetailEntity.class);
    }

    public static Map<String, String> getHeaders(final String entityHomepage) {
        if (StrUtil.isBlank(entityHomepage)) {
            return null;
        }
        //通过访问homepage获取cookie
        final var pair = AsyncHttpClientUtil.doGet(entityHomepage);
        final var headers = pair.getValue().getHeader("Set-Cookie");
        if (StrUtil.isBlank(headers)) {
            log.info("cookie is empty");
            return Map.of("Referer", ARZON_URL);
        }
        final String cookie = headers.substring(0, headers.indexOf(";"));
        return Map.of("Cookie", cookie, "Referer", ARZON_URL);
    }

    @SneakyThrows
    public static Document getDocument(final CustomDetailEntity entity) {
        final String homepage = entity.getHomepage();
        return Jsoup.connect(homepage).get();
    }

    public static String getDesc(final Document document) {
        final Element divElement = document.selectFirst("div.item_text");
        return divElement.ownText();
    }

    public static List<String> getPreviewImg(final Document document) {
        //获取包含所有需要信息的<div>元素
        final Element divElement = document.selectFirst("div.detail_img");
        //获取所有包含图片信息的<a>元素列表
        final Elements aElements = divElement.select("a[data-lightbox=items]");
        //遍历<a>元素列表以提取图片和超链接
        final List<String> imgSrcs = aElements.stream()
                .parallel()
                .map(aElement -> aElement.selectFirst("img").attr("src"))
                .map(imgSrc -> imgSrc.replaceFirst("m_", ""))//去掉m_前缀 使其变成高清图片
                .map(imgSrc -> "https:" + imgSrc)
                .toList();

        log.info("imgSrcs: {}", imgSrcs);
        return imgSrcs;
    }

}
