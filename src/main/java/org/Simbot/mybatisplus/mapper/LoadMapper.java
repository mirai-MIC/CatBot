package org.Simbot.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.Simbot.mybatisplus.domain.loadData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * @BelongsPackage: org.Simbot.mybatisplusutils.mapper
 * @Author: MIC
 * @CreateTime: 2023-02-20  13:26
 * @Version: 1.0
 */

@Mapper
public interface LoadMapper extends BaseMapper<loadData> {
    @Update("TRUNCATE loadday;")
    void deleteUser();
}
