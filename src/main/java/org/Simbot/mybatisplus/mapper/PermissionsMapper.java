package org.Simbot.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.Simbot.mybatisplus.domain.PermissionsData;
import org.apache.ibatis.annotations.Mapper;

/**
 * @BelongsPackage: org.Simbot.mybatisplus.mapper
 * @Author: MIC
 * @CreateTime: 2023-03-07  21:19
 * @Version: 1.0
 */


@Mapper
public interface PermissionsMapper extends BaseMapper<PermissionsData> {
}
