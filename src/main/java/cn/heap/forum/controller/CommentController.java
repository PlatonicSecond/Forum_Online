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
            // 广播新评论
            messagingTemplate.convertAndSend("/topic/comments", comment);
            return ServerResult.success(result);
        } else {
            return ServerResult.error(500, "添加评论失败");
        }
    }

    // 查询某个帖子下的所有评论
    @GetMapping("/post/{postId}")
    public ServerResult<List<Comment>> getCommentsByPostId(@PathVariable Integer postId) {
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        return ServerResult.success(comments);
    }

}