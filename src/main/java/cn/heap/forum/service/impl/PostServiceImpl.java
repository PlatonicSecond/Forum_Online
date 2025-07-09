package cn.heap.forum.service.impl;

import cn.heap.forum.mapper.PostMapper;
import cn.heap.forum.pojo.Post;
import cn.heap.forum.pojo.PostDTO;
import cn.heap.forum.service.PostService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostMapper postMapper;

    @Override
    public List<Post> selectAll(int id){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("post_id", id);
        return postMapper.selectList(queryWrapper);
    }

    @Override
    public void add(PostDTO postDTO) {
        postMapper.add(postDTO);
    }

    @Override
    public void delete(int id) {
        postMapper.delete(id);
    }
}
