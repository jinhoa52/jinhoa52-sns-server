package me.koobin.snsserver.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import me.koobin.snsserver.annotation.CurrentUser;
import me.koobin.snsserver.model.User;
import me.koobin.snsserver.util.SessionKey;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(CurrentUser.class);
  }

  @Override
  public User resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
    HttpServletRequest servletRequest = (HttpServletRequest) webRequest.getNativeRequest();
    HttpSession httpSession = servletRequest.getSession();

    return (User) httpSession.getAttribute(SessionKey.USER);
  }
}
