package cn.heap.forum.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
public class PostResultDTO {
    @TableId("postId")
    private Integer postId;
    private String title;
    private String content;
    private Integer authorId;
    @TableField("createTime") // 映射数据库字段名
    private LocalDateTime createTime;
    private String imgPath;
    private Integer plateId;
    private Integer viewCount;
    private String username;
    @TableField("avatar_path")  // 指定数据库字段名
    private String avatarPath;

    // 无参构造方法（Jackson 反序列化需要）
    public PostResultDTO() {
    }

    // 带参数的构造方法（显式指定反序列化方式）
    @JsonCreator
    public PostResultDTO(
            @JsonProperty("postId") String postId,
            @JsonProperty("title") @NonNull String title,
            @JsonProperty("content") String content,
            @JsonProperty("authorId") Integer authorId,
            @JsonProperty("createTime") LocalDateTime createTime,
            @JsonProperty("imgPath") String imgPath,
            @JsonProperty("plateId") Integer plateId,
            @JsonProperty("viewCount") Integer viewCount,
            @JsonProperty("username") String username,
            @JsonProperty("avatarPath") String avatarPath
    ) {
        this.postId = plateId;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.createTime = createTime;
        this.imgPath = imgPath;
        this.plateId = plateId;
        this.viewCount = viewCount;
        this.username = username;
        this.avatarPath = avatarPath;
    }
}
