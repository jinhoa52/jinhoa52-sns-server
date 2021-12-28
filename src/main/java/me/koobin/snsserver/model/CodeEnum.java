package me.koobin.snsserver.model;

import com.fasterxml.jackson.annotation.JsonValue;

public interface CodeEnum {
  @JsonValue
  String getCode();
}
