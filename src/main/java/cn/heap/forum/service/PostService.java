package cn.heap.forum.service;

import cn.heap.forum.pojo.PostDTO;
import cn.heap.forum.pojo.PostResultDTO;

import java.util.List;

public interface PostService {

    List<PostResultDTO> select(int id);

    void add(PostDTO postDTO);

    void delete(int id);

}
