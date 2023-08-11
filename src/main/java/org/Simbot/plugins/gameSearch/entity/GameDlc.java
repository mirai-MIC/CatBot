package org.Simbot.plugins.gameSearch.entity;

import lombok.Data;

/**
 * @author ：ycvk
 * @description ：游戏DLC
 * @date ：2023/08/11 15:26
 */
@Data
public class GameDlc {

    private int gameCount;
    private String name;
    private String image;
    private int bundleId;
    private GamePrice price;
}
