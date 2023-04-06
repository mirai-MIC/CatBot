package org.Simbot.listens;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.component.mirai.event.MiraiMemberJoinEvent;
import love.forte.simbot.definition.Group;
import love.forte.simbot.definition.GroupMember;
import love.forte.simbot.event.JoinRequestEvent;
import org.Simbot.mybatisplus.mapper.AliciaMapper;
import org.Simbot.utils.Msg;
import org.Simbot.utils.Properties.properties;
import org.Simbot.utils.SendMsgUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

/**
 * @author mirai
 * @className GroupMemberAddListener
 * @data 2023/01/11 23:03
 * @description
 */


@Component
@Slf4j
public class GroupMemberAddListener {

    @Deprecated
    @lombok.Getter
    String masterId = new properties().getProperties("cache/application.properties", "user.Master");


    @Autowired
    private AliciaMapper mapper;

    public GroupMemberAddListener() throws IOException {
    }


    /**
     * 进群欢迎
     *
     * @param event
     */
    @Listener
    public void groupAddListener(MiraiMemberJoinEvent event) {
        int randomIndex = (int) (Math.random() * mapper.selectCount(Wrappers.emptyWrapper()));
        Group group = event.getGroup();
        GroupMember after = event.getAfter();
        log.info(String.valueOf(after.getJoinTime().getMillisecond()));
        String msg = "入群提示：群名[" + group.getName() + "]，群员昵称" + after.getNickname();
        log.info(msg);

        if (event.getGroup().getBot().getId().equals(after.getId())) {
            return;
        }

        SendMsgUtil.sendSimpleGroupImage(group, after.getId(), "欢迎入群", mapper.selectById(randomIndex).getUrl());
    }

    /**
     * 仅限Master邀请
     *
     * @param requestEvent
     */
    @Listener
    public void acceptGroup(JoinRequestEvent requestEvent) {
//        if (!Objects.requireNonNull(groupJoinRequestEvent.getInviter()).getId().equals(Msg.Id(getMasterId()))) {
//            return;
//        }

        if (Objects.requireNonNull(requestEvent.getInviter()).getId().equals(Msg.Id(getMasterId()))) {
            return;
        }

        requestEvent.acceptAsync().join();
        log.info(requestEvent.getInviter().getUsername() + "\t\t\t邀请机器人加入群");
//        groupJoinRequestEvent.acceptAsync().join();
//        log.info(groupJoinRequestEvent.getInviter().getUsername());
    }

}
