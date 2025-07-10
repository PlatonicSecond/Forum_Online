package cn.heap.forum.service.impl;

import cn.heap.forum.mapper.PostMapper;
import cn.heap.forum.pojo.PostDTO;
import cn.heap.forum.pojo.PostResultDTO;
import cn.heap.forum.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostMapper postMapper;

    @Value("${file.upload-dir}")
    private String uploadDir; // 从配置文件注入路径

    @Override
    public List<PostResultDTO> select(int id){
        return postMapper.select(id);
    }

    @Override
    public void add(PostDTO postDTO) {
        postMapper.add(postDTO);
    }

    @Override
    public void delete(int id) {
        postMapper.delete(id);
    }

    @Override
    public String storeFile(MultipartFile file) {
        // 生成唯一的文件名
        String fileName = UUID.randomUUID().toString() + ".png";

        try {
            // 确保目录存在
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // 创建目标文件路径
            Path targetLocation = Paths.get(uploadDir).resolve(fileName);
            file.transferTo(targetLocation.toFile());

            // 返回可用于访问的相对路径或完整文件名
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not store file " + fileName, e);
        }
    }
}
