package org.Simbot.config.gson;

import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * @author ：ycvk
 * @description ：自定义gson类型适配
 * @date ：2023/09/03 18:01
 */
public class GsonTypeAdapter {
    // Initialize GsonBuilder
    private static final GsonBuilder builder = new GsonBuilder();

    /**
     * 正确处理 URL 编码问题
     *
     * @return GsonBuilder
     */
    public static GsonBuilder getGsonTypeBuilder() {
        // Register a custom TypeAdapter for String class
        builder.registerTypeAdapter(String.class, new TypeAdapter<String>() {
            @Override
            public void write(final JsonWriter out, final String value) throws IOException {
                // Serialize the string as-is
                out.value(value);
            }

            @Override
            public String read(final JsonReader in) throws IOException {
                // Deserialize and replace escape characters
                final String str = in.nextString();
                return str.replace("\\/", "/");
            }
        });
        return builder;
    }

}
