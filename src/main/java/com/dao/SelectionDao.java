package com.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.entity.SelectionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.Date;

@Mapper
public interface SelectionDao extends BaseMapper<SelectionEntity> {
    @Update("UPDATE selection SET " +
            "progress = #{progress}, " +
            "status = #{status}, " +
            "completion_time = #{completionTime}, " +
            "last_learn_time = #{lastLearnTime} " +
            "WHERE id = #{id}")
    int updateCompletionStatus(
            @Param("id") Long id,
            @Param("progress") Double progress,
            @Param("status") Integer status,
            @Param("completionTime") Date completionTime,
            @Param("lastLearnTime") Date lastLearnTime
    );

    /**
     * 根据用户ID和课程ID查询选课记录
     */
    SelectionEntity selectByUserAndCourse(@Param("userId") Long userId,
                                       @Param("courseId") Long courseId);

    /**
     * 更新选课记录的进度信息
     */
    int updateProgress(SelectionEntity selection);

}
