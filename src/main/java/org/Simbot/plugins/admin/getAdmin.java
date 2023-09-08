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
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class getAdmin {

    @Value("${user.Master}")
    private String master;

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
    public void ban(final ChatRoomMessageEvent chatRoomMessageEvent, @NotNull final GroupMessageEvent event, @FilterValue("accountCode") final String accountCode) {
        final boolean adminBot = event.getGroup().getBot().toMember().isAdmin();
        final boolean adminUser = event.getAuthor().isAdmin();
        if (adminBot && adminUser || event.getAuthor().getId().equals(Msg.Id(master))) {
            final MiraiMember member = (MiraiMember) chatRoomMessageEvent.getSource().getMember(ID.$(accountCode.trim()));
            try {
                assert member != null;
                member.kickAsync("没有理由");
            } catch (final Exception e) {
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
    public void getMute(final GroupEvent event, final GroupMessageEvent groupMessageEvent, @FilterValue("accountCode") final String accountCode, @FilterValue("Time") final String time) {
        final boolean adminBot = event.getGroup().getBot().toMember().isAdmin();
        final boolean adminUser = groupMessageEvent.getAuthor().isAdmin();
        final long timeStr = Long.parseLong(time.trim());
        if (adminBot && adminUser || groupMessageEvent.getAuthor().getId().equals(Msg.Id(master))) {
            try {
                adminUtils.getMute(groupMessageEvent, Msg.Id(accountCode.trim()), timeStr);
            } catch (final Exception e) {
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
    public void test(final GroupMessageEvent event, @FilterValue("Id") final String id) {
        final boolean adminBot = event.getGroup().getBot().toMember().isAdmin();
        final boolean adminUser = event.getAuthor().isAdmin();
        if (adminBot && adminUser || event.getAuthor().getId().equals(Msg.Id(master))) {
            try {
                adminUtils.getUnmute(event, Msg.Id(id));
            } catch (final Exception e) {
                log.error("解除禁言外部发生异常: " + e.getMessage());
            }
        } else {
            event.replyAsync("检查权限");
        }
    }
}
