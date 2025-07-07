package cn.heap.forum.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("Plate")
public class Plate {
    @TableId("plateId")
    private Integer plateId;
    @TableField("name")
    private String name;
    @TableField("description")
    private String description;

}
