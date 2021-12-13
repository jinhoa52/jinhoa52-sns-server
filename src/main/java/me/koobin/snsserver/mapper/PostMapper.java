package me.koobin.snsserver.mapper;

import java.util.List;
import me.koobin.snsserver.model.PostInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostMapper {

  void post(PostInfo postInfo);

  void savePostImage(Long postId, List<Long> fileIds);
}
