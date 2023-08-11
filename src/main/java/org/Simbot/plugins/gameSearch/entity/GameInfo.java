package org.Simbot.plugins.gameSearch.entity;

import lombok.Data;

import java.util.List;

/**
 * @author ：ycvk
 * @description ：游戏信息
 * @date ：2023/08/11 13:37
 */
@Data
public class GameInfo {

    private int steamAppid;

    private String image;

    private List<String> platforms;

    private String positiveDesc;

    private List<String> genres;

    private String name;

    private String nameEn;

    private GamePrice price;

    private List<GameDlc> dlcs;

    private int supportChinese;

    private List<GameOnlineData> gameOnlineData;
}


