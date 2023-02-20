package org.Simbot.plugins.sign;

import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.ContentTrim;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.MessagesBuilder;
import org.Simbot.mybatisplusutils.domain.signData;
import org.Simbot.mybatisplusutils.mapper.SignMapper;
import org.Simbot.utils.FormatTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author mirai
 * @version 1.0
 * @packAge: org.Simbot.Plugins.Sign
 * @date 2022/12/7 19:00
 */
@Component
@Slf4j
public class GroupSign {


    @Autowired
    private SignMapper signMapper;


    /**
     * 签到注册
     *
     * @param event
     */
    @ContentTrim
    @Listener
    @Filter(value = "注册")
    public void Enroll(GroupMessageEvent event) {
        try {
            signData signData = new signData();
            signData.setId(Long.parseLong(event.getAuthor().getId().toString().trim()));
            signData.setDaytime(new FormatTime().getTime());
            signData.setSum(50L);
            signData.setSumday(1L);
            signMapper.insert(signData);
            event.replyAsync("注册成功");
        } catch (Exception e) {
            log.error("异常信息:" + e.getMessage());
            event.replyAsync("重复注册...");
        }
    }

    /**
     * 群签到
     *
     * @param event
     */
    @ContentTrim
    @Listener
    @Filter(value = "签到")
    public void getSign(GroupMessageEvent event) {
        try {
            var signData = signMapper.selectById(Long.parseLong(event.getAuthor().getId().toString().trim()));
            if (signData == null) return;
            if (!signData.getDaytime().equals(new FormatTime().getTime())) {
                signData.setDaytime(new FormatTime().getTime());
                signData.setSum(signData.getSum() + 100L);
                signData.setSumday(signData.getSumday() + 1);
                signMapper.updateById(signData);
                event.replyAsync("签到成功");
            } else event.replyAsync("今日已签到...");
        } catch (Exception e) {
            log.error(e.getMessage());
            event.replyAsync("抛出异常: " + e.getMessage());
        }
    }

    @Listener
    @Filter("/积分")
    public void getSignById(GroupMessageEvent event) {
        try {
            var messagesBuilder = new MessagesBuilder();
            var signData = signMapper.selectById(Long.parseLong(event.getAuthor().getId().toString().trim()));
            messagesBuilder.at(event.getAuthor().getId());
            messagesBuilder.text("\n时间: " + signData.getDaytime());
            messagesBuilder.text("\n坤坤币: " + signData.getSum());
            messagesBuilder.text("\n累计坤日: " + signData.getSumday());

            event.getSource().sendBlocking(messagesBuilder.build());
        } catch (Exception e) {
            event.replyAsync("请先注册.....");
        }
    }
}
