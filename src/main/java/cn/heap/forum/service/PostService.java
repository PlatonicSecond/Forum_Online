package cn.heap.forum.service;

import cn.heap.forum.pojo.Post;
import cn.heap.forum.pojo.PostDTO;
import cn.heap.forum.pojo.PostResultDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {

    List<PostResultDTO> select(int id);

    void add(PostDTO postDTO);

    void delete(int id);

    String storeFile(MultipartFile file);

    void update(Post post);
}
