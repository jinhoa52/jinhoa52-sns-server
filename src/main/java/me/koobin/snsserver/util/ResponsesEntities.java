package me.koobin.snsserver.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponsesEntities {

  private ResponsesEntities() {
    throw new IllegalStateException("Utility class");
  }

  public static final ResponseEntity<Void> RESPONSE_OK = new ResponseEntity<>(HttpStatus.OK);
  public static final ResponseEntity<Void> RESPONSE_CREATED = new ResponseEntity<>(
      HttpStatus.CREATED);
  public static final ResponseEntity<Void> RESPONSE_CONFLICT = new ResponseEntity<>(
      HttpStatus.CONFLICT);
  public static final ResponseEntity<Void> RESPONSE_UNAUTHORIZED = new ResponseEntity<>(
      HttpStatus.UNAUTHORIZED);

}
