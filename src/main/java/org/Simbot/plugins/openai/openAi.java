package org.Simbot.plugins.openai;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;
import love.forte.simbot.event.GroupMessageEvent;
import org.Simbot.plugins.openai.data.openAiData;
import org.Simbot.utils.HttpClient4Util;
import org.Simbot.utils.Msg;
import org.Simbot.utils.Properties.properties;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @author mirai
 * @version 1.0
 * @packAge: org.openai
 * @date 2023/1/4 16:40
 */

@Component
@Slf4j
public class openAi {
    @lombok.Getter
    @Deprecated
    String properties = new properties().getProperties("cache/application.properties", "user.Master");

    public openAi() throws IOException {
    }

    /**
     * openAi 搜索插件
     *
     * @param event
     */
    @Listener
    @Filter(value = "/q ", matchType = MatchType.REGEX_CONTAINS)
    public void getOpenAi(@NotNull GroupMessageEvent event) {
        if (event.getAuthor().getId().equals(Msg.Id(getProperties().trim()))) {
            var gson = new Gson();
            var params = new HashMap<String, Object>();
            params.put("model", "text-davinci-003");
            params.put("prompt", new Scanner(event.getMessageContent().getPlainText().substring(3)).next());
            params.put("max_tokens", 4000);
            String post = HttpClient4Util.getPost("https://api.openai.com/v1/completions", gson.toJson(params));
            openAiData openAiData = gson.fromJson(post, openAiData.class);
            var choices = openAiData.getChoices();
            if (choices == null) return;
            choices.forEach(text -> {
                event.replyAsync(text.getText());
                log.info(text.getText());
            });
        }
    }
}
