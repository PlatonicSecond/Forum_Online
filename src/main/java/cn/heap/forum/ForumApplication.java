package cn.heap.forum;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.heap.forum.mapper")
public class ForumApplication {
    public static void main(String[] args) {

        SpringApplication.run(ForumApplication.class, args);
    }
}
