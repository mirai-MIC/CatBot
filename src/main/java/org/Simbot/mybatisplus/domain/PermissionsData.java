package org.Simbot.mybatisplus.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @BelongsProject: simbot
 * @BelongsPackage: org.Simbot.mybatisplus.domain
 * @Author: MIC
 * @CreateTime: 2023-03-07  21:12
 * @Description:
 * @Version: 1.0
 */


@Data
@TableName(value = "PermissionsTables")
public class PermissionsData {

    private Integer id;
    private Integer value;
}
