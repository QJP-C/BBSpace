package com.be.bang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.be.bang.common.R;
import com.be.bang.dto.TaskNewClassDto;
import com.be.bang.domain.TaskClass;

import java.util.List;

/**
 * (TaskClass)表服务接口
 *
 * @author makejava
 * @since 2023-04-17 19:48:07
 */
public interface TaskClassService extends IService<TaskClass> {
    /**
     * 获取类型列表
     * @return
     */
    R<List<TaskClass>> getType();

    /**
     * 新增分类
     * @param taskNewClassDto
     * @return
     */
    R<String> newClass(TaskNewClassDto taskNewClassDto);

    /**
     * 获取类型名称
     * @param openid
     * @return
     */
    String typesName(String typeId);
}

