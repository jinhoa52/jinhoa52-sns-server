package me.koobin.snsserver.aspect;

import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import me.koobin.snsserver.util.SessionKey;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

@Aspect
@Component
@RequiredArgsConstructor
public class CheckLoginAspect {

  private final HttpSession httpSession;

  @Before("@annotation(me.koobin.snsserver.annotation.CheckLogin)")
  public void checkLogin() throws HttpClientErrorException {

    if (httpSession.getAttribute(SessionKey.USER) == null) {
      throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
    }
  }
}
