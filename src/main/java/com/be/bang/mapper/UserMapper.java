package com.be.bang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.be.bang.domain.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * (User)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-13 16:44:27
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}

