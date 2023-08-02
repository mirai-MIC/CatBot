package org.Simbot.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.Simbot.plugins.avSearch.entity.AvDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AvDetailMapper extends BaseMapper<AvDetail> {

    public AvDetail selectByAvNum(@Param("avNum") String avNum);
}