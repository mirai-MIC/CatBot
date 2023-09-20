package org.Simbot.plugins.rss.pixiv.entity;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
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
    DAILY_RANK("日榜", "day", DateUtil.yesterday().toDateStr()),
    WEEKLY_RANK("周榜", "week", DateUtil.yesterday().toDateStr()),
    MONTHLY_RANK("月榜", "month", DateUtil.format(DateUtil.date(), DatePattern.NORM_MONTH_PATTERN)),
    MALE_RANK("男性榜", "day_male", DateUtil.yesterday().toDateStr()),
    FEMALE_RANK("女性榜", "day_female", DateUtil.yesterday().toDateStr()),
    AI_RANK("AI榜", "day_ai", DateUtil.yesterday().toDateStr()),
    ORIGINAL_RANK("原创榜", "week_original", DateUtil.yesterday().toDateStr()),
    ROOKIE_USER_RANK("新人榜", "week_rookie", DateUtil.yesterday().toDateStr()),
    R18_DAY("色图日榜", "day_r18", DateUtil.yesterday().toDateStr()),
    R18_AI("色图ai榜", "day_r18_ai", DateUtil.yesterday().toDateStr()),
    R18_MALE("色图男性榜", "day_male_r18", DateUtil.yesterday().toDateStr()),
    R18_FEMALE("色图女性榜", "day_female_r18", DateUtil.yesterday().toDateStr()),
    R18_WEEK("色图周榜", "week_r18", DateUtil.yesterday().toDateStr()),
    R18_WEEK_G("色图G榜", "week_r18g", DateUtil.yesterday().toDateStr());

    private final String description;

    @Getter
    private final String data;

    @Getter
    private final String defaultDate;

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