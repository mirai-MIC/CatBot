package org.Simbot.plugins.gameSearch.entity;

import lombok.Data;

import java.util.List;

/**
 * @author ：ycvk
 * @description ：小黑盒游戏搜索数据实体类
 * @date ：2023/08/11 11:34
 */
@Data
public class BlackBoxSearchEntity {
    private int steamAppid;

    private String image;

    //游戏平台
    private List<String> platforms;

    //游戏分数
    private String score;

    //游戏价格
    private GamePrice price;

    //是否免费
    private boolean isFree;

    private String name;

    private String gameType;
}

