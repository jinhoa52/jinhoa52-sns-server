package me.koobin.snsserver.resolver;

import lombok.RequiredArgsConstructor;
import me.koobin.snsserver.annotation.CurrentUser;
import me.koobin.snsserver.mapper.UserMapper;
import me.koobin.snsserver.service.LoginService;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

  private final UserMapper userMapper;

  private final LoginService loginService;

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(CurrentUser.class);
  }

  @Override
  public Object resolveArgument(MethodParameter methodParameter,
      ModelAndViewContainer modelAndViewContainer,
      NativeWebRequest nativeWebRequest,
      WebDataBinderFactory webDataBinderFactory) {

    Long userId = loginService.getCurrentUserId();
    return userMapper.findByUserId(userId);
  }
}
