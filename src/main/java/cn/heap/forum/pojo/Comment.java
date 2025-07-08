package cn.heap.forum.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("comment")
public class Comment {
    @TableId("commentId")
    private Integer commentId;
    private String content;
    private Integer authorId;
    private Integer replyToUserId;
    private Integer postId;
    @TableField("createTime")
    private Date createTime;
}