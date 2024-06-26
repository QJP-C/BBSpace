package com.be.bang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.be.bang.domain.File;
import org.apache.ibatis.annotations.Mapper;


/**
 * (File)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-17 20:04:27
 */
@Mapper
public interface FileMapper extends BaseMapper<File> {
}

