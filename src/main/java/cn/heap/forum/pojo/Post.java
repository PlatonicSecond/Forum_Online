package cn.heap.forum.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("Post")
public class Post {
    private Integer postId;
    private String content;
    private Integer authorId;
    private Date createTime;
    private String postImgpath;
    private Integer plateId;

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", content='" + content + '\'' +
                ", authorId=" + authorId +
                ", createTime=" + createTime +
                ", postImgpath='" + postImgpath + '\'' +
                ", plateId=" + plateId +
                '}';
    }
}
