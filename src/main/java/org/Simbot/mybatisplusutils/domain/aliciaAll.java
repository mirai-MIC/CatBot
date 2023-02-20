package org.Simbot.mybatisplusutils.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @BelongsProject: simbot
 * @BelongsPackage: org.Simbot.mybatisplusutils.domain
 * @Author: MIC
 * @CreateTime: 2023-02-20  12:17
 * @Description:
 * @Version: 1.0
 */

@Data
@TableName("alicia_all")
public class aliciaAll {

    private Integer id;
    private String url;
    private Long time;
    private String date;
}
