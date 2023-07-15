package org.Simbot.plugins.openai;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;
import love.forte.simbot.event.GroupMessageEvent;
import org.Simbot.plugins.openai.data.AIModel;
import org.Simbot.plugins.openai.data.MessageEntity;
import org.Simbot.utils.OK3HttpClient;
import org.Simbot.utils.SendMsgUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Component
@Slf4j
//@PropertySource("file:./cache/application.properties")//用此方法获取或者直接在yml中注入配置
public class openAI {
    @Value("${user.Master}")
    String master;

    @Value("${chatGPT.url}")
    String ChatGPTUrl;

    @Value("${chatGPT.Cookie}")//有些非官方的ChatGPT-API需要Cookie
    String cookie;

    @Value("${chatGPT.X-Auth-Code}")//有些非官方的ChatGPT-API需要X-Auth-Code
    String authCode;

    /**
     * openAI 搜索插件
     *
     * @param event 群消息事件
     */
    @Listener
    @Filter(value = "/q ", matchType = MatchType.REGEX_CONTAINS)
    public void getOpenAI(@NotNull final GroupMessageEvent event) {
        final Map<String, String> headMap = new HashMap<>();
        headMap.put("Cookie", cookie);
        headMap.put("X-Auth-Code", authCode);
        headMap.put("Content-Type", "application/json");
        final String prompt = """
                你是 ChatGPT，一个由 OpenAI 训练的大型语言模型，请仔细遵循用户的指示。请仔细的一步步思考，保证回答的合理性，准确性。
                """;
        final String next = new Scanner(event.getMessageContent().getPlainText().substring(3)).next();
        final Map<String, Object> modelConstructor = requestModelConstructor("GPT3",
                IdUtil.fastSimpleUUID(),
                prompt,
                List.of(new MessageEntity(next, "user")));
        OK3HttpClient.httpPostAsync(ChatGPTUrl,
                modelConstructor,
                headMap,
                result -> {
                    SendMsgUtil.sendReplyGroupMsg(event, result);
                    log.info("openAI result: {}", result);
                },
                error -> log.error("openAI error: {}", error.getMessage()),
                10);
    }

    private Map<String, Object> requestModelConstructor(final String modelType, final String conversationId, final String prompt, final List<MessageEntity> messages) {
        final var params = new HashMap<String, Object>();
        final AIModel model = new AIModel(modelType);
        params.put("model", model);
        params.put("conversationId", conversationId);
        params.put("messages", messages);
        params.put("prompt", prompt);
        return params;
    }
}
