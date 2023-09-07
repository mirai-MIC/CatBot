package org.Simbot.plugins.tu;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.MessagesBuilder;
import love.forte.simbot.resources.Resource;
import org.Simbot.mybatisplus.mapper.AliciaMapper;
import org.Simbot.utils.CaffeineUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Optional;

/**
 * 发送图片
 */


@Slf4j
@Component
@RequiredArgsConstructor
public class GetTu {

    private final AliciaMapper mapper;

    private final CaffeineUtil caffeineUtil;

    // 缓存记录总数
    public Long getRecordCount() {
        final Optional<Long> recordCount = caffeineUtil.getOrLoad("recordCount", Long.class, s -> mapper.selectCount(Wrappers.emptyWrapper()));
        return recordCount.orElse(0L);
    }

    @Filter(value = "/tu")
    @Listener
    public void randomImage(@NotNull final GroupMessageEvent event) {
        final Long aLong = getRecordCount(); // 从缓存中获取记录总数
        final int randomIndex = (int) (Math.random() * aLong);
        final var messagesBuilder = new MessagesBuilder();
        messagesBuilder.at(event.getAuthor().getId());
        messagesBuilder.append("\nId: " + randomIndex + "\n");
        try {
            messagesBuilder.image(Resource.of(new URL(mapper.selectById(randomIndex).getUrl())));
            event.getSource().sendBlocking(messagesBuilder.build());
        } catch (final Exception e) {
            event.replyAsync("发送图片接口接口异常\n" + e.getMessage());
        }
    }
}