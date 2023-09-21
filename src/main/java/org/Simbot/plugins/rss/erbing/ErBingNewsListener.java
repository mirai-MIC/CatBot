package org.Simbot.plugins.rss.erbing;

import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;
import love.forte.simbot.component.mirai.message.MiraiForwardMessageBuilder;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.MessagesBuilder;
import love.forte.simbot.resources.Resource;
import net.mamoe.mirai.message.data.ForwardMessage;
import org.Simbot.config.threadpool.IOThreadPool;
import org.Simbot.plugins.rss.erbing.entity.ErBingNews;
import org.Simbot.utils.AsyncHttpClientUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author ：ycvk
 * @description ：二柄新闻监听器
 * @date ：2023/09/21 13:42
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ErBingNewsListener {

    private final ErBingNewsScraper bingNewsScraper;

    @Listener
    @Filter(value = "/游戏新闻", matchType = MatchType.TEXT_EQUALS)
    public void getMagnetSearchResult(@NotNull final GroupMessageEvent event) {

        final List<ErBingNews> newsList = bingNewsScraper.getErBingNewsList();
        if (newsList.isEmpty()) {
            event.replyBlocking("未找到相关资源");
            return;
        }

        //构建消息
        final List<List<ErBingNews>> split = CollUtil.split(newsList, 10);
        final List<CompletableFuture> futures = new ArrayList<>();
        for (final List<ErBingNews> bingNews : split) {
            //构建消息链
            final var chain = new MiraiForwardMessageBuilder(ForwardMessage.DisplayStrategy.Default);
            bingNews.parallelStream()
                    .map(this::buildErBingNews)
                    .forEach(news -> chain.add(event.getBot(), news.build()));
            //发送消息
            final var sent = event.getSource().sendAsync(chain.build());
            futures.add(sent);
        }
        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();
    }

    MessagesBuilder buildErBingNews(final ErBingNews news) {
        final MessagesBuilder builder = new MessagesBuilder();
        final StringBuilder stringBuilder = new StringBuilder();
        final List<String> img = news.getImg();
        List<Future<ByteArrayInputStream>> list = null;
        if (CollUtil.isNotEmpty(img)) {
            list = img.parallelStream().map(this::downloadImage)
                    .toList();
        }

        stringBuilder.append(news.getTitle()).append("\n")
                .append("发布时间 : ").append(news.getPublishedDate()).append("\n\t")
                .append(news.getContent()).append("\n");

        builder.text(stringBuilder.toString());

        if (CollUtil.isNotEmpty(list)) {
            for (final Future<ByteArrayInputStream> future : list) {
                try {
                    final ByteArrayInputStream stream = future.get(15, TimeUnit.SECONDS);
                    builder.image(Resource.of(stream));
                } catch (final Exception e) {
                    log.error("下载图片失败", e);
                }
            }
        }


        return builder;
    }

    Future<ByteArrayInputStream> downloadImage(final String url) {
        return IOThreadPool.submit(() -> AsyncHttpClientUtil.downloadImage(url, false));
    }
}
