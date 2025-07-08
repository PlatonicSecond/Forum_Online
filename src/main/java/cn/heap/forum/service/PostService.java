package cn.heap.forum.service;

import cn.heap.forum.pojo.Post;
import cn.heap.forum.pojo.PostDTO;

import java.util.List;

public interface PostService {
    List<Post> selectAll(int id);

    void add(PostDTO postDTO);
}
