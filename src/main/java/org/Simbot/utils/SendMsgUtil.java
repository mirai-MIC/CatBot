package org.Simbot.utils;

import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.ID;
import love.forte.simbot.component.mirai.event.MiraiFriendMessageEvent;
import love.forte.simbot.component.mirai.message.MiraiForwardMessageBuilder;
import love.forte.simbot.definition.Group;
import love.forte.simbot.definition.Member;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.MessageReceipt;
import love.forte.simbot.message.Messages;
import love.forte.simbot.message.MessagesBuilder;
import love.forte.simbot.resources.Resource;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;

@Slf4j
public class SendMsgUtil {
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
     * @param image
     * @return
     * @throws MalformedURLException
     */
    public static MessageReceipt sendSimpleGroupImage(Group group, ID id, String msg, String image) throws MalformedURLException {
        MessagesBuilder messagesBuilder = new MessagesBuilder();
        messagesBuilder.at(id);
        messagesBuilder.text("\n");
        messagesBuilder.text(msg);
        messagesBuilder.text("\n");
        messagesBuilder.image(Resource.of(new URL(image)));
        return group.sendBlocking(messagesBuilder.build());
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
     * 给私聊发消息
     *
     * @param event
     * @param msg
     * @return
     */
    @NotNull
    public static MessageReceipt sendFriendMsg(MiraiFriendMessageEvent event, String msg) {
        return event.getSource().sendBlocking(msg);
    }
    /**
     * 私聊合并消息
     *
     * @param event
     * @param messages
     * @param AuthorId
     * @param UserName
     * @return
     */
    public static MessageReceipt sendFriendForwardMessage(MiraiFriendMessageEvent event, Messages messages, ID AuthorId, String UserName) {
        MiraiForwardMessageBuilder miraiForwardMessageBuilder = new MiraiForwardMessageBuilder();
        miraiForwardMessageBuilder.add(AuthorId, UserName, messages);
        return event.getSource().sendBlocking(miraiForwardMessageBuilder.build());
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
