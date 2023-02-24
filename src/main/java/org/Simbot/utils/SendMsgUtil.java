package org.Simbot.utils;

import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.ID;
import love.forte.simbot.component.mirai.message.MiraiForwardMessageBuilder;
import love.forte.simbot.definition.Group;
import love.forte.simbot.definition.Member;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.MessageReceipt;
import love.forte.simbot.message.Messages;
import love.forte.simbot.message.MessagesBuilder;
import love.forte.simbot.resources.Resource;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Friend;
import org.Simbot.mybatisplus.mapper.AliciaMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;

@Slf4j
@Controller
public class SendMsgUtil {


    @Autowired
    private AliciaMapper mapper;

    /**
     * 发送普通群消息
     *
     * @param event
     * @param msg
     * @return
     */
    public static MessageReceipt sendSimpleGroupMsg(GroupMessageEvent event, String msg) {
        return sendSimpleGroupMsg(event.getGroup(), msg);
    }

    /**
     * 发送普通群消息
     *
     * @param group
     * @param msg
     * @return
     */
    public static MessageReceipt sendSimpleGroupMsg(Group group, String msg) {
        msg = msg.trim();
        log.info("发送普通群消息[{}]:{}", group.getName(), msg);
        return group.sendBlocking(msg);
    }

    /**
     * 发送回复群消息
     *
     * @param event
     * @param msg
     * @return
     */
    public static MessageReceipt sendReplyGroupMsg(GroupMessageEvent event, String msg) {
        msg = msg.trim();
        Group group = event.getGroup();
        Member author = event.getAuthor();
        log.info("发送回复群消息[{}] ==> [{}]:{}", group.getName(), author.getNickOrUsername(), msg);
        return event.replyBlocking(msg);
    }

    /**
     * 发送图片信息
     *
     * @param group
     * @param id
     * @param msg
     * @return
     */
    @NotNull
    public static MessageReceipt sendSimpleGroupImage(Group group, ID id, String msg, String url) {
        try {
            var messagesBuilder = new MessagesBuilder();
            messagesBuilder.at(id);
            messagesBuilder.text("\n");
            messagesBuilder.text(msg);
            messagesBuilder.text("\n");
            try {
                messagesBuilder.image(Resource.of(new URL(url)));
            } catch (IOException e) {
                log.error(e.getMessage());
            }
            return group.sendBlocking(messagesBuilder.build());
        } catch (Exception e) {
            log.error("{发送图片失败}===>  \n" + e.getMessage());
        }
        return null;
    }

    /**
     * 发送合并消息
     *
     * @param group
     * @param AuthorId
     * @param UserName
     * @param build
     * @return
     */
    public static MessageReceipt sendForwardMessageBuilder(Group group, ID AuthorId, String UserName, Messages build) {
        MiraiForwardMessageBuilder miraiForwardMessageBuilder = new MiraiForwardMessageBuilder();
        miraiForwardMessageBuilder.add(AuthorId, UserName, build);
        return group.sendBlocking(miraiForwardMessageBuilder.build());
    }


    /**
     * 发送私聊消息
     *
     * @param id
     * @param build
     */

    public static Object sendFriendMessage(long id, String build) {
        Friend friend = null;
        for (Bot instance : Bot.getInstances()) friend = instance.getFriend(id);
        if (friend == null) return "";
        else return friend.sendMessage(build);
    }


    /**
     * 转发消息
     *
     * @param event
     * @param groupId
     * @param builder
     * @return
     */
    public static MessageReceipt ForwardMessages(GroupMessageEvent event, ID groupId, MessagesBuilder builder) {
        Group group = event.getBot().getGroup(groupId);
        assert group != null;
        return group.sendBlocking(builder.build());
    }
}
