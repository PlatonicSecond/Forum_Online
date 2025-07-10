package cn.heap.forum.mapper;

import cn.heap.forum.pojo.PlatePostDTO;
import cn.heap.forum.pojo.Post;
import cn.heap.forum.pojo.PostDTO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PostMapper extends BaseMapper<Post> {
    @Insert("insert into post(title,content,author_id,plate_id,create_time,img_path)"+
            "values(#{title},#{content},#{authorId},#{plateId},#{createTime},#{imgPath})")
    void add(PostDTO postDTO);

    @Delete("delete from post where post_id=#{id}")
    void delete(int id);

    @Select("select p.*, u.username, u.avatar_path from post p join user u on u.user_id = p.author_id where post_id=#{id}")
    List<PlatePostDTO> select(int id);
}
