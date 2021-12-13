package me.koobin.snsserver.advice;

import me.koobin.snsserver.exception.LoginException;
import me.koobin.snsserver.util.ResponsesEntities;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class LoginErrorHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(LoginException.class)
  public final ResponseEntity<Void> userFoundException (){
    return ResponsesEntities.UNAUTHORIZED;

  }

}
