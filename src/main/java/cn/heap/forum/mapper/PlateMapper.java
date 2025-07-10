package cn.heap.forum.mapper;

import cn.heap.forum.pojo.Plate;
import cn.heap.forum.pojo.PlatePostDTO;
import cn.heap.forum.pojo.Post;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PlateMapper extends BaseMapper<Plate> {

    @Select("select p.*, u.username, u.avatar_path from post p join user u on u.user_id = p.author_id where plate_id=#{plateId}")
    List<PlatePostDTO> search(int plateId);

    @Select("select p.*, u.username, u.avatar_path from post p join user u on u.user_id = p.author_id where plate_id=#{authorId}")
    List<PlatePostDTO> authorsearch(int authorId);

    @Select("SELECT p.*, u.username, u.avatar_path " +
            "FROM post p " +
            "JOIN comment c ON p.post_id = c.post_id " +
            "JOIN user u ON u.user_id = p.author_id " +
            "WHERE c.author_id = #{authorId} " +
            "GROUP BY p.post_id")
    List<PlatePostDTO> commentsearch(int authorId);

}
