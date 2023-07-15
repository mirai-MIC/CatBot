package org.Simbot.plugins.card;

import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.FilterValue;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;
import love.forte.simbot.component.mirai.message.SimbotOriginalMiraiMessage;
import love.forte.simbot.event.GroupMessageEvent;
import net.mamoe.mirai.message.data.LightApp;
import org.springframework.stereotype.Component;

/**
 * @BelongsProject: simbot
 * @BelongsPackage: org.Simbot.plugins.card
 * @Author: MIC
 * @CreateTime: 2023-02-12  14:21
 * @Description:
 * @Version: 1.0
 */

@Component
@Slf4j
public class sendCard {
    /**
     * 发送卡片消息
     *
     * @param event 事件
     * @param text  消息
     */

    @Listener
    @Filter(value = "/c {{text}}", matchType = MatchType.REGEX_MATCHES)
    public void sendCardMessage(final GroupMessageEvent event, @FilterValue("text") final String text) {
//        event.getSource().sendBlocking(new MiraiSendOnlyAudio(Resource.of(new URL("https://m10.music.126.net/20230212210343/c174ba3c631d68800a1ed06205fd5d19/ymusic/8db1/fb75/0b7f/bfab110266c21818ae8e91ebdbe8d7ba.mp3"))));
//        String aaa = "{\"app\":\"com.tencent.gamecenter.gameshare\",\"desc\":\"\",\"view\":\"noDataView\",\"ver\":\"0.0.0.0\",\"prompt\":\"让妲己看看你的心~\",\"appID\":\"\",\"sourceName\":\"\",\"actionData\":\"\",\"actionData_A\":\"\",\"sourceUrl\":\"\",\"meta\":{\"shareData\":{\"DATA10\":\"\",\"DATA13\":\"0\",\"DATA14\":\"videotest1\",\"jumpUrl\":\"http:\\/\\/game.gtimg.cn\\/images\\/yxzj\\/zlkdatasys\\/audios\\/audio\\/20220412\\/16497568317400.wav\",\"scene\":\"SCENE_SHARE_VIDEO\",\"type\":\"video\",\"url\":\"http:\\/\\/game.gtimg.cn\\/images\\/yxzj\\/zlkdatasys\\/audios\\/audio\\/20220412\\/16497568317400.wav\"}},\"config\":{\"ctime\":1674022176,\"forward\":0,\"height\":-1000,\"token\":\"61fe0a996f85e161b98fb748ff6f1209\",\"type\":\"normal\",\"width\":-1000},\"text\":\"\",\"sourceAd\":\"\",\"extra\":\"\"}";
        try {
            event.getSource().sendBlocking(new SimbotOriginalMiraiMessage(new LightApp(event.getMessageContent().getPlainText().substring(3))));
        } catch (final Exception e) {
            event.getSource().sendBlocking("发送错误");
            log.error("卡片插件异常...  " + e.getMessage());
        }
    }
}

