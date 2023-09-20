package org.Simbot.plugins.rss.pixiv;

import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.Simbot.plugins.rss.pixiv.entity.Pixiv;
import org.Simbot.plugins.rss.pixiv.entity.RankType;
import org.Simbot.utils.AsyncHttpClientUtil;
import org.Simbot.utils.CaffeineUtil;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ：ycvk
 * @description ：pixiv排行榜爬虫
 * @date ：2023/09/18 00:27
 */
@Slf4j
@Component
public class PixivRankScraper {
    private static final String RANK_URL = "https://rsshub.app/pixiv/ranking/";

    private final Pattern descPattern = Pattern.compile("画师：(.*?) - 阅览数：(\\d+) - 收藏数：(\\d+)");
    private final Pattern imgPattern = Pattern.compile("src=\"(https[^\"]+)\"");

    @Resource
    private CaffeineUtil caffeineUtil;

    @SneakyThrows
    public List<Pixiv> getRankList(final String type, final String date) {

        final var pixivs = caffeineUtil.get(type + ":" + date, new TypeReference<List<Pixiv>>() {
        });
        if (pixivs.isPresent()) {
            log.info("Get data from cache");
            return pixivs.get();
        }

        final SyndFeed feed = getRankRssData(type, date);
        final List<SyndEntry> entries = feed.getEntries();

        final List<Pixiv> list = entries.parallelStream()
                .map(this::parseEntryToPixiv)
                .sorted()
                .toList();

        if (CollUtil.isNotEmpty(list)) {
            caffeineUtil.put(type + ":" + date, list);
            return list;
        }
        return List.of();
    }

    @SneakyThrows
    private SyndFeed getRankRssData(final String type, String date) {
        final RankType rankType = RankType.getRankTypeByDesc(type);
        final String typeData = rankType.getData();
        if (StrUtil.isBlank(date)) {
            date = rankType.getDefaultDate();
        }
        final String url = RANK_URL + typeData + "/" + date;
        log.info("rss url:{}", url);
        final InputStream stream = AsyncHttpClientUtil.doGet(url).getValue().getResponseBodyAsStream();
        return new SyndFeedInput().build(new XmlReader(stream));
    }

    private Pixiv parseEntryToPixiv(final SyndEntry entry) {
        final Pixiv pixiv = new Pixiv();
        pixiv.setTitle(entry.getTitle()).setLink(entry.getLink());

        final String desc = entry.getDescription().getValue();
        final Matcher matcher = descPattern.matcher(desc);

        if (matcher.find()) {
            pixiv.setAuthor(matcher.group(1)).setReadNum(matcher.group(2)).setCollectNum(matcher.group(3));
        } else {
            log.info("No match found");
        }

        final Matcher imgMatcher = imgPattern.matcher(desc);

        final List<String> list = new ArrayList<>();
        while (imgMatcher.find()) {
            list.add(imgMatcher.group(1));
        }
        pixiv.setImgLink(list);
        return pixiv;
    }
}
