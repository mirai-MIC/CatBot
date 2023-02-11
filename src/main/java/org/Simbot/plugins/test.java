package org.Simbot.plugins;

import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.FilterValue;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.MessagesBuilder;
import org.Simbot.utils.SendMsgUtil;
import org.springframework.stereotype.Component;

import static org.Simbot.utils.Msg.Id;

/**
 * @BelongsProject: simbot
 * @BelongsPackage: org.Simbot.plugins
 * @Author: MIC
 * @CreateTime: 2023-02-10  19:20
 * @Description:
 * @Version: 1.0
 */


@Component
public class test {
    @Listener
    @Filter(value = "转发/{{code}}/{{text}}", matchType = MatchType.REGEX_MATCHES)
    public void send(GroupMessageEvent event, @FilterValue("code") String code, @FilterValue("text") String text) {
        MessagesBuilder messagesBuilder = new MessagesBuilder();
        messagesBuilder.text(text);
        SendMsgUtil.ForwardMessages(event, Id(code.trim()), messagesBuilder);
    }
}
