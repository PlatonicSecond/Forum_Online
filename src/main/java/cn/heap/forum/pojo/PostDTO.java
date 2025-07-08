package cn.heap.forum.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostDTO {
    private String content;
    private Integer authorId;
    @TableField("createTime") // 映射数据库字段名
    private LocalDateTime createTime;
    private String postImgpath;
    private Integer plateId;
    private Integer viewcount;
}
