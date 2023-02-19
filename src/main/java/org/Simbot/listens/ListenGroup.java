package org.Simbot.listens;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.ID;
import love.forte.simbot.component.mirai.message.MiraiForwardMessage;
import love.forte.simbot.component.mirai.message.SimbotOriginalMiraiMessage;
import love.forte.simbot.definition.GroupMember;
import love.forte.simbot.event.GroupJoinRequestEvent;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.*;
import org.Simbot.listens.data.MessageData;
import org.Simbot.utils.Msg;
import org.Simbot.utils.Properties.properties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.MessageFormat;
import java.util.Objects;

/**
 * @author mirai
 * @version 1.0
 * @packAge: org.Simbot.Listens
 * @date 2022/12/5 21:05
 */
@Slf4j
@Component
public class ListenGroup {
    @Deprecated
    @lombok.Getter
    String masterId = new properties().getProperties("cache/application.properties", "user.Master");

    public ListenGroup() throws IOException {
    }


    @Listener
    public void getMsg(GroupMessageEvent event) throws MalformedURLException {
        var groupId = "群: " + event.getGroup().getName() + "(" + event.getGroup().getId() + ")";
        var groupUser = "成员: " + event.getAuthor().getUsername() + "(" + event.getAuthor().getId() + ")";
        var group = event.getGroup();

        MessagesBuilder messagesBuilder = new MessagesBuilder();
        log.info(MessageFormat.format("{0}\t\t{1}", groupId, groupUser));
        for (Message.Element<?> message : event.getMessageContent().getMessages()) {
            if (message instanceof Image<?> image) {
                log.info(MessageFormat.format("[图片消息: {0} ]", image.getResource().getName()));
            }
            if (message instanceof MiraiForwardMessage miraiForwardMessage) {
                miraiForwardMessage.getOriginalForwardMessage().getNodeList().forEach(a -> {
                    log.info(MessageFormat.format("[转发消息: \n内容: {0} ]", a.getMessageChain()));
                });
            }
            if (message instanceof Face) {
                log.info(MessageFormat.format("[Face表情: {0} ]", ((Face) message).getId()));
            }
            if (message instanceof At) {
                ID targetId = ((At) message).getTarget();
                GroupMember targetMember = group.getMember(targetId);
                if (targetMember == null) {
                    log.info(MessageFormat.format("[AT消息:未找到目标用户: {0} ]", targetId));
                } else {
                    log.info(MessageFormat.format("[AT消息: @{0}( {1} )", targetMember.getNickOrUsername(), targetMember.getId()));
                }
            }
            if (message instanceof SimbotOriginalMiraiMessage) {
                try {
                    String simpleApp = ((SimbotOriginalMiraiMessage) message).getOriginalMiraiMessage().contentToString();
                    MessageData messageData = new Gson().fromJson(simpleApp, MessageData.class);
                    MessageData.MetaDTO.Detail1DTO detail1 = messageData.getMeta().getDetail1();
                    messagesBuilder.text("程序来源: " + messageData.getPrompt() + "\n");
                    messagesBuilder.text("标题: " + detail1.getDesc() + "\n");
                    messagesBuilder.text("来源: " + detail1.getQqdocurl());
                    event.getSource().sendBlocking(messagesBuilder.build());
                    log.info("[小程序]");
                    log.info(simpleApp);
                } catch (Exception e) {
                    log.info("[回复消息]");
                }
            }
            if (message instanceof SimbotOriginalMiraiMessage messageApp) {
                log.info(messageApp.getOriginalMiraiMessage().contentToString());
            }
        }
        Msg.GroupMsg(event);
    }

    /**
     * 仅限Master邀请
     *
     * @param groupJoinRequestEvent
     */
    @Listener
    public void acceptGroup(GroupJoinRequestEvent groupJoinRequestEvent) {
        if (!Objects.requireNonNull(groupJoinRequestEvent.getInviter()).getId().equals(Msg.Id(getMasterId()))) {
            return;
        }
        groupJoinRequestEvent.acceptAsync().join();
        log.info(groupJoinRequestEvent.getInviter().getUsername());
    }
}
