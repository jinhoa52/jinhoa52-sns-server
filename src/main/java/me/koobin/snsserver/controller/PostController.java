package me.koobin.snsserver.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.koobin.snsserver.annotation.CheckLogin;
import me.koobin.snsserver.annotation.CurrentUser;
import me.koobin.snsserver.model.PostFileInfo;
import me.koobin.snsserver.model.user.User;
import me.koobin.snsserver.service.PostService;
import me.koobin.snsserver.util.ResponseEntities;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

  private final PostService postService;

  @PostMapping
  @CheckLogin
  public ResponseEntity<Void> post(@CurrentUser User user, @RequestParam String content,
      @RequestPart("image") List<MultipartFile> images){
    postService.post(user, content, images);
    return ResponseEntities.CREATED;
  }

  @GetMapping("/{postId}")
  @CheckLogin
  public ResponseEntity<PostFileInfo> getPost(@PathVariable Long postId){
    PostFileInfo postFileInfo = postService.getPost(postId);
    return ResponseEntity.ok(postFileInfo);
  }

}
