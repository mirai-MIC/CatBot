package org.Simbot.plugins.rss.erbing;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.Simbot.plugins.rss.erbing.entity.ErBingNews;
import org.Simbot.utils.CaffeineUtil;
import org.Simbot.utils.XmlUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author ：ycvk
 * @description ：二柄新闻爬虫
 * @date ：2023/09/21 13:28
 */
@Component
@Slf4j
public class ErBingNewsScraper {

    private static final String ERBING_NEWS_URL = "https://rsshub.app/diershoubing/news";

    @Resource
    private CaffeineUtil caffeineUtil;

    public List<ErBingNews> getErBingNewsList() {
        final var news = caffeineUtil.get(DateUtil.today(), new TypeReference<List<ErBingNews>>() {
        });
        if (news.isPresent()) {
            log.info("Get data from cache");
            return news.get();
        }
        final SyndFeed feed = XmlUtil.getXmlFeedFromUrl(ERBING_NEWS_URL);
        final List<SyndEntry> entries = feed.getEntries();
        final List<ErBingNews> bingNews = feedEntryToErBingNews(entries);
        caffeineUtil.put(DateUtil.today(), bingNews, 1, TimeUnit.HOURS);
        log.info("Get data from network");
        return bingNews;
    }

    private List<ErBingNews> feedEntryToErBingNews(final List<SyndEntry> entries) {
        final List<ErBingNews> list = new ArrayList<>();
        if (CollUtil.isEmpty(entries)) {
            return list;
        }
        for (final SyndEntry entry : entries) {
            final String value = entry.getDescription().getValue();
//            final List<String> imgUrls = XmlUtil.extractImgUrls(value);
            final List<String> imgUrls = null;
            final ErBingNews news = new ErBingNews(entry.getTitle(), entry.getLink(), null, imgUrls, DateUtil.format(entry.getPublishedDate(), "yyyy-MM-dd HH:mm:ss"));
            news.setContent(value.replaceFirst("【.*?】", "")
                    .replaceAll("<img.*?>", "")
                    .replaceAll("<iframe.*?>.*?</iframe>", "")
                    .replaceAll("<br>", "\n\t")
                    .trim());
            list.add(news);
        }
        return list;
    }
}
