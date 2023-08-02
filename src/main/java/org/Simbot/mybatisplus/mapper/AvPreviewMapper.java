package org.Simbot.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.Simbot.plugins.avSearch.entity.AvPreview;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AvPreviewMapper extends BaseMapper<AvPreview> {


    List<String> selectByAvNum(@Param("avNum") String avNum);

    void insertList(@Param("avNum") String avNum, @Param("imgList") List<String> previewImages);
}