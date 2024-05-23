package com.be.bang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.be.bang.domain.TaskClass;
import org.apache.ibatis.annotations.Mapper;

/**
 * (TaskClass)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-17 11:42:45
 */

@Mapper
public interface TaskClassMapper extends BaseMapper<TaskClass> {
}

