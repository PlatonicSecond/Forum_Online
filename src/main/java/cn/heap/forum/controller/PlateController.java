package cn.heap.forum.controller;


import cn.heap.forum.pojo.Plate;
import cn.heap.forum.service.PlateService;
import cn.heap.forum.util.ServerResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/plate")
@Api("plate管理类测试文档")
public class PlateController {
    @Autowired
    private PlateService plateService;

    @GetMapping("/select")
    @ApiParam("获取所有plate")
    public ServerResult<List<Plate>> selectAll() {
        return ServerResult.success(plateService.selectAll());
    }
}
