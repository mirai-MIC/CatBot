package org.Simbot.plugins.searchvideo;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;
import love.forte.simbot.ID;
import love.forte.simbot.component.mirai.message.MiraiForwardMessageBuilder;
import love.forte.simbot.event.ContinuousSessionContext;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.Image;
import love.forte.simbot.message.Message;
import love.forte.simbot.message.Messages;
import love.forte.simbot.message.MessagesBuilder;
import org.Simbot.plugins.searchvideo.data.searchData;
import org.Simbot.utils.OK3HttpClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @BelongsProject: simbot
 * @BelongsPackage: org.Simbot.plugins.searchvideo
 * @Author: MIC
 * @CreateTime: 2023-02-10  11:38
 * @Description:
 * @Version: 1.0
 */
@Component
@Slf4j
public class search {


    @Listener
    @Filter(value = "搜番", matchType = MatchType.TEXT_EQUALS)
    public void getSearch(GroupMessageEvent event, ContinuousSessionContext sessionContext) {
        MiraiForwardMessageBuilder miraiForwardMessageBuilder = new MiraiForwardMessageBuilder();
        final String qqId = String.valueOf(event.getAuthor().getId());
        final int time = 30;
        ID id = event.getAuthor().getId();
        ID groupId = event.getGroup().getId();
        event.getSource().sendBlocking("请发送截图.....");
        var messagesBuilder = new MessagesBuilder();
        try {
            sessionContext.waitingForNextMessage(qqId, GroupMessageEvent.Key, time, TimeUnit.SECONDS, (e, c) -> {
                StringBuilder url = new StringBuilder("https://api.trace.moe/search?anilistInfo&url=");
                if (!(c.getAuthor().getId().equals(id) && c.getGroup().getId().equals(groupId))) {
                    return false;
                }
                Messages messages = c.getMessageContent().getMessages();
                messagesBuilder.at(c.getAuthor().getId());
                for (Message.Element<?> message : messages) {
                    if (message instanceof Image<?> image) {
                        url.append(image.getResource().getName());
                        c.getSource().sendBlocking("触发成功....结果仅供参考请自行明辨");
                        log.info(url.toString());
                        String videoJson = OK3HttpClient.httpGet(url.toString(), null, null);
                        log.info(videoJson);
                        try {
                            searchData searchData = new Gson().fromJson(videoJson, searchData.class);
                            searchData.getResult().forEach(a -> {
                                try {
                                    messagesBuilder.text("\n文件名: " + a.getFilename());
                                    messagesBuilder.text("\n\n图片: " + a.getImage());
                                    messagesBuilder.text("\n\n从: " + a.getFrom());
                                    messagesBuilder.text("\n到: " + a.getTo());
                                    messagesBuilder.text("\n置信度: " + a.getSimilarity() + "\n\n");

                                } catch (Exception ex) {
                                    log.error(ex.getMessage());
                                }
                            });
                        } catch (Exception ex) {
                            log.error(ex.getMessage());
                        }
                        return true;
                    }
                }
                return true;
            });
            miraiForwardMessageBuilder.add(event.getAuthor().getId(), event.getAuthor().getNickname(), messagesBuilder.build());
            event.getSource().sendBlocking(miraiForwardMessageBuilder.build());
        } catch (Exception e) {
            event.getSource().sendBlocking("超时退出");
            log.error("超时退出: " + e.getMessage());
        }
    }
}
