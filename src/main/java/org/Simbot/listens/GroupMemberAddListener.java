package org.Simbot.listens;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.component.mirai.event.MiraiMemberJoinEvent;
import love.forte.simbot.definition.Group;
import love.forte.simbot.definition.GroupMember;
import org.Simbot.mybatisplus.mapper.AliciaMapper;
import org.Simbot.utils.SendMsgUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author mirai
 * @className GroupMemberAddListener
 * @data 2023/01/11 23:03
 * @description
 */


@Component
@Slf4j
public class GroupMemberAddListener {

    @Autowired
    private AliciaMapper mapper;


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
        String msg = "入群提示：群名[" + group.getName() + "]，群员昵称" + after.getNickname();
        log.info(msg);
        if (event.getGroup().getBot().getId().equals(after.getId())) {
            return;
        }

        SendMsgUtil.sendSimpleGroupImage(group, after.getId(), "欢迎入群", mapper.selectById(randomIndex).getUrl());
    }


}
