package cn.heap.forum.service;

import cn.heap.forum.pojo.PlatePostDTO;
import cn.heap.forum.pojo.Post;
import cn.heap.forum.pojo.PostDTO;

import java.util.List;

public interface PostService {

    List<PlatePostDTO> select(int id);

    void add(PostDTO postDTO);

    void delete(int id);
}
