package org.Simbot.plugins.avSearch.entity;

import lombok.Data;

/**
 * @author ：ycvk
 * @description ： 搜索FC2视频实体类
 * @date ：2023/08/13 16:11
 */
@Data
public class FC2SearchEntity {

    private String id;
    private String number;
    private String title;
    private String provider;
    private String homepage;
    private String thumbUrl;
    private String coverUrl;
    private int score;
    private String releaseDate;
}
