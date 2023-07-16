package org.Simbot.plugins.openai;

import cn.hutool.core.map.SafeConcurrentHashMap;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;
import love.forte.simbot.event.GroupMessageEvent;
import org.Simbot.plugins.openai.data.AIModel;
import org.Simbot.plugins.openai.data.MessageEntity;
import org.Simbot.plugins.openai.data.UserConversation;
import org.Simbot.utils.OK3HttpClient;
import org.Simbot.utils.SendMsgUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    static final Map<String, UserConversation> USER_CONVERSATION_MAP = new SafeConcurrentHashMap<>();

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
        //获取发送人的 QQ号
        final String qq = event.getAuthor().getId().toString();
        //通过QQ号获取用户会话
        final UserConversation userConversation = getUserConversation(qq);
        final List<MessageEntity> messages = userConversation.getMessages();
        //获取用户输入的内容
        final String next = event.getMessageContent().getPlainText().substring(3);
        //将用户输入的内容添加到会话中
        messages.add(new MessageEntity(next, "user"));
        final String prompt = userConversation.getPrompt();
        //将用户输入的内容和会话内容拼接成输入内容
        final Map<String, Object> modelConstructor = requestModelConstructor(userConversation.getModelType(),
                userConversation.getConversationId(),
                prompt,
                messages);
        //发送请求
        OK3HttpClient.httpPostAsync(ChatGPTUrl,
                modelConstructor,
                headMap,
                result -> {
                    SendMsgUtil.sendReplyGroupMsg(event, "\n" + result);
                    messages.add(new MessageEntity(result, "assistant"));
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

    private UserConversation getUserConversation(final String qqId) {
        UserConversation conversation = USER_CONVERSATION_MAP.get(qqId);
        if (conversation == null) {
            conversation = new UserConversation()
                    .setId(qqId)
                    .setConversationId(IdUtil.fastSimpleUUID())
                    .setModelType("GPT4")//todo 自定义模型与自定义prompt待实现
                    .setMessages(new ArrayList<>())
            ;
            USER_CONVERSATION_MAP.put(qqId, conversation);
        }
        return conversation;
    }
}
