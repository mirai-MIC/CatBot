package org.Simbot.plugins.tu;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.MessagesBuilder;
import love.forte.simbot.resources.Resource;
import org.Simbot.mybatisplus.mapper.AliciaMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.net.URL;

/**
 * 发送图片
 */


@Slf4j
@Component
public class GetTu {
    @javax.annotation.Resource
    private AliciaMapper mapper;

    @Filter(value = "/all")
    @Listener
    public void randomImage(@NotNull GroupMessageEvent event) {
        int randomIndex = (int) (Math.random() * mapper.selectCount(Wrappers.emptyWrapper()));
        var messagesBuilder = new MessagesBuilder();
        messagesBuilder.at(event.getAuthor().getId());
        messagesBuilder.append("\nId: " + randomIndex + "\n");
        try {
            messagesBuilder.image(Resource.of(new URL(mapper.selectById(randomIndex).getUrl())));
            event.getSource().sendBlocking(messagesBuilder.build());
        } catch (Exception e) {
            event.replyAsync("发送图片接口接口异常\n" + e.getMessage());
        }
    }
}