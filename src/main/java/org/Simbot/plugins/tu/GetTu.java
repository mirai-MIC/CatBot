package org.Simbot.plugins.tu;

import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.MessagesBuilder;
import love.forte.simbot.resources.Resource;
import org.Simbot.utils.HttpUtils;
import org.Simbot.utils.randomUrl;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author mirai
 * @version 1.0
 * @packAge: org.Simbot.Plugins.Tu
 * @date 2022/12/10 20:40
 * @description 发送图片
 */
@Controller
@Slf4j
public class GetTu {
    @Filter(value = "/pc")
    @Listener
    public void pcImage(GroupMessageEvent event) throws IOException {
        MessagesBuilder messagesBuilder = new MessagesBuilder();
        messagesBuilder.at(event.getAuthor().getId());
        messagesBuilder.append("\n你点的PC壁纸到了\n");
        messagesBuilder.image(Resource.of(new URL(HttpUtils.sendGet("http://ap1.iw233.cn/api.php?sort=pc"))));
        event.getSource().sendBlocking(messagesBuilder.build());
    }

    @Filter(value = "/竖屏")
    @Listener
    public void androidImage(GroupMessageEvent event) throws IOException {
        MessagesBuilder messagesBuilder = new MessagesBuilder();
        messagesBuilder.at(event.getAuthor().getId());
        messagesBuilder.append("\n你点的竖屏壁纸到了\n");
        messagesBuilder.image(Resource.of(new URL(HttpUtils.sendGet("http://ap1.iw233.cn/api.php?sort=mp"))));
        event.getSource().sendBlocking(messagesBuilder.build());
    }

    @Filter(value = "/推荐")
    @Listener
    public void suggestImage(GroupMessageEvent event) throws IOException {
        MessagesBuilder messagesBuilder = new MessagesBuilder();
        messagesBuilder.at(event.getAuthor().getId());
        messagesBuilder.append("\n你点的推荐壁纸到了\n");
        messagesBuilder.image(Resource.of(new URL(HttpUtils.sendGet("http://ap1.iw233.cn/api.php?sort=top"))));
        event.getSource().sendBlocking(messagesBuilder.build());
    }

    @Filter(value = "/白毛")
    @Listener
    public void whiteImage(GroupMessageEvent event) throws IOException {
        MessagesBuilder messagesBuilder = new MessagesBuilder();
        messagesBuilder.at(event.getAuthor().getId());
        messagesBuilder.append("\n你点的银发壁纸到了\n");
        messagesBuilder.image(Resource.of(new URL(HttpUtils.sendGet("http://ap1.iw233.cn/api.php?sort=yin"))));
        event.getSource().sendBlocking(messagesBuilder.build());
    }

    @Filter(value = "/兽耳")
    @Listener
    public void catImage(GroupMessageEvent event) throws IOException {
        MessagesBuilder messagesBuilder = new MessagesBuilder();
        messagesBuilder.at(event.getAuthor().getId());
        messagesBuilder.append("\n你点的兽耳壁纸到了\n");
        messagesBuilder.image(Resource.of(new URL(HttpUtils.sendGet("http://ap1.iw233.cn/api.php?sort=cat"))));
        event.getSource().sendBlocking(messagesBuilder.build());
    }

    @Filter(value = "/随机")
    @Listener
    public void randomImage(GroupMessageEvent event) throws IOException {
        MessagesBuilder messagesBuilder = new MessagesBuilder();
        messagesBuilder.at(event.getAuthor().getId());
        messagesBuilder.append("\n你点的随机壁纸到了\n");
        messagesBuilder.image(Resource.of(new URL(HttpUtils.sendGet("http://ap1.iw233.cn/api.php?sort=iw233"))));
        event.getSource().sendBlocking(messagesBuilder.build());
    }

    @Listener
    @Filter(value = "/看妹子")
    public void randomMeiZi(GroupMessageEvent event) throws MalformedURLException {
        MessagesBuilder messagesBuilder = new MessagesBuilder();
        messagesBuilder.at(event.getAuthor().getId());
        messagesBuilder.image(Resource.of(new URL(HttpUtils.get(new randomUrl().getArrUrlGet()))));
        event.getSource().sendBlocking(messagesBuilder.build());
    }

    @Listener
    @Filter(value = "/妹子")
    public void ceshi(GroupMessageEvent event) {
        MessagesBuilder messagesBuilder = new MessagesBuilder();
        messagesBuilder.at(event.getAuthor().getId());
        try {
            messagesBuilder.image(Resource.of(new URL(HttpUtils.sendGet(new randomUrl().getArrUrl()))));
        } catch (Exception e) {
            messagesBuilder.text("\n图片接口出现错误\n");
        }
        event.getSource().sendBlocking(messagesBuilder.build());
    }
}