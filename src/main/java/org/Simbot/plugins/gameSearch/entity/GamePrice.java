package org.Simbot.plugins.gameSearch.entity;

import lombok.Data;

@Data
public class GamePrice {
    // 当前价格
    private String current;
    // 截止时间戳
    private long deadlineTimestamp;
    // 初始价格
    private String initial;
    // 是否最低价
    private int isLowest;
    // 折扣百分比
    private int discount;
    // 截止日期描述（例如"剩余5天"）
    private String deadlineDate;
    // 最低价格（原始）
    private String lowestPriceRaw;
    // 最低价格
    private int lowestPrice;
    // 是否新的最低价格
    private int newLowest;
    // 最低折扣百分比
    private int lowestDiscount;
}
