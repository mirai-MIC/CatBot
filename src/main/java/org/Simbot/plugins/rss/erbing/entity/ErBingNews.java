package org.Simbot.plugins.rss.erbing.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ：ycvk
 * @description ：二柄新闻实体类
 * @date ：2023/09/21 12:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErBingNews {
    private String title;
    private String link;
    private String content;
    private List<String> img;
    private String publishedDate;
}
