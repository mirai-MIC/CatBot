package org.Simbot.plugins.rss.pixiv;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.FilterValue;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;
import love.forte.simbot.component.mirai.message.MiraiForwardMessageBuilder;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.MessagesBuilder;
import love.forte.simbot.resources.Resource;
import net.mamoe.mirai.message.data.ForwardMessage;
import org.Simbot.config.threadpool.IOThreadPool;
import org.Simbot.plugins.rss.pixiv.entity.Pixiv;
import org.Simbot.utils.AsyncHttpClientUtil;
import org.Simbot.utils.SendMsgUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author ：ycvk
 * @description ：pixiv排行榜监听器
 * @date ：2023/09/18 09:16
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class PixivRankListener {

    private final PixivRankScraper pixivRankScraper;

    @Listener
//    @Filter(value = "/pixiv {{text}} {{date}}", matchType = MatchType.REGEX_CONTAINS)
    @Filter(value = "/p {{text}}", matchType = MatchType.REGEX_CONTAINS)
    public void getMagnetSearchResult(@NotNull final GroupMessageEvent event, @FilterValue("text") final String text) {
        if (StrUtil.isBlank(text)) {
            event.replyBlocking("请输入搜索内容");
            return;
        }
        final List<Pixiv> rankList = pixivRankScraper.getRankList(text, "");
        if (CollUtil.isEmpty(rankList)) {
            event.replyBlocking("未找到相关资源");
            return;
        }
        SendMsgUtil.withdrawMessage(event.replyBlocking("正在查询，请稍后..."), 15);
        //构建消息链
        final var chain = new MiraiForwardMessageBuilder(ForwardMessage.DisplayStrategy.Default);
        //构建消息
        rankList.parallelStream()
                .limit(10)
                .map(this::buildMessage)
                .forEachOrdered(messagesBuilder -> chain.add(event.getBot(), messagesBuilder.build()));
        //发送消息
        event.getSource().sendBlocking(chain.build());
    }

    MessagesBuilder buildMessage(final Pixiv pixiv) {
        final MessagesBuilder builder = new MessagesBuilder();
        final String stringBuilder = "名称: " + pixiv.getTitle() + "\n" +
                "作者: " + pixiv.getAuthor() + "\n" +
                "访问量: " + pixiv.getReadNum() + "\n" +
                "收藏量: " + pixiv.getCollectNum() + "\n" +
                "链接: " + pixiv.getLink() + "\n";
        builder.text(stringBuilder);
        final List<String> imgLink = pixiv.getImgLink();
        log.info("开始下载图片: {}", pixiv.getTitle());
        final MessagesBuilder imgMessage = buildImgMessage(imgLink);
        if (imgMessage != null) {
            builder.append("预览图: \n")
                    .append(imgMessage.build());
        }
        log.info("下载图片完成: {}", pixiv.getTitle());
        return builder;
    }

    MessagesBuilder buildImgMessage(final List<String> imgLink) {
        if (CollUtil.isEmpty(imgLink)) {
            return null;
        }
        final MessagesBuilder builder = new MessagesBuilder();
        imgLink.stream()
                .limit(3)
                .map(s -> IOThreadPool.submit(() -> AsyncHttpClientUtil.downloadImage(s, true, true, 0.6f)))
                .map(s -> {
                    try {
                        return s.get(1, TimeUnit.MINUTES);
                    } catch (final Exception e) {
                        log.error("图片下载失败", e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .forEach(bytes -> {
                    try {
                        builder.image(Resource.of(bytes));
                    } catch (final IOException e) {
                        log.error("图片下载失败", e);
                    }
                });
        return builder;
    }
}
