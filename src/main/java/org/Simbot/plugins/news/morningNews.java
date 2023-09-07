package org.Simbot.plugins.news;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.event.GroupMessageEvent;
import org.Simbot.config.threadpool.IOThreadPool;
import org.Simbot.utils.AsyncHttpClientUtil;
import org.Simbot.utils.SendMsgUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

/**
 * @author : ycvk
 * @version : 1.0
 * @date : 2023-09-07  20:09
 * @description : 每日早报
 */

@Component
@Slf4j
public class morningNews {

    @Value("${api.news}")
    String newsImage;

    @Value("${news.token}")
    String token;

    @Value("${api.todayInHistory}")
    String history;

    @Listener
    @Filter(value = "/每日早报")
    @Filter(value = "/早报")
    @Filter(value = "/zb")
    public void getNews(final GroupMessageEvent event) {

        IOThreadPool.submit(() -> {
            final String morningNews = getMorningNews();
            SendMsgUtil.sendSimpleGroupImage(event.getGroup(), event.getAuthor().getId(), "每日早报~", morningNews);
            return null;
        });
        final ByteArrayInputStream stream = getHistory();
        SendMsgUtil.sendSimpleGroupImage(event.getGroup(), event.getAuthor().getId(), "历史上的今天~", stream);
    }

    private String getMorningNews() {
        final var responsePair = AsyncHttpClientUtil.doGet(newsImage, builder -> builder.addQueryParam("token", token));
        final String body = responsePair.getValue().getResponseBody();
        final JSONObject obj = JSONUtil.parseObj(body);
        final String str = obj.getJSONObject("data").getStr("image");
        log.info("result:{}", str);
        return str;
    }

    private ByteArrayInputStream getHistory() {
        return AsyncHttpClientUtil.downloadImage(history, false);
    }
}
