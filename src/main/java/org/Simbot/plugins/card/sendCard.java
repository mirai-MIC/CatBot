package org.Simbot.plugins.card;

import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
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

//@Component
public class sendCard {


    @Listener
    @Filter(value = "/卡片")
    public void sendCardMessage(GroupMessageEvent event) {
        String xmlApp = """
                {
                  "app":"com.tencent.miniapp",
                  "desc":"",
                  "view":"notification",
                  "ver":"0.0.0.1",
                  "prompt":"text",
                  "appID":"","sourceName":"",
                  "actionData":"",
                  "actionData_A":"",
                  "sourceUrl":"",
                  "meta":{
                    "notification":{
                    "appInfo":{
                      "appName":"MusicCatBot",
                      "appType":4,
                      "appid":2034149631,
                      "iconUrl":"https:\\/\\/gchat.qpic.cn\\/gchatpic_new\\/0\\/0-0-D702BD9E998F5A25AD7F80272C5DA253\\/0"
                      },
                      "data":[{
                        "title":"菜单",
                        "value":"点歌\t\t签到\t\t色图
                 "
                        }],
                        "emphasis_keyword":""
                        }},
                        "text":"",
                        "sourceAd":"",
                        "extra":""
                  }""";
        event.getSource().sendBlocking(new SimbotOriginalMiraiMessage(new LightApp(xmlApp)));
    }
}

