package org.Simbot.plugins.openai.data;

import kotlinx.serialization.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Serializable
@AllArgsConstructor
public class MessageEntity {
    String content;
    String role;
}
