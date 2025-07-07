package cn.heap.forum.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("Plate")
public class Plate {
    private Integer plateId;
    private String name;
    private String description;

    @Override
    public String toString() {
        return "Plate{" +
                "plateId=" + plateId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
