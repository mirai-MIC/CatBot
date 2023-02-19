package org.Simbot.listens;

import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.component.mirai.event.MiraiMemberJoinEvent;
import love.forte.simbot.definition.Group;
import love.forte.simbot.definition.GroupMember;
import org.Simbot.utils.SendMsgUtil;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author mirai
 * @className GroupMemberAddListener
 * @data 2023/01/11 23:03
 * @description
 */


@Component
@Slf4j
public class GroupMemberAddListener {

    /**
     * 进群欢迎
     *
     * @param event
     * @throws IOException
     */
    @Listener
    public void groupAddListener(MiraiMemberJoinEvent event) throws IOException {
        Group group = event.getGroup();
        GroupMember after = event.getAfter();
        String msg = "入群提示：群名[" + group.getName() + "]，群员昵称" + after.getNickname();
        log.info(msg);
        if (event.getGroup().getBot().getId().equals(after.getId())) {
            return;
        }
        SendMsgUtil.sendSimpleGroupImage(group, after.getId(), "欢迎入群");
    }


}
