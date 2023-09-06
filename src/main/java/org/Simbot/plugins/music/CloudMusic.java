package org.Simbot.plugins.music;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.FilterValue;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;
import love.forte.simbot.component.mirai.message.SimbotOriginalMiraiMessage;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.MessageReceipt;
import love.forte.simbot.message.MessagesBuilder;
import net.mamoe.mirai.message.data.MusicKind;
import net.mamoe.mirai.message.data.MusicShare;
import org.Simbot.utils.AsyncHttpClientUtil;
import org.Simbot.utils.SendMsgUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

/**
 * @author mirai
 * @version 1.0
 * @date 2023/09/05 22:47
 * @description 网易云音乐点歌
 */
@Slf4j
@Component
public class CloudMusic {

    @Value("${api.musicApi}")
    private String musicApi;

    @Listener
    @Filter(value = "/点歌 {{text}}", matchType = MatchType.REGEX_CONTAINS)
    public void sendMusic(final GroupMessageEvent event, @FilterValue("text") final String text) {

        final var responsePair = AsyncHttpClientUtil.doGet(musicApi, builder -> {
            builder.addQueryParam("msg", text.trim())
                    .addQueryParam("type", "json");
        });

        final String body = responsePair.getValue().getResponseBody();
        final JSONObject entries = JSONUtil.parseObj(body);
        if (entries.getInt("code") != 200) {
            final var receipt = event.replyBlocking("未找到相关歌曲");
            SendMsgUtil.withdrawMessage(receipt, 10);
            return;
        }

        final MessagesBuilder builder = new MessagesBuilder();
        builder.append("为您找到以下歌曲:\n").append("--------------");

        //最多只能选取前五首歌曲
        final List<JSONObject> beanList = entries.getBeanList("list", JSONObject.class);
        IntStream.range(1, beanList.size() - 1)
                .limit(5)//最多只能选取前五首歌曲
                .forEach(i -> {
                    final JSONObject object = beanList.get(i - 1);
                    final String name = object.getStr("name");
                    final String singer = object.getStr("singer");
                    builder.append("\n" + i + ". " + name + " - " + singer);
                });


        builder.append("\n--------------\n").append("自动选取第一首歌曲");
        final MessageReceipt sent = event.getSource().sendBlocking(builder.build());
        SendMsgUtil.withdrawMessage(sent, 30);

        final SimbotOriginalMiraiMessage music = getMusic(text, "1");
        event.getSource().sendBlocking(music);
    }

    SimbotOriginalMiraiMessage getMusic(final String text, final String num) {
        final var responsePair = AsyncHttpClientUtil.doGet(musicApi, builder -> {
            builder.addQueryParam("msg", text.trim())
                    .addQueryParam("type", "json")
                    .addQueryParam("n", num);
        });
        final String body = responsePair.getValue().getResponseBody();
        final JSONObject entries = JSONUtil.parseObj(body);

        //获取歌曲信息
        final String name = entries.getStr("name");
        final String singer = entries.getStr("singer");
        final String cover = entries.getStr("cover");
        final String url = entries.getStr("url");

        //构建一个音乐分享
        final MusicShare musicShare = new MusicShare(MusicKind.QQMusic, name, singer, url, cover, url);

        //构建一个原生的消息
        return new SimbotOriginalMiraiMessage(musicShare);
    }

}
