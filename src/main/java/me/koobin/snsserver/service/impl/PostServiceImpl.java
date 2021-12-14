package me.koobin.snsserver.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.koobin.snsserver.mapper.PostMapper;
import me.koobin.snsserver.model.PostInfo;
import me.koobin.snsserver.model.user.User;
import me.koobin.snsserver.service.FileInfoService;
import me.koobin.snsserver.service.PostService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

  private final PostMapper postMapper;
  private final FileInfoService fileInfoService;

  @Override
  public void post(User user, String content, List<MultipartFile> images) {

    // 파일 업로드
    // 파일 저장
    List<Long> fileIds = fileInfoService.saveFiles(images);

// 포스트 저장
    PostInfo postInfo = PostInfo.builder()
        .userId(user.getId())
        .content(content)
        .build();
    postMapper.post(postInfo);
    // 포스트 이미지 등록
    postMapper.savePostImage(postInfo.getId(), fileIds);



  }
}
