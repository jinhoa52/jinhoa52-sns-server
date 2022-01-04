package me.koobin.snsserver.controller;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.koobin.snsserver.annotation.CheckLogin;
import me.koobin.snsserver.annotation.CurrentUser;
import me.koobin.snsserver.model.PatchPostInfo;
import me.koobin.snsserver.model.PatchPostParam;
import me.koobin.snsserver.model.PostFileInfo;
import me.koobin.snsserver.model.user.User;
import me.koobin.snsserver.service.PostService;
import me.koobin.snsserver.util.ResponseEntities;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
  public ResponseEntity<PostFileInfo> post(@CurrentUser User user, @RequestParam String content,
      @RequestPart("image") List<MultipartFile> images) {
    Long postId = postService.post(user, content, images);

    // (Header) "Location"에 상세정보를 볼 수 있는 "URI"를 URL 추가
    // 불필요한 네트워크 트래픽도 감소
    return ResponseEntity.created(URI.create(String.format("/posts/%d", postId))).build();
  }

  @GetMapping("/{postId}")
  @CheckLogin
  public ResponseEntity<PostFileInfo> getPost(@PathVariable Long postId) {
    PostFileInfo postFileInfo = postService.getPost(postId);
    return ResponseEntity.ok(postFileInfo);
  }

  @GetMapping("/my")
  @CheckLogin
  public ResponseEntity<List<PostFileInfo>> getMyPost(@CurrentUser User user) {
    List<PostFileInfo> postFileInfo = postService.getMyPost(user.getId());
    return ResponseEntity.ok(postFileInfo);
  }

  @PatchMapping
  @CheckLogin
  public ResponseEntity<Object> updatePost(@CurrentUser User user
      , @RequestPart PatchPostParam patchPostParam
      , @RequestPart(required = false) List<MultipartFile> images) {
    PatchPostInfo patchPostInfo = PatchPostInfo.builder()
        .userId(user.getId())
        .postId(patchPostParam.getPostId())
        .content(patchPostParam.getContent())
        .files(images)
        .fileIds(patchPostParam.getFileIds())
        .build();
    try {
      postService.patchPost(patchPostInfo);
    }catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }

    return ResponseEntity.ok(postService.getPost(patchPostParam.getPostId()));
  }

  @DeleteMapping("/{postId}")
  @CheckLogin
  public ResponseEntity<Void> deletePost(@CurrentUser User user, @PathVariable Long postId) {
    postService.deletePost(user.getId(), postId);
    return ResponseEntities.OK;
  }
}
