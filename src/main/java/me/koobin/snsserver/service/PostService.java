package me.koobin.snsserver.service;

import java.util.List;
import me.koobin.snsserver.model.user.User;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {

  void post(User user, String content, List<MultipartFile> images);
}
