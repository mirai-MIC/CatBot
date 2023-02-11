package org.Simbot.plugins.music;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.FilterValue;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;
import love.forte.simbot.component.mirai.message.MiraiMusicShare;
import love.forte.simbot.event.GroupMessageEvent;
import net.mamoe.mirai.message.data.MusicKind;
import org.Simbot.plugins.music.data.musicData;
import org.Simbot.utils.OK3HttpClient;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.HashMap;

/**
 * @author mirai
 * @version 1.0
 * @className mi
 * @data 2023/01/20 20:07
 * @description
 */
@Slf4j
@Component
public class cloudMusic {
    @Listener
    @Filter(value = "/点歌 {{text}}", matchType = MatchType.REGEX_CONTAINS)
    public void sendMusic(GroupMessageEvent event, @FilterValue("text") String text) {
        var params = new HashMap<String, Object>();
        params.put("msg", text.trim());
        params.put("num", 1);
        params.put("n", 1);
        try {
            String musicJson = OK3HttpClient.httpGet("https://www.dreamling.xyz/API/163/music/api.php", params, null);
            log.info(musicJson);
            musicData musicData = new Gson().fromJson(musicJson, musicData.class);
            int code = musicData.getCode();
            if (code == 200) {
                var data = musicData.getData();
                String getPicture = data.getCover();
                String getMusic = data.getMusic();
                String getNickUser = data.getSinger();
                String getMusicUrl = data.getMusicUrl();
                String getUrl = data.getUrl();
                var miraiMusicShare = new MiraiMusicShare(MusicKind.NeteaseCloudMusic, getMusic, getNickUser, getMusicUrl, getPicture, getUrl);
                event.getSource().sendBlocking(miraiMusicShare);
            } else {
                log.error("code异常.....");
                event.replyAsync("code异常..： " + code);
            }
        } catch (JsonSyntaxException e) {
            log.error(MessageFormat.format("歌曲插件异常,疑似接口出现错误: {0}", e.getMessage()));
            event.replyAsync(MessageFormat.format("歌曲插件异常,疑似接口出现错误: {0}", e.getMessage()));
        }
    }
}
