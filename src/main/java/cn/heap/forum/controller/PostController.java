package cn.heap.forum.controller;

import cn.heap.forum.pojo.Post;
import cn.heap.forum.pojo.PostDTO;
import cn.heap.forum.service.PostService;
import cn.heap.forum.util.ServerResult;
import cn.heap.forum.util.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

import java.time.LocalDateTime;
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

    @PostMapping("/add")
    @ApiOperation(value = "创建帖子", notes = "用户创建新帖子")
    public ServerResult<Void> add(@RequestBody PostDTO post) {
        try {
            // 从ThreadLocal获取当前登录用户信息
            Integer currentUserId = UserContext.getCurrentUserId();

            if (currentUserId == null) {
                return ServerResult.error(401, "用户未登录");
            }

            // 设置帖子的创建信息
            post.setAuthorId(currentUserId);
            post.setViewCount(0);
            post.setCreateTime(LocalDateTime.now());

            // 处理图片
            if (post.getImgBase64() != null && !post.getImgBase64().isEmpty()) {
                // 解码Base64图片字符串
                byte[] imageBytes = Base64Utils.decodeFromString(post.getImgBase64());

                // 生成唯一的文件名
                String fileName = UUID.randomUUID().toString() + ".jpg";
                String filePath = "src/main/resources/static/img/" + fileName;

                // 保存图片到指定目录
                File imageFile = new File(filePath);
                FileUtils.writeByteArrayToFile(imageFile, imageBytes);

                // 拼接图片的URL
                String imageUrl = "/img/" + fileName;
                post.setImgPath(imageUrl);
            }

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
}
