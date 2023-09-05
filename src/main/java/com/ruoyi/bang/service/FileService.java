package com.ruoyi.bang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.bang.domain.File;

/**
 * (File)表服务接口
 *
 * @author makejava
 * @since 2023-04-17 20:04:27
 */
public interface FileService extends IService<File> {
    /**
     * 添加帖子附件
     * @param url
     * @param postId
     * @return
     */
    boolean addPostFile(String url, String postId);

    /**
     * 获取帖子附件
     * @param postId
     * @return
     */
    String[] getPostFiles(String postId);
    /**
     * 获取任务附件
     * @param taskId
     * @return
     */
    String[] getTaskFiles(String taskId);

    /**
     * 添加完成任务附件
     * @param url
     * @param taskId
     * @return
     */
    boolean addTaskFile(String url, String taskId);
}

