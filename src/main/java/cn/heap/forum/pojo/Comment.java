package cn.heap.forum.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Comment {
    private static final long serialVersionUID = 1L;
    private Integer commentId;
    private String content;
    private Integer authorId;
    private Integer replyToUserId;
    private Integer postId;
    private Date createTime;
}