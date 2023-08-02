package org.Simbot.plugins.avSearch.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

@TableName(value = "av_preview")
public class AvPreview implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "不能为null")
    private Integer id;

    /**
     * AV番号
     */
    @TableField(value = "av_num")
    @Size(max = 100, message = "AV番号最大长度要小于 100")
    @NotBlank(message = "AV番号不能为空")
    private String avNum;

    /**
     * 预览图地址
     */
    @TableField(value = "preview_url")
    @Size(max = 255, message = "预览图地址最大长度要小于 255")
    private String previewUrl;

}