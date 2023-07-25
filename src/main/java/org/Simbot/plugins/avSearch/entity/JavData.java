package org.Simbot.plugins.avSearch.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author ：ycvk
 * @description ：javbus数据实体类
 * @date ：2023/07/24 12:43
 */
@Accessors(chain = true)
@AllArgsConstructor
@Getter
public class JavData {
    private String title;

    private String actors;

    private String releaseDate;

    private String coverImage;

    private List<String> previewImages;

    private List<String> magnetLink;

    private String avNumber;

    private List<String> categories;

    @Override
    public String toString() {
        return """
                番号：%s \n
                标题：%s \n
                演员：%s \n
                发行日期：%s \n
                封面：%s \n
                预览图：%s \n
                磁力链接：%s
                """.formatted(avNumber, title, actors, releaseDate, coverImage, previewImages, magnetLink);
    }
}
