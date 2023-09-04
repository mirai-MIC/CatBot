package org.Simbot.listens;

import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.ID;
import love.forte.simbot.component.mirai.message.MiraiForwardMessage;
import love.forte.simbot.component.mirai.message.SimbotOriginalMiraiMessage;
import love.forte.simbot.definition.GroupMember;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.*;
import love.forte.simbot.resources.Resource;
import org.Simbot.config.gson.GsonTypeAdapter;
import org.Simbot.listens.data.MessageData;
import org.Simbot.utils.HttpUtils;
import org.Simbot.utils.Msg;
import org.springframework.stereotype.Component;

import java.net.URL;
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
    public void getMsg(final GroupMessageEvent event) {
        final var groupId = "群: " + event.getGroup().getName() + "(" + event.getGroup().getId() + ")";
        final var groupUser = "成员: " + event.getAuthor().getUsername() + "(" + event.getAuthor().getId() + ")";
        final var group = event.getGroup();
        final MessagesBuilder messagesBuilder = new MessagesBuilder();
        log.info(MessageFormat.format("{0}\t\t{1}", groupId, groupUser));
        for (final Message.Element<?> message : event.getMessageContent().getMessages()) {
            if (message instanceof final Image<?> image) {
                log.info(MessageFormat.format("[图片消息: {0} ]", image.getResource().getName()));
            }
            if (message instanceof final MiraiForwardMessage miraiForwardMessage) {
                miraiForwardMessage.getOriginalForwardMessage().getNodeList().forEach(a -> {
                    log.info(MessageFormat.format("[转发消息: \n内容: {0} ]", a.getMessageChain()));
                });
            }
            if (message instanceof final Face face) {
                log.info(MessageFormat.format("[Face表情: {0} ]", face.getId()));
            }
            if (message instanceof final At at) {
                final ID targetId = at.getTarget();
                final GroupMember targetMember = group.getMember(targetId);
                if (targetMember == null) {
                    log.info(MessageFormat.format("[AT消息:未找到目标用户: {0} ]", targetId));
                } else {
                    log.info(MessageFormat.format("[AT消息: @{0}( {1} )", targetMember.getNickOrUsername(), targetMember.getId()));
                }
            }
            if (message instanceof final SimbotOriginalMiraiMessage simbotOriginalMiraiMessage) {
                try {
                    final String simpleApp = simbotOriginalMiraiMessage.getOriginalMiraiMessage().contentToString();
                    final Gson gson = GsonTypeAdapter.getGsonTypeBuilder().create();
                    final MessageData messageData = gson.fromJson(simpleApp, MessageData.class);
                    final MessageData.MetaDTO.Detail1DTO detail1 = messageData.getMeta().getDetail1();
//                    messagesBuilder.text("程序来源: " + messageData.getPrompt() + "\n");
                    messagesBuilder.text("程序来源: " + detail1.getTitle() + "\n");
                    messagesBuilder.text("标题: " + detail1.getDesc() + "\n");
                    messagesBuilder.text("地址链接: " + HttpUtils.cleanURL(detail1.getQqdocurl()));
                    final String preview = detail1.getPreview();
                    if (StrUtil.isNotBlank(preview)) {
                        messagesBuilder.text("\n预览图: \n");
                        messagesBuilder.image(!preview.startsWith("http") || !preview.startsWith("https") ? Resource.of(new URL("http://%s".formatted(preview))) : Resource.of(new URL(preview)));
                    }
                    event.getSource().sendBlocking(messagesBuilder.build());
                    log.info("[小程序]\n{}", simpleApp);
                } catch (final Exception e) {
                    log.error("处理小程序转发失败, 可能不是小程序");
                }
            }
        }
        Msg.GroupMsg(event);
    }
}
