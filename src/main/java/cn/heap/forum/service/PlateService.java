package cn.heap.forum.service;

import cn.heap.forum.pojo.Plate;
import cn.heap.forum.pojo.Post;

import java.util.List;

public interface PlateService {
    List<Plate> selectAll();

    List<Post> search(int plateId);

    List<Post> authorsearch(int userId);

    List<Post> commentsearch(int authorId);
}
