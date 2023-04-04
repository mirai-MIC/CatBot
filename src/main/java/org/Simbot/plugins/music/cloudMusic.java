package org.Simbot.plugins.music;

import com.google.gson.Gson;
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
import org.Simbot.utils.Properties.properties;
import org.springframework.stereotype.Component;

import java.io.IOException;
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

    @lombok.Getter
    @Deprecated
    String musicApi = new properties().getProperties("cache/application.properties", "api.musicApi");

    public cloudMusic() throws IOException {
    }

    @Listener
    @Filter(value = "/点歌 {{text}}", matchType = MatchType.REGEX_CONTAINS)
    public void sendMusic(GroupMessageEvent event, @FilterValue("text") String text) {
        var params = new HashMap<String, Object>();
        params.put("msg", text.trim());
        params.put("num", 1);
        params.put("n", 1);
        OK3HttpClient.httpGetAsync(getMusicApi(), params, null,
                result -> {
                    log.info(result);
                    musicData musicData = new Gson().fromJson(result, musicData.class);
                    if (musicData.getCode() != 200) {
                    } else {
                        var data = musicData.getData();
                        String getPicture = data.getCover();
                        String getMusic = data.getMusic();
                        String getNickUser = data.getSinger();
                        String getMusicUrl = data.getMusicUrl();
                        String getUrl = data.getUrl();
                        var miraiMusicShare = new MiraiMusicShare(MusicKind.NeteaseCloudMusic, getMusic, getNickUser, getMusicUrl, getPicture, getUrl);
                        event.getSource().sendAsync(miraiMusicShare);
                    }
                },
                error -> {
                    log.error("出现异常: \n" + error);
                    event.replyAsync("出现异常: \n" + error.getMessage());
                }
        );
    }
}
