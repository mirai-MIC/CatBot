package org.Simbot.plugins.openai;

import cn.hutool.core.map.SafeConcurrentHashMap;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.MessageReceipt;
import org.Simbot.plugins.openai.data.AIModel;
import org.Simbot.plugins.openai.data.MessageEntity;
import org.Simbot.plugins.openai.data.UserConversation;
import org.Simbot.utils.OK3HttpClient;
import org.Simbot.utils.SendMsgUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

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
    @Filter(value = "/q ", matchType = MatchType.TEXT_STARTS_WITH)
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
        if (next.equals("reset")) {
            messages.clear();
            SendMsgUtil.sendReplyGroupMsg(event, "已重置会话");
            return;
        }
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
                    final MessageReceipt receipt = SendMsgUtil.sendReplyGroupImg(event, result);
//                    SendMsgUtil.withdrawMessage(receipt, 55);
                    messages.add(new MessageEntity(result, "assistant"));
//                    log.info("openAI result: {}", result);
                    log.info("userConversation: {}", userConversation);
                },
                error -> log.error("openAI error: {}", error.getMessage()),
                10);
    }

    @Listener
    @Filter(value = "/prompt ", matchType = MatchType.TEXT_STARTS_WITH)
    public void setPrompt(@NotNull final GroupMessageEvent event) {
        //获取发送人的 QQ号
        final String qq = event.getAuthor().getId().toString();
        //通过QQ号获取用户会话
        final UserConversation userConversation = getUserConversation(qq);
        final List<MessageEntity> messages = userConversation.getMessages();
        //获取用户输入的内容
        final String next = event.getMessageContent().getPlainText().substring(8);
        if (StrUtil.isBlank(next)) {
            SendMsgUtil.sendReplyGroupMsg(event, "prompt不能为空");
            return;
        }
        messages.clear();
        userConversation.setPrompt(next).setConversationId(IdUtil.fastSimpleUUID());
        SendMsgUtil.sendReplyGroupMsg(event, "已设置prompt,自动清空会话");
    }

    @Listener
    @Filter(value = "/model ", matchType = MatchType.TEXT_STARTS_WITH)
    public void setModel(@NotNull final GroupMessageEvent event) {
        //获取发送人的 QQ号
        final String qq = event.getAuthor().getId().toString();
        //通过QQ号获取用户会话
        final UserConversation userConversation = getUserConversation(qq);
        final List<MessageEntity> messages = userConversation.getMessages();
        //获取用户输入的内容
        final String next = event.getMessageContent().getPlainText().substring(7).toUpperCase(Locale.ROOT);
        if (StrUtil.isBlank(next)) {
            SendMsgUtil.sendReplyGroupMsg(event, "model不能为空");
            return;
        }
        if (!next.equals("GPT4") && !next.equals("GPT3") && !next.equals("CLAUDE")) {
            SendMsgUtil.sendReplyGroupMsg(event, "model只能为GPT4,GPT3,CLAUDE");
            return;
        }
        if (next.equals("CLAUDE")) {
            userConversation.setPrompt("""
                    Please adhere to the following structured reasoning framework to craft response:
                    1. Decomposition of the Problem: Disassemble the complex problem into smaller, manageable elements termed as "thoughts". These "thoughts" function as standalone sub-problems, the solutions of which collectively contribute towards the resolution of the overarching problem.
                    2. Ideation of Potential Thoughts: For each isolated "thought", brainstorm multiple probable reasoning routes or solutions that may be feasible.
                    3. Assessment of Thoughts: Carry out self-appraisal of the incremental progress achieved by each "thought" towards problem-solving via a meticulous reasoning process. Ensure this is done methodically for each "thought".
                    4. Optimal Reasoning Path Selection: Prior to the selection of a reasoning path, undertake a thorough review of all potential routes. This should entail contrasting the pros and cons of each path and examining how effectively they address the root problem. Post the comprehensive evaluation of "thoughts" and exploration of reasoning pathways, you are to elect the most suitable course of action for problem resolution.
                    5. Generation of the Final Response: Contemplate on the response prior to its finalization. This might include validating the response for its logical consistency, relevance, and precision. Once the most effective reasoning path has been identified, you can put together a comprehensive and cohesive response that aptly addresses the initial problem or query.
                    """
            );
        }
        messages.clear();
        userConversation.setModelType(next).setConversationId(IdUtil.fastSimpleUUID()).setMessages(new ArrayList<>());
        SendMsgUtil.sendReplyGroupMsg(event, "已设置model为 " + next + " ,自动清空会话");
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
                    .setModelType("GPT4")
                    .setMessages(new ArrayList<>())
            ;
            USER_CONVERSATION_MAP.put(qqId, conversation);
        }
        return conversation;
    }
}
