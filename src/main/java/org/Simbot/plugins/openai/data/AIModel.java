package org.Simbot.plugins.openai.data;

import lombok.Data;

@Data
public class AIModel {
    private String id;
    private String name;
    private int maxLength;
    private int tokenLimit;

    public AIModel(final String type) {
        switch (type) {
            case "GPT4" -> {
                this.id = "gpt-4-0613";
                this.name = "GPT-4";
                this.maxLength = 24000;
                this.tokenLimit = 8000;
            }
            case "GPT3" -> {
                this.id = "gpt-3.5-turbo-16k";
                this.name = "GPT-3.5-16k";
                this.maxLength = 48000;
                this.tokenLimit = 16000;
            }
            case "CLAUDE" -> {
                this.id = "claude-2-100k";
                this.name = "claude-2-100k";
                this.maxLength = 400000;
                this.tokenLimit = 100000;
            }
        }
    }
}
