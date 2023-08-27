package org.Simbot.plugins.avSearch.entity;

import lombok.Data;

import java.util.List;

/**
 * @author ：ycvk
 * @description ：通过自建爬虫(metaTube)获取的视频详情
 * @date ：2023/08/27 10:37
 */
@Data
public class CustomDetailEntity {
    private String id;
    private String number;
    private String title;
    private String provider;
    private String homepage;
    private String thumbUrl;
    private String coverUrl;
    private int score;
    private List<String> actors;
    private String releaseDate;

}
