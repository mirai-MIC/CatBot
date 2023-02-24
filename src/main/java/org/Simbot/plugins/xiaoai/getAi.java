package org.Simbot.plugins.xiaoai;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.MessagesBuilder;
import org.Simbot.plugins.xiaoai.Xutils.data;
import org.Simbot.utils.Properties.properties;
import org.springframework.stereotype.Controller;

import java.io.IOException;

import static org.Simbot.utils.HttpUtils.get;

/**
 * @author mirai
 * @version 1.0
 * @packAge: org.Simbot.Plugins.xiaoAi
 * @date 2022/12/6 14:50
 */
@Slf4j
@Controller
public class getAi {
    @Deprecated
    @lombok.Getter
    String AiUrl = new properties().getProperties("cache/application.properties", "api.xiaoAi");

    public getAi() throws IOException {

    }


    @Listener
    @Filter(targets = @Filter.Targets(atBot = true))
    public void at(GroupMessageEvent event) {
        MessagesBuilder builder = new MessagesBuilder();
        String plainText = event.getMessageContent().getPlainText().trim();
        if (plainText.equals("")) {
            event.replyAsync("参数为空");
            log.info("小爱插件产生空参数异常");
            return;
        } else {
            try {
                data data = new Gson().fromJson(get(getAiUrl() + plainText), data.class);
                builder.at(event.getAuthor().getId());
                builder.append("\t\t" + data.getResponseText());
            } catch (Exception e) {
                log.info("Q管家异常");
            }
        }
        event.getSource().sendBlocking(builder.build());

    }
}
