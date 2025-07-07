package cn.heap.forum.controller;

import cn.heap.forum.pojo.Post;
import cn.heap.forum.service.PostService;
import cn.heap.forum.util.ServerResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/post")
@Api("Post控制类接口文档")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping("/select")
    @ApiParam("寻找plateId对应的post")
    public ServerResult<List<Post>> selectAll(int id){
        return ServerResult.success(postService.selectAll(id));
    }
}
