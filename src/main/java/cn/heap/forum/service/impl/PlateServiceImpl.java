package cn.heap.forum.service.impl;

import cn.heap.forum.mapper.PlateMapper;
import cn.heap.forum.pojo.Plate;
import cn.heap.forum.service.PlateService;
import io.swagger.annotations.Api;
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
}
