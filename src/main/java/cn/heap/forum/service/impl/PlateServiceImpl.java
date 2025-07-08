package cn.heap.forum.service.impl;

import cn.heap.forum.mapper.PlateMapper;
import cn.heap.forum.pojo.Plate;
import cn.heap.forum.pojo.Post;
import cn.heap.forum.service.PlateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlateServiceImpl implements PlateService {
    @Autowired
    private PlateMapper plateMapper;

    @Override
    public List<Plate> selectAll() {
        return plateMapper.selectList(null);
    }

    @Override
    public List<Post> search(int plateId) {
        return plateMapper.search(plateId);
    }

    @Override
    public List<Post> authorsearch(int userId) {
        return plateMapper.authorsearch(userId);
    }

    @Override
    public List<Post> commentsearch(int commentId, int authorId) {
        return plateMapper.commentsearch(commentId,authorId);
    }
}
