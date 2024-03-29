package org.Simbot.listens;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.ID;
import love.forte.simbot.component.mirai.message.MiraiForwardMessage;
import love.forte.simbot.component.mirai.message.SimbotOriginalMiraiMessage;
import love.forte.simbot.definition.GroupMember;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.At;
import love.forte.simbot.message.Face;
import love.forte.simbot.message.Image;
import love.forte.simbot.message.MessagesBuilder;
import org.Simbot.listens.data.MessageData;
import org.Simbot.utils.Msg;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * @author mirai
 * @version 1.0
 * @packAge: org.Simbot.Listens
 * @date 2022/12/5 21:05
 */
@Slf4j
@Component
public class messageListens {
    @Listener
    public void Messages( GroupMessageEvent event) {
        var groupId = "群: " + event.getGroup().getName() + "(" + event.getGroup().getId() + ")";
        var groupUser = "成员: " + event.getAuthor().getUsername() + "(" + event.getAuthor().getId() + ")";
        var group = event.getGroup();
        MessagesBuilder messagesBuilder = new MessagesBuilder();
        log.info(MessageFormat.format("{0}\t\t{1}", groupId, groupUser));
        for (var message : event.getMessageContent().getMessages()) {
            if (message instanceof Image<?> image) {
                log.info(MessageFormat.format("[图片消息: {0} ]", image.getResource().getName()));
            }
            if (message instanceof MiraiForwardMessage miraiForwardMessage) {
                miraiForwardMessage.getOriginalForwardMessage().getNodeList()
                        .stream()
                        .map(a -> MessageFormat.format("[转发消息: \n内容: {0} ]", a.getMessageChain()))
                        .forEach(log::info);
            }
            if (message instanceof Face face) {
                log.info(MessageFormat.format("[Face表情: {0} ]", face.getId()));
            }
            if (message instanceof At at) {
                ID targetId = at.getTarget();
                GroupMember targetMember = group.getMember(targetId);
                log.info(targetMember == null ? MessageFormat.format("[AT消息:未找到目标用户: {0} ]", targetId) : MessageFormat.format("[AT消息: @{0}( {1} )", targetMember.getNickOrUsername(), targetMember.getId()));
            }
            if (message instanceof SimbotOriginalMiraiMessage simbotOriginalMiraiMessage) {
                try {
                    String simpleApp = simbotOriginalMiraiMessage.getOriginalMiraiMessage().contentToString();
                    var messageData = new Gson().fromJson(simpleApp, MessageData.class);
                    var detail1 = messageData.getMeta().getDetail1();
                    messagesBuilder
                            .text("程序来源: %s\n".formatted(messageData.getPrompt()))
                            .text("标题: %s\n".formatted(detail1.getDesc()))
                            .text("来源: %s".formatted(detail1.getQqdocurl()));
                    event.getSource().sendBlocking(messagesBuilder.build());
                    log.info("[小程序]");
                    log.info(simpleApp);
                } catch (Exception e) {
                    log.info("[回复消息]");
                }
            }
            if (message instanceof SimbotOriginalMiraiMessage messageApp) {
                String xmlApp = messageApp.getOriginalMiraiMessage().contentToString();
                log.info(xmlApp);
            }
        }
        Msg.GroupMsg(event);
    }
}
