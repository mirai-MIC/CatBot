package org.Simbot.plugins.video;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.FilterValue;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.component.mirai.message.MiraiForwardMessageBuilder;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.MessagesBuilder;
import love.forte.simbot.resources.Resource;
import org.Simbot.plugins.video.data.VData;
import org.Simbot.utils.OK3HttpClient;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * @author mirai
 * @version 1.0
 * @className TxVideo
 * @data 2023/01/20 21:48
 * @description
 */
@Component
@Slf4j
public class TxVideo {

    @Listener
    @Filter(value = "/tx {{text}}/{{n}}")
    public void txVideo(GroupMessageEvent event, @FilterValue("text") String text, @FilterValue("n") String num) {
        Gson gson = new Gson();
        MiraiForwardMessageBuilder miraiForwardMessageBuilder = new MiraiForwardMessageBuilder();
        var params = new HashMap<String, Object>();
        MessagesBuilder messagesBuilder = new MessagesBuilder();
        params.put("msg", text.trim());
        params.put("n", 1);
        params.put("num", num.trim());
        try {
            String videoJson = OK3HttpClient.httpGet("https://xiaoapi.cn/API/dy_tx.php", params, null);
            VData vData = gson.fromJson(videoJson, VData.class);
            log.info(videoJson);
            messagesBuilder.at(event.getAuthor().getId());
            try {
                messagesBuilder.image(Resource.of(new URL(vData.getPic())));
            } catch (MalformedURLException e) {
                log.error("视频无封面图片异常.....");
            }
            messagesBuilder.text("\n" + vData.getTitle());
            messagesBuilder.text("\n" + vData.getPlayTitle());
            messagesBuilder.text("\n" + vData.getType());
            messagesBuilder.text("\n" + vData.getMsg());
            messagesBuilder.text("\n" + vData.getUrl());
            miraiForwardMessageBuilder.add(event.getAuthor().getId(), event.getAuthor().getNickOrUsername(), messagesBuilder.build());
            event.getSource().sendBlocking(miraiForwardMessageBuilder.build());
        } catch (Exception e) {
            log.error("视频解析插件出现异常...." + e.getMessage());
            event.replyAsync("视频解析插件出现异常....\n异常信息: " + e.getMessage());
        }
    }
}
