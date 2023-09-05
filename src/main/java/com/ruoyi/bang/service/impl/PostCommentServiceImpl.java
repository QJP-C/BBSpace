package com.ruoyi.bang.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.bang.domain.PostComment;
import com.ruoyi.bang.mapper.PostCommentMapper;
import com.ruoyi.bang.service.PostCommentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * (PostComment)表服务实现类
 *
 * @author makejava
 * @since 2023-04-25 21:49:23
 */
@Service("postCommentService")
public class PostCommentServiceImpl extends ServiceImpl<PostCommentMapper, PostComment> implements PostCommentService {
    /**
     * 评论帖子
     *
     * @param openid
     * @param postId
     * @param text
     * @return
     */
    @Override
    public boolean commentPost(String openid, String postId, String text) {
        PostComment postComment = new PostComment();
        postComment.setUserId(openid);
        postComment.setText(text);
        postComment.setPostId(postId);
        postComment.setCommentTime(LocalDateTime.now());
        return this.save(postComment);
    }


}

