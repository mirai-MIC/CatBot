package org.Simbot.plugins.openai.data;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class UserConversation {

    //qq号
    private String id;
    //会话id
    private String conversationId;
    //系统prompt
    private String prompt = """
               你是一个人工智能的大型语言模型，请仔细遵循用户的指示。请仔细的一步步思考，保证回答的合理性，准确性。
            """;
    //用户输入
    private List<MessageEntity> messages;
    //模型类型
    private String modelType;
}
