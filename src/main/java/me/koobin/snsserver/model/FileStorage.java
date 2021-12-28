package me.koobin.snsserver.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum FileStorage implements CodeEnum {
  LOCAL("LOCAL"), S3("S3");
  private final String code;

  @Override
  public String getCode() {
    return code;
  }
}
