package org.Simbot.plugins.sign;

import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.ContentTrim;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.MessagesBuilder;
import org.Simbot.db.dbUtils;
import org.Simbot.utils.FormatTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * @author mirai
 * @version 1.0
 * @packAge: org.Simbot.Plugins.Sign
 * @date 2022/12/7 19:00
 */
@Component
@Slf4j
public class GroupSign {

    @Autowired
    private dbUtils getDb;

    /**
     * 签到注册
     *
     * @param event
     */
    @ContentTrim
    @Listener
    @Filter(value = "注册")
    public void Enroll(GroupMessageEvent event) {
        try {
            getDb.setEnroll(event.getAuthor().getId(), new FormatTime().getTime());
            event.replyAsync("注册成功");
        } catch (Exception e) {
            event.replyAsync("请勿重复注册");
        }
    }

    /**
     * 群签到
     *
     * @param event
     */
    @ContentTrim
    @Listener
    @Filter(value = "签到")
    public void getSign(GroupMessageEvent event) {
        var messagesBuilder = new MessagesBuilder();
        try {

            log.info(event.getAuthor().getId().toString());
            log.info(new FormatTime().getTime());
            getDb.setSign(event.getAuthor().getId(), new FormatTime().getTime());
            getDb.getSelect(event.getAuthor().getId()).forEach(s -> {
                messagesBuilder.at(event.getAuthor().getId());
                messagesBuilder.append("\n当前坤币: " + s.get("sum") + " 个\n");
                messagesBuilder.append("累积签到: " + s.get("sumday") + " 坤日\n");
                messagesBuilder.append("每日签到获得50坤坤币..\n(重复签到无效休想卡bug..\n先注册后签到哦~)");
                event.getSource().sendBlocking(messagesBuilder.build());
            });
        } catch (Exception e) {
            log.error(MessageFormat.format("签到异常: {0}", e.getMessage()));
        }
    }
}
