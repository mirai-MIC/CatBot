package org.Simbot.plugins.magnet.entity;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.FilterValue;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;
import love.forte.simbot.component.mirai.message.MiraiForwardMessageBuilder;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.MessagesBuilder;
import net.mamoe.mirai.message.data.ForwardMessage;
import org.Simbot.plugins.magnet.MagnetScraper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

/**
 * @author ：ycvk
 * @description ：磁力搜索监听器
 * @date ：2023/09/07 21:52
 */
@Component
@RequiredArgsConstructor
public class MagnetSearchListener {

    private final MagnetScraper magnetScraper;

    @Listener
    @Filter(value = "/搜索 {{text}}", matchType = MatchType.REGEX_CONTAINS)
    public void getMagnetSearchResult(@NotNull final GroupMessageEvent event, @FilterValue("text") final String text) {

        if (StrUtil.isBlank(text)) {
            event.replyBlocking("请输入搜索内容");
            return;
        }
        final var entityList = magnetScraper.magnetSearch(text);
        if (CollUtil.isEmpty(entityList)) {
            event.replyBlocking("未找到相关资源");
            return;
        }
        //构建消息链
        final var chain = new MiraiForwardMessageBuilder(ForwardMessage.DisplayStrategy.Default);
        //构建消息
        entityList.parallelStream()
                .limit(10)
                .map(this::buildMagnetSearchResult)
                .forEach(messagesBuilder -> chain.add(event.getBot(), messagesBuilder.build()));

        event.getSource().sendBlocking(chain.build());
    }

    MessagesBuilder buildMagnetSearchResult(final MagnetSearchEntity entity) {
        final MessagesBuilder builder = new MessagesBuilder();
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("名称: ").append(entity.getVideoTitle()).append("\n")
                .append("发布时间: ").append(entity.getTimestamp()).append("\n")
                .append("做种人数: ").append(entity.getSeeders()).append("\n")
                .append("下载人数: ").append(entity.getLeechers()).append("\n")
                .append("磁力链接: ").append(entity.getMagnetLink()).append("\n")
        ;
        builder.append(stringBuilder.toString());
        return builder;
    }
}
