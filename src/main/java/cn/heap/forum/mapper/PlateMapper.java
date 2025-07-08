package cn.heap.forum.mapper;

import cn.heap.forum.pojo.Plate;
import cn.heap.forum.pojo.Post;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Mapper
public interface PlateMapper extends BaseMapper<Plate> {

    @Select("select * from post where plateId=#{plateId}")
    @ResultMap("postResultMap")
    List<Post> search(int plateId);

    @Select("select * from post where plateId=#{authorId}")
    @ResultMap("postResultMap")
    List<Post> authorsearch(int authorId);

    @Select("SELECT p.* " +
            "FROM post p " +
            "JOIN comment c ON p.postId = c.postId " +
            "WHERE c.commentId = #{commentId} and c.userId = #{authorId}")
    @ResultMap("postResultMap")
    List<Post> commentsearch(int commentId,int authorId);
}
