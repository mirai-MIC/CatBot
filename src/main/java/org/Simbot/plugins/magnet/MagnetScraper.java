package org.Simbot.plugins.magnet;

import cn.hutool.core.lang.Pair;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.Simbot.plugins.magnet.entity.MagnetSearchEntity;
import org.Simbot.utils.AsyncHttpClientUtil;
import org.Simbot.utils.CaffeineUtil;
import org.asynchttpclient.Request;
import org.asynchttpclient.Response;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author ：ycvk
 * @description ：磁力搜索爬虫
 * @date ：2023/09/07 21:09
 */
@Component
@Slf4j
public class MagnetScraper {

    private static final String SEARCH_URL = "https://sukebei.nyaa.si?c=0_0&f=0&q=";
//    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Safari/537.36";

    @Resource
    private CaffeineUtil caffeineUtil;

    @SneakyThrows
    public List<MagnetSearchEntity> magnetSearch(final String keyword) {
        final Document document;
        final Optional<Document> result = caffeineUtil.get(keyword, Document.class);
        if (result.isPresent()) {
            document = result.get();
        } else {
            final Pair<Request, Response> responsePair = AsyncHttpClientUtil.doGet(SEARCH_URL + keyword);
            final String body = responsePair.getValue().getResponseBody();
            document = Jsoup.parse(body);
//            document = Jsoup.connect(SEARCH_URL + keyword).userAgent(USER_AGENT).get();
            caffeineUtil.put(keyword, document);
        }
        try {
            final Element table = document.select("table.torrent-list").first();
            final Elements rows = table.select("tbody > tr");
            return rows.parallelStream().map(this::getMagnetSearchEntity).toList();
        } catch (final Exception e) {
            log.error("magnetSearch error", e);
            return List.of();
        }
    }

    @NotNull
    private MagnetSearchEntity getMagnetSearchEntity(final Element row) {
        final String videoLink = row.select("td > a[href*=view]").attr("href");
        final String videoTitle = row.select("td > a[href*=view]").attr("title");
        final String magnetLink = row.select("td > a[href*=magnet]").attr("href").replaceAll("&dn=.*$", "");
        final String seeders = row.select("td.text-center").get(3).text();
        final String leechers = row.select("td.text-center").get(4).text();
        final String timestamp = row.select("td.text-center[data-timestamp]").text();
        return new MagnetSearchEntity(videoLink, videoTitle, magnetLink, seeders, leechers, timestamp);
    }
}
