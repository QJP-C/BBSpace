package com.ruoyi.bang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.bang.domain.TaskHistory;

/**
 * (TaskHistory)表服务接口
 *
 * @author makejava
 * @since 2023-04-19 18:06:11
 */
public interface TaskHistoryService extends IService<TaskHistory> {
    /**
     * 添加历史记录
     * @param openid
     * @param taskId
     * @return
     */
    boolean addHistory(String openid, String taskId);
}

