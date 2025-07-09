package cn.heap.forum.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("comment")
public class Comment {
    @TableId(value = "comment_id", type = IdType.AUTO)
    private Integer commentId;
    private String content;
    private Integer authorId;
    private Integer userId;
    private Integer postId;
    @TableField("create_time")
    private Date createTime;
}