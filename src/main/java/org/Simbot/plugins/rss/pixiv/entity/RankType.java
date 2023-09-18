package org.Simbot.plugins.rss.pixiv.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author : ycvk
 * @description : pixiv排行榜类型
 * @date : 2023/09/18 00:29
 */
@AllArgsConstructor
public enum RankType {
    DAILY_RANK("日榜", "day"),
    WEEKLY_RANK("周榜", "week"),
    MONTHLY_RANK("月榜", "month"),
    MALE_RANK("男性榜", "day_male"),
    FEMALE_RANK("女性榜", "day_female"),
    AI_RANK("AI榜", "day_ai"),
    ORIGINAL_RANK("原创榜", "week_original"),
    ROOKIE_USER_RANK("新人榜", "week_rookie");

    private final String description;

    @Getter
    private final String data;

    private static final Map<String, RankType> DESCRIPTION_TO_TYPE_MAP;

    static {
        DESCRIPTION_TO_TYPE_MAP = Arrays.stream(RankType.values())
                .collect(Collectors.toMap(
                        rankType -> rankType.description.toLowerCase(),
                        rankType -> rankType
                ));
    }

    public static RankType getRankTypeByDesc(final String description) {
        return DESCRIPTION_TO_TYPE_MAP.getOrDefault(description.toLowerCase(), MONTHLY_RANK);
    }
}