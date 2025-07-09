package cn.heap.forum.mapper;

import cn.heap.forum.pojo.Plate;
import cn.heap.forum.pojo.Post;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PlateMapper extends BaseMapper<Plate> {

    @Select("select * from post where plate_id=#{plateId}")
    List<Post> search(int plateId);

    @Select("select * from post where plate_id=#{authorId}")
    List<Post> authorsearch(int authorId);

    @Select("SELECT p.* " +
            "FROM post p " +
            "JOIN comment c ON p.post_id = c.post_id " +
            "WHERE c.user_id = #{authorId}")
    List<Post> commentsearch(int authorId);
}
