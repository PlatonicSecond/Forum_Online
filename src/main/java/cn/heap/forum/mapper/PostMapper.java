package cn.heap.forum.mapper;

import cn.heap.forum.pojo.Post;
import cn.heap.forum.pojo.PostDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostMapper extends BaseMapper<Post> {
    @Insert("insert into post(title,content,author_id,plate_id,create_time,img_path)"+
            "values(#{title},#{content},#{authorId},#{plateId},#{createTime},#{imgPath})")
    void add(PostDTO postDTO);


}
