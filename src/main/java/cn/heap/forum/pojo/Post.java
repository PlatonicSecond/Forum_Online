package cn.heap.forum.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("Post")
public class Post {
    @TableId("postId")
    private Integer postId;
    private String content;
    private Integer authorId;
    @TableField("createTime") // 映射数据库字段名
    private LocalDateTime createTime;
    @TableField("updateTime")
    private LocalDateTime updateTime;
    private String imgPath;
    private Integer plateId;
    private Integer viewcount;
}
