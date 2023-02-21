package org.Simbot.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.Simbot.mybatisplus.domain.signData;
import org.apache.ibatis.annotations.Mapper;

/**
 * @BelongsPackage: org.Simbot.mybatisplusutils.mapper
 * @Author: MIC
 * @CreateTime: 2023-02-20  13:45
 * @Version: 1.0
 */

@Mapper
public interface SignMapper extends BaseMapper<signData> {
}
