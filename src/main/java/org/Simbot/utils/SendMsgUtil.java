package org.Simbot.utils;

import lombok.SneakyThrows;
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
import net.mamoe.mirai.utils.ExternalResource;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class SendMsgUtil {


    /**
     * 发送普通群消息
     *
     * @param event 消息事件
     * @param msg   消息内容
     * @return 消息回执
     */
    @NotNull
    public static MessageReceipt sendSimpleGroupMsg(final GroupMessageEvent event, final String msg) {
        return sendSimpleGroupMsg(event.getGroup(), msg);
    }

    /**
     * 发送普通群消息
     *
     * @param group 群
     * @param msg   消息内容
     * @return 消息回执
     */
    public static MessageReceipt sendSimpleGroupMsg(final Group group, String msg) {
        msg = msg.trim();
        log.info("发送普通群消息[{}]:{}", group.getName(), msg);
        return group.sendBlocking(msg);
    }

    /**
     * 发送回复群消息
     *
     * @param event 消息事件
     * @param msg   消息内容
     * @return 消息回执
     */
    public static MessageReceipt sendReplyGroupMsg(final GroupMessageEvent event, String msg) {
        msg = msg.trim();
        final Group group = event.getGroup();
        final Member author = event.getAuthor();
        log.info("发送回复群消息[{}] ==> [{}]:{}", group.getName(), author.getNickOrUsername(), msg);
        return event.replyBlocking(msg);
    }

    @SneakyThrows
    public static MessageReceipt sendReplyGroupImg(final GroupMessageEvent event, String msg) {
        msg = msg.trim();
        final Group group = event.getGroup();
        final Member author = event.getAuthor();
        log.info("发送回复群图片消息[{}] ==> [{}]:{}", group.getName(), author.getNickOrUsername(), msg);
        final var messagesBuilder = new MessagesBuilder();
        final ByteArrayInputStream image = ImageUtil.createImage(msg);
        messagesBuilder.image(Resource.of(image));
        return event.replyBlocking(messagesBuilder.build());
    }

    /**
     * 发送图片信息
     *
     * @param group 群
     * @param id    发送人
     * @param msg   消息内容
     */
    public static void sendSimpleGroupImage(final Group group, final ID id, final String msg, final String url) {
        try {
            final var messagesBuilder = new MessagesBuilder();
            messagesBuilder.at(id);
            messagesBuilder.text("\n");
            messagesBuilder.text(msg);
            messagesBuilder.text("\n");
            try {
                messagesBuilder.image(Resource.of(new URL(url)));
            } catch (final IOException e) {
                log.error("处理图片URL出错:", e);
            }
            group.sendBlocking(messagesBuilder.build());
        } catch (final Exception e) {
            log.error("{发送图片失败}===>  \n" + e.getMessage());
        }
    }

    /**
     * 发送合并消息
     *
     * @param group    群
     * @param AuthorId 发送人
     * @param UserName 发送人昵称
     * @param build    消息内容
     * @return 消息回执
     */
    public static MessageReceipt sendForwardMessageBuilder(final Group group, final ID AuthorId, final String UserName, final Messages build) {
        final MiraiForwardMessageBuilder miraiForwardMessageBuilder = new MiraiForwardMessageBuilder();
        miraiForwardMessageBuilder.add(AuthorId, UserName, build);
        return group.sendBlocking(miraiForwardMessageBuilder.build());
    }

    /**
     * 发送私聊消息
     *
     * @param id    发送人
     * @param build 消息内容
     */
    public static void sendFriendMessage(final long id, final String build) {
        Bot.getInstances().stream()
                .map(bot -> bot.getFriend(id))
                .filter(Objects::nonNull)
                .findFirst().ifPresent(friend -> friend.sendMessage(build));
    }

    /**
     * 发送私聊图片
     *
     * @param id    发送人
     * @param build 消息内容
     */
    @SneakyThrows
    public static void sendFriendImage(final long id, final String build) {
        final var image = ImageUtil.createImage(build);
        try (var resource = ExternalResource.create(image)) {
            Bot.getInstances().stream()
                    .map(bot -> bot.getFriend(id))
                    .filter(Objects::nonNull)
                    .findFirst().ifPresent(friend -> {
                        friend.uploadImage(resource);
                        log.info("发送私聊图片[{}]:{}", friend.getNick(), build);
                    });
        }
    }

    /**
     * 转发消息
     *
     * @param event   消息事件
     * @param groupId 转发群
     * @param builder 消息内容
     */
    public static void ForwardMessages(final GroupMessageEvent event, final ID groupId,
                                       final MessagesBuilder builder) {
        final Group group = event.getBot().getGroup(groupId);
        if (group == null) {
            // 输出日志或者抛出自定义的异常
            log.error("群号[{}]不存在", groupId);
        } else {
            group.sendBlocking(builder.build());
        }
    }

    /**
     * 撤回消息
     *
     * @param messageReceipt 消息回执
     * @param time           撤回时间
     */
    public static void withdrawMessage(final MessageReceipt messageReceipt, final Integer time) {
        if (!messageReceipt.isSuccess()) {
            return;
        }
        CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(time);
                final boolean blocking = messageReceipt.deleteBlocking();
                log.info(blocking ? "撤回成功" : "撤回失败");
            } catch (final InterruptedException e) {
                log.error("撤回消息失败", e);
            }
        });
    }
}
