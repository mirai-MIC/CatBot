package org.Simbot.plugins.rss.pixiv.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ：ycvk
 * @description ：pixiv实体类
 * @date ：2023/09/18 01:06
 */
@Data
@Accessors(chain = true)
public class Pixiv implements Comparable<Pixiv> {
    private String title;
    private String link;
    private String author;
    private String readNum;
    private String collectNum;
    private List<String> imgLink;

    @Override
    public int compareTo(@NotNull final Pixiv other) {
        final int num1 = extractNumber(this.title);
        final int num2 = extractNumber(other.title);
        return Integer.compare(num1, num2);
    }

    private int extractNumber(final String title) {
        final Matcher matcher = Pattern.compile("#(\\d+)").matcher(title);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 0;
    }
}
