package org.Simbot.plugins.admin.Adminutils;

import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.ID;
import love.forte.simbot.definition.GroupMember;
import love.forte.simbot.event.GroupMessageEvent;
import org.Simbot.utils.SendMsgUtil;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author mirai
 * @version 1.0
 * @packAge: org.Simbot.Plugins.Admin.Adminutils
 * @date 2022/12/8 14:53
 */

@Slf4j
@Component
public class adminUtils {
    public static void getMute(GroupMessageEvent event, ID Id, long time) {
        GroupMember member = event.getGroup().getMember(Id);
        try {
            assert member != null;
            member.muteAsync(time, TimeUnit.MINUTES);
        } catch (Exception e) {
            SendMsgUtil.sendReplyGroupMsg(event, "异常");
            log.error("禁言插件出现异常: " + e.getMessage());
        }
        /*
              public static void getCdTime(MiraiGroupMessageEvent event){

                  Objects.requireNonNull(event.getSource().getMember(ID.$(""))).kickAsync("T");
              }


         */

    }

    public static void getUnmute(GroupMessageEvent event, ID id) {
        GroupMember member = event.getGroup().getMember(id);
        try {
            assert member != null;
            member.unmuteAsync();
        } catch (Exception e) {
            SendMsgUtil.sendReplyGroupMsg(event, "异常");
            log.error("解除禁言出现异常: " + e.getMessage());
        }
    }
}
