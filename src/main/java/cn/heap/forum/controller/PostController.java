package cn.heap.forum.controller;

import cn.heap.forum.pojo.PlatePostDTO;
import cn.heap.forum.pojo.Post;
import cn.heap.forum.pojo.PostDTO;
import cn.heap.forum.pojo.PostResultDTO;
import cn.heap.forum.service.PostService;
import cn.heap.forum.util.ServerResult;
import cn.heap.forum.util.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

import java.util.List;

@RestController
@RequestMapping("/post")
@Api("Post控制类接口文档")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping("/select")
    @ApiParam("根据id找post")
    public ServerResult<List<PostResultDTO>> select(int id){
        return ServerResult.success(postService.select(id));
    }

    @PostMapping(value = "/add", consumes = "multipart/form-data")
    @ApiOperation(value = "创建帖子", notes = "用户创建新帖子")
    public ServerResult<Void> add(    @RequestParam("title") String title,
                                      @RequestParam("content") String content,
                                      @RequestParam("file") MultipartFile file) {
        try {
            PostDTO post=new PostDTO();
            post.setTitle(title);
            post.setContent(content);
            // 从ThreadLocal获取当前登录用户信息
            Integer currentUserId = UserContext.getCurrentUserId();
            Integer roleId= UserContext.getCurrentUserRoleId();

            if (currentUserId == null) {
                return ServerResult.error(401, "用户未登录");
            }

            String fileName = postService.storeFile(file);

            // 设置帖子的创建信息
            post.setAuthorId(currentUserId);
            post.setViewCount(0);
            post.setCreateTime(LocalDateTime.now());
            if(roleId == 1)
                post.setPlateId(1);
            else
                post.setPlateId(2);

            post.setImgPath(fileName);

            postService.add(post);
            return ServerResult.success();

        } catch (Exception e) {
            e.printStackTrace();
            return ServerResult.error(500, "创建帖子失败:" + e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    @ApiParam(value = "删除帖子")
    public ServerResult<Void> delete(int id) {
        try {
            postService.delete(id);
            return ServerResult.success();
        } catch (Exception e) {
            return ServerResult.error(501, e.getMessage());
        }
    }

    @PostMapping(value = "/update", consumes = "multipart/form-data")
    @ApiOperation(value = "更新帖子", notes = "用户更新自己的帖子")
    public ServerResult<Void> update(
            @RequestParam("id") Integer id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "file", required = false) MultipartFile file) { // 1. file 设置为可选

        try {
            // 2. 验证用户登录状态
            Integer currentUserId = UserContext.getCurrentUserId();
            if (currentUserId == null) {
                return ServerResult.error(401, "用户未登录");
            }

            // 3. 从数据库获取要更新的原始帖子
            Post post = new Post();

            post.setPostId(id);

            List<PostResultDTO> postResultDTOS = postService.select(id);
            if (postResultDTOS.isEmpty()) {
                return ServerResult.error(404, "帖子不存在");
            }

            post.setTitle(title);
            post.setContent(content);

            // 6. 如果用户上传了新文件，则处理新文件
            if (file != null && !file.isEmpty()) {
                // a. 存储新文件
                String newFileName = postService.storeFile(file);

                // c. 更新帖子记录中的图片路径
                post.setImgPath(newFileName);
            }
            else{
                post.setImgPath(postResultDTOS.get(0).getImgPath());
            }

            post.setUpdateTime(LocalDateTime.now());

            // 8. 调用Service层执行更新操作
            postService.update(post); // 假设你的Service有update方法

            return ServerResult.success();

        } catch (Exception e) {
            e.printStackTrace();
            return ServerResult.error(500, "更新帖子失败:" + e.getMessage());
        }
    }

}
