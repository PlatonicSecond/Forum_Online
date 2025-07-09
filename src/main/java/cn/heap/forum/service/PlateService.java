package cn.heap.forum.service;

import cn.heap.forum.pojo.Plate;
import cn.heap.forum.pojo.PlatePostDTO;

import java.util.List;

public interface PlateService {
    List<Plate> selectAll();

    List<PlatePostDTO> search(int plateId);

    List<PlatePostDTO> authorsearch(int userId);

    List<PlatePostDTO> commentsearch(int authorId);
}
