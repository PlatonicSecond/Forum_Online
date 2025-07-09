package cn.heap.forum.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("plate")
public class Plate {
    @TableId("plate_id")
    private Integer plateId;
    @TableField("plate_name")
    private String name;
    @TableField("description")
    private String description;

}
