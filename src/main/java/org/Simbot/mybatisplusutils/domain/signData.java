package org.Simbot.mybatisplusutils.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @BelongsProject: simbot
 * @BelongsPackage: org.Simbot.mybatisplusutils.domain
 * @Author: MIC
 * @CreateTime: 2023-02-20  13:43
 * @Description:
 * @Version: 1.0
 */

@Data
@TableName("sumplus")
public class signData {
    private Long id;
    private Long sum;
    private String daytime;
    private Long sumday;

}
