package org.Simbot.plugins.rss.pixiv;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
import org.Simbot.plugins.rss.pixiv.entity.Pixiv;
import org.Simbot.utils.AsyncHttpClientUtil;
import org.Simbot.utils.SendMsgUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

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
    @Filter(value = "/p {{text}}", matchType = MatchType.REGEX_CONTAINS)
    public void getMagnetSearchResult(@NotNull final GroupMessageEvent event, @FilterValue("text") final String text) {
        if (StrUtil.isBlank(text)) {
            event.replyBlocking("请输入搜索内容");
            return;
        }
        final List<Pixiv> rankList = pixivRankScraper.getRankList(text, DateUtil.yesterday().toDateStr());
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
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("名称: ").append(pixiv.getTitle()).append("\n")
                .append("作者: ").append(pixiv.getAuthor()).append("\n")
                .append("访问量: ").append(pixiv.getReadNum()).append("\n")
                .append("收藏量: ").append(pixiv.getCollectNum()).append("\n")
                .append("链接: ").append(pixiv.getLink()).append("\n");
        builder.text(stringBuilder.toString());
        final List<String> imgLink = pixiv.getImgLink();
        final MessagesBuilder imgMessage = buildImgMessage(imgLink);
        if (imgMessage != null) {
            builder.append("预览图: \n")
                    .append(imgMessage.build());
        }
        return builder;
    }

    @SneakyThrows
    MessagesBuilder buildImgMessage(final List<String> imgLink) {
        if (CollUtil.isEmpty(imgLink)) {
            return null;
        }
        final MessagesBuilder builder = new MessagesBuilder();
        imgLink.parallelStream()
                .limit(5)
                .map(s -> AsyncHttpClientUtil.downloadImage(s, false, true, 0.6f))
                .forEachOrdered(s -> {
                    try {
                        builder.image(Resource.of(s));
                    } catch (final IOException e) {
                        log.error("下载图片失败", e);
                    }
                });
        return builder;
    }


}
