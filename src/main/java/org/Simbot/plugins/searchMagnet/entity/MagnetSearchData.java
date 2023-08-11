package org.Simbot.plugins.searchMagnet.entity;

import lombok.Data;

import java.util.List;

/**
 * @author ：ycvk
 * @description ：磁力链接搜索结果
 * @date ：2023/08/07 23:23
 */
@Data
public class MagnetSearchData {
    private String type;
    private String fileType;
    private String name;
    private long size;
    private int count;
    private List<Screenshots> screenshots;
}

