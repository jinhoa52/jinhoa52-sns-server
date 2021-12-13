package me.koobin.snsserver.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntities {

  private ResponseEntities() {
    throw new IllegalStateException("Utility class");
  }

  public static final ResponseEntity<Void> OK = new ResponseEntity<>(HttpStatus.OK);
  public static final ResponseEntity<Void> CREATED = new ResponseEntity<>(
      HttpStatus.CREATED);
  public static final ResponseEntity<Void> CONFLICT = new ResponseEntity<>(
      HttpStatus.CONFLICT);
  public static final ResponseEntity<Void> UNAUTHORIZED = new ResponseEntity<>(
      HttpStatus.UNAUTHORIZED);

}
