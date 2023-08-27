package org.Simbot.plugins.avSearch.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@TableName(value = "av_detail")
@NoArgsConstructor
@Accessors(chain = true)
public class AvDetail implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * AV番号
     */
    @TableField(value = "av_Num")
    @Size(max = 100, message = "AV番号最大长度要小于 100")
    @NotBlank(message = "AV番号不能为空")
    private String avNum;

    /**
     * AV标题
     */
    @TableField(value = "title")
    @Size(max = 1000, message = "标题最大长度要小于 1000")
    private String title;

    /**
     * 影片演员
     */
    @TableField(value = "actors")
    @Size(max = 255, message = "影片演员最大长度要小于 255")
    private String actors;

    /**
     * 视频封面地址
     */
    @TableField(value = "cover_Url")
    @Size(max = 255, message = "视频封面地址最大长度要小于 255")
    private String coverUrl;

    /**
     * 视频磁力链接
     */
    @TableField(value = "magnet_Link")
    @Size(max = 5000, message = "视频磁力链接最大长度要小于 5000")
    private String magnetLink;

    /**
     * 高清磁力链接
     */
    @TableField(value = "magnet_link_hd")
    @Size(max = 5000, message = "视频磁力链接最大长度要小于 5000")
    private String magnetLinkHd;

    /**
     * 字幕磁力链接
     */
    @TableField(value = "magnet_link_sub")
    @Size(max = 2000, message = "视频磁力链接最大长度要小于 2000")
    private String magnetLinkSub;

    /**
     * 在线播放地址
     */
    @TableField(value = "online_play_Url")
    @Size(max = 500, message = "在线播放地址最大长度要小于 500")
    private String onlinePlayUrl;

    /**
     * 作品描述
     */
    @TableField(value = "description")
    @Size(max = 1000, message = "作品描述最大长度要小于 1000")
    private String description;

    /**
     * 作品时长
     */
    @TableField(value = "duration")
    private Integer duration;

    /**
     * 作品类别
     */
    @TableField(value = "categories")
    @Size(max = 1000, message = "作品类别最大长度要小于 1000")
    private String categories;

    /**
     * 发布日期
     */
    @TableField(value = "release_Date")
    private Timestamp releaseDate;

    @TableField(exist = false)
    private transient List<String> previewImages;

    @TableField(exist = false)
    private transient String mainUrl;

}