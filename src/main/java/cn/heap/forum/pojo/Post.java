package cn.heap.forum.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("post")
public class Post {
    @TableId(value = "post_id", type = IdType.AUTO)
    private Integer postId;
    private String title;
    private String content;
    private Integer userId;
    @TableField("create_time") // 映射数据库字段名
    private LocalDateTime createTime;
    @TableField("update_time")
    private LocalDateTime updateTime;
    private String imgPath;
    private Integer plateId;
    private Integer viewCount;
}
