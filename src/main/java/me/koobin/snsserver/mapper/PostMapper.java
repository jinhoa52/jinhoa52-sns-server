package me.koobin.snsserver.mapper;

import java.util.List;
import me.koobin.snsserver.model.PatchPostInfo;
import me.koobin.snsserver.model.PostInfo;
import me.koobin.snsserver.model.PostFileInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostMapper {

  void post(PostInfo postInfo);

  void savePostImage(Long postId, List<Long> fileIds);

  PostFileInfo getPost(Long postId);

  List<PostFileInfo> findByUserId(Long userId);

  boolean sameAuthor(Long userId, Long postId);

  void update(PatchPostInfo patchPostInfo);

  void deletePostFile(Long postId, List<Long> fileIds);

  void deleteFileOfPost(Long postId);

  void deletePostFileBy(Long postId);

  void deletePostBy(Long postId);
}
