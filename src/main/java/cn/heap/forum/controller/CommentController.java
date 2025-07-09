package cn.heap.forum.controller;

import cn.heap.forum.pojo.Comment;
import cn.heap.forum.service.CommentService;
import cn.heap.forum.util.ServerResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // 添加评论
    @PostMapping
    public ServerResult<Integer> addComment(@RequestBody Comment comment) {
        int result = commentService.addComment(comment);
        if (result > 0) {
            messagingTemplate.convertAndSend("/topic/comments", comment);
            return ServerResult.success(result);
        } else {
            return ServerResult.error(500, "添加评论失败");
        }
    }

    // 删除评论
    @DeleteMapping("/{commentId}")
    public ServerResult<Integer> deleteComment(@PathVariable Integer commentId) {
        int result = commentService.deleteComment(commentId);
        if (result > 0) {
            // 可以在这里广播删除消息
            return ServerResult.success(result);
        } else {
            return ServerResult.error(500, "删除评论失败");
        }
    }

    // 修改评论
    @PutMapping
    public ServerResult<Integer> updateComment(@RequestBody Comment comment) {
        int result = commentService.updateComment(comment);
        if (result > 0) {
            // 可以在这里广播修改消息
            return ServerResult.success(result);
        } else {
            return ServerResult.error(500, "修改评论失败");
        }
    }

    // 根据 ID 查询评论
    @GetMapping("/{commentId}")
    public ServerResult<Comment> getCommentById(@PathVariable Integer commentId) {
        Comment comment = commentService.getCommentById(commentId);
        if (comment != null) {
            return ServerResult.success(comment);
        } else {
            return ServerResult.error(404, "评论不存在");
        }
    }

    // 查询某个帖子下的所有评论
    @GetMapping("/post/{postId}")
    public ServerResult<List<Comment>> getCommentsByPostId(@PathVariable Integer postId) {
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        return ServerResult.success(comments);
    }
}