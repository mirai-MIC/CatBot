package org.Simbot.plugins.magnet.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author ：ycvk
 * @description ：磁力搜索结果实体类
 * @date ：2023/09/07 21:48
 */
@Data
@AllArgsConstructor
public class MagnetSearchEntity {

    //地址
    private String videoLink;
    //标题
    private String videoTitle;
    //磁力链接
    private String magnetLink;
    //做种人数
    private String seeders;
    //下载人数
    private String leechers;
    //发布时间
    private String timestamp;
}
