package org.Simbot.plugins.news;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.event.GroupMessageEvent;
import org.Simbot.utils.AsyncHttpClientUtil;
import org.Simbot.utils.SendMsgUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @BelongsProject: simbot
 * @BelongsPackage: org.Simbot.plugins.news
 * @Author: MIC
 * @CreateTime: 2023-02-24  20:09
 * @Description:
 * @Version: 1.0
 */

@Component
@Slf4j
public class morningNews {

    @Value("${api.news}")
    String newsImage;

    @Value("${news.token}")
    String token;

    @Listener
    @Filter(value = "/每日早报")
    @Filter(value = "/早报")
    public void getNews(final GroupMessageEvent event) {
        final var responsePair = AsyncHttpClientUtil.doGet(newsImage, builder -> builder.addQueryParam("token", token));
        final String body = responsePair.getValue().getResponseBody();
        final JSONObject obj = JSONUtil.parseObj(body);
        final String str = obj.getJSONObject("data").getStr("image");
        log.info("result:{}", str);
        SendMsgUtil.sendSimpleGroupImage(event.getGroup(), event.getAuthor().getId(), "每日早报~", str);
    }
}
