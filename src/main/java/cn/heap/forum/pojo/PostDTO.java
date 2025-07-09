package cn.heap.forum.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
public class PostDTO {
    @NonNull
    private String title;
    private String content;
    private Integer authorId;
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT")
    private LocalDateTime createTime;
    private String imgPath;  // 存储图片的URL路径
    private String imgBase64; // 接收前端上传的Base64编码图片
    private Integer plateId;
    private Integer viewCount;

    // 无参构造方法（Jackson 反序列化需要）
    public PostDTO() {
    }

    // 带参数的构造方法（显式指定反序列化方式）
    @JsonCreator
    public PostDTO(
            @JsonProperty("title") @NonNull String title,
            @JsonProperty("content") String content,
            @JsonProperty("authorId") Integer authorId,
            @JsonProperty("imgPath") String imgPath,
            @JsonProperty("imgBase64") String imgBase64,
            @JsonProperty("plateId") Integer plateId,
            @JsonProperty("viewCount") Integer viewCount
    ) {
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.imgPath = imgPath;
        this.imgBase64 = imgBase64;
        this.plateId = plateId;
        this.viewCount = viewCount;
    }

    // 获取图片的Base64编码
    public String getImgBase64() {
        return imgBase64;
    }

    // 设置图片的Base64编码
    public void setImgBase64(String imgBase64) {
        this.imgBase64 = imgBase64;
    }

    // 获取图片路径
    public String getImgPath() {
        return imgPath;
    }

    // 设置图片路径
    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}

