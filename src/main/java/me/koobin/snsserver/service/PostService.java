package me.koobin.snsserver.service;

import java.util.List;
import me.koobin.snsserver.model.PatchPostInfo;
import me.koobin.snsserver.model.PostFileInfo;
import me.koobin.snsserver.model.user.User;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {

  Long post(User user, String content, List<MultipartFile> images);

  PostFileInfo getPost(Long postId);

  List<PostFileInfo> getMyPost(Long id);

  void patchPost(PatchPostInfo patchPostInfo);

  void deletePost(Long id, Long postId);
}
