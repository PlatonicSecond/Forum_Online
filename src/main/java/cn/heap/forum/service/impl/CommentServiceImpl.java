package cn.heap.forum.service.impl;

import cn.heap.forum.mapper.CommentMapper;
import cn.heap.forum.pojo.Comment;
import cn.heap.forum.service.CommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Autowired
    private CommentMapper commentMapper;

    /**
     * 添加评论
     *
     * @param comment 评论实体
     * @return 插入成功的记录数
     */
    @Override
    public int addComment(Comment comment) {
        try {
            comment.setCreateTime(new Date());
            return commentMapper.insert(comment);
        } catch (Exception e) {
            logger.error("添加评论失败", e);
            return 0;
        }
    }

    /**
     * 查询某个帖子下的所有评论
     *
     * @param postId 帖子的ID
     * @return 评论列表
     */
    @Override
    public List<Comment> getCommentsByPostId(Integer postId) {
        try {
            return commentMapper.selectList(new QueryWrapper<Comment>().eq("post_id", postId));
        } catch (Exception e) {
            logger.error("查询某个帖子下的所有评论时出现异常，帖子ID: {}", postId, e);
            return null;
        }
    }

    /**
     * 更新评论
     *
     * @param comment 要更新的评论对象
     * @return 更新影响的记录数
     */
    @Override
    public int updateComment(Comment comment) {
        try {
            return commentMapper.updateById(comment); // 使用 MyBatis-Plus 的 updateById 方法
        } catch (Exception e) {
            logger.error("更新评论时出现异常，评论ID: {}", comment.getCommentId(), e);
            return 0;
        }
    }


    /**
     * 删除评论
     *
     * @param commentId 要删除的评论ID
     * @return 删除影响的记录数
     */
    @Override
    public int deleteComment(Integer commentId) {
        try {
            return commentMapper.deleteById(commentId); // 使用 MyBatis-Plus 的 deleteById 方法
        } catch (Exception e) {
            logger.error("删除评论时出现异常，评论ID: {}", commentId, e);
            return 0;
        }
    }


}