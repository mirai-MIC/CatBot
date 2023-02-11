package org.Simbot.utils;

import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.ID;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.Message;
import love.forte.simbot.message.Text;

import java.text.MessageFormat;

/**
 * @author mirai
 * @version 1.0
 * @packAge: org.Simbot.utils
 * @date 2022/12/6 13:58
 */
@Slf4j
public class Msg {
    /**
     * 从消息中取出文本消息
     *
     * @param event
     */
    public static void GroupMsg(GroupMessageEvent event) {
        for (Message.Element<?> message : event.getMessageContent().getMessages()) {
            if (message instanceof Text) {
                log.info(MessageFormat.format("[文本消息: {0} ]", ((Text) message).getText()));
            }
        }
    }

    /**
     * 将long类型Id值转换为ID
     *
     * @param id
     * @return
     */
    public static long Id(ID id) {
        return Long.parseLong(String.valueOf(id).trim());
    }

    /**
     * 将String 类型Id转换为ID
     *
     * @param id
     * @return
     */
    public static ID Id(String id) {
        return ID.$(id.trim());
    }

    /**
     * 未知用途
     *
     * @param time
     * @return
     */
    public static long longId(String time) {
        return Long.parseLong(time.trim());
    }
}
