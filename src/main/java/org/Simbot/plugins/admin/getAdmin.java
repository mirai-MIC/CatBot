package org.Simbot.plugins.admin;

import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.ContentTrim;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.FilterValue;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;
import love.forte.simbot.ID;
import love.forte.simbot.component.mirai.MiraiMember;
import love.forte.simbot.event.ChatRoomMessageEvent;
import love.forte.simbot.event.GroupEvent;
import love.forte.simbot.event.GroupMessageEvent;
import org.Simbot.plugins.admin.Adminutils.adminUtils;
import org.Simbot.utils.Msg;
import org.Simbot.utils.Properties.properties;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;

import java.io.IOException;

/**
 * @author mirai
 * @version 1.0
 * @packAge: org.Simbot.plugins.admin
 * @date 2022/12/7 13:22
 */
@Controller
@Slf4j
public class getAdmin {


    @lombok.Getter
    @Deprecated
    String master = new properties().getProperties("cache/application.properties", "user.Master");


    public getAdmin() throws IOException {
    }


    /**
     * 移出群成员
     *
     * @param chatRoomMessageEvent
     * @param event
     * @param accountCode
     */

    @ContentTrim
    @Listener
    @Filter(value = "k/{{accountCode}}", matchType = MatchType.REGEX_MATCHES)
    public void ban(ChatRoomMessageEvent chatRoomMessageEvent, @NotNull GroupMessageEvent event, @FilterValue("accountCode") String accountCode) {
        boolean adminBot = event.getGroup().getBot().toMember().isAdmin();
        boolean adminUser = event.getAuthor().isAdmin();
        if (adminBot && adminUser || event.getAuthor().getId().equals(Msg.Id(getMaster()))) {
            MiraiMember member = (MiraiMember) chatRoomMessageEvent.getSource().getMember(ID.$(accountCode.trim()));
            try {
                assert member != null;
                member.kickAsync("没有理由");
            } catch (Exception e) {
                log.error("移出群聊成员出现异常: " + e.getMessage());
            }
        } else {
            event.replyAsync("权限不足");
        }
    }

    /**
     * 将群成员禁言
     *
     * @param event
     * @param groupMessageEvent
     * @param accountCode
     * @param time
     */

    @ContentTrim
    @Listener
    @Filter(value = "b/{{accountCode}}/{{Time}}", matchType = MatchType.REGEX_MATCHES)
    public void getMute(GroupEvent event, GroupMessageEvent groupMessageEvent, @FilterValue("accountCode") String accountCode, @FilterValue("Time") String time) {
        boolean adminBot = event.getGroup().getBot().toMember().isAdmin();
        boolean adminUser = groupMessageEvent.getAuthor().isAdmin();
        long timeStr = Long.parseLong(time.trim());
        if (adminBot && adminUser || groupMessageEvent.getAuthor().getId().equals(Msg.Id(getMaster()))) {
            try {
                adminUtils.getMute(groupMessageEvent, Msg.Id(accountCode.trim()), timeStr);
            } catch (Exception e) {
                log.error("禁言出现外部异常");
            }
        } else {
            groupMessageEvent.replyAsync("请检查权限");
        }
    }

    /**
     * 解开群成员
     *
     * @param event
     * @param id
     */
    @Listener
    @Filter(value = "j/{{Id}}")
    public void test(GroupMessageEvent event, @FilterValue("Id") String id) {
        boolean adminBot = event.getGroup().getBot().toMember().isAdmin();
        boolean adminUser = event.getAuthor().isAdmin();
        if (adminBot && adminUser || event.getAuthor().getId().equals(Msg.Id(getMaster()))) {
            try {
                adminUtils.getUnmute(event, Msg.Id(id));
            } catch (Exception e) {
                log.error("解除禁言外部发生异常: " + e.getMessage());
            }
        } else {
            event.replyAsync("检查权限");
        }
    }
}
