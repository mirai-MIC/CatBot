package org.Simbot.listens;

import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.action.ActionType;
import love.forte.simbot.component.mirai.event.MiraiMemberLeaveEvent;
import org.Simbot.utils.SendMsgUtil;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * @author mirai
 * @className GroupMemberReduceListener
 * @data 2023/01/12 00:16
 * @description 退群提示
 */
@Slf4j
@Component
public class GroupMemberReduceListener {
    @Listener
    public void GroupMemberReduce(MiraiMemberLeaveEvent event) {
        var group = event.getGroup();
        var member = event.getBefore();
        var actionType = event.getActionType();
        String msg;

        if (actionType == ActionType.PROACTIVE) {
            msg = MessageFormat.format("退群提示：主动退群 ==> 群名[{0}]，群员昵称{1}({2})", group.getName(), member.getNickname(), member.getId());
            //主动退群
        } else {
            //被动退群（被踢）
            msg = MessageFormat.format("退群提示：被踢出 ==> 群名[{0}]，群员昵称{1}({2})", group.getName(), member.getNickname(), member.getId());
        }
        log.info(msg);
        if (group.getBot().getId().equals(member.getId())) {
            return;
        }
        if (actionType == ActionType.PROACTIVE) {
            //主动退群
            SendMsgUtil.sendSimpleGroupMsg(group, MessageFormat.format("就在刚刚,我们的群友『{0}({1}) 』悄悄地离开了...", member.getNickOrUsername(), member.getId()));
        } else {
            //被动退群（被踢）
            SendMsgUtil.sendSimpleGroupMsg(group, MessageFormat.format("就在刚刚,我们的群友『{0}({1})』被某股神秘的力量推出了群聊.", member.getNickOrUsername(), member.getId()));
        }
    }


}
