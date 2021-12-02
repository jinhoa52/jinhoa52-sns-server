package me.koobin.snsserver.exception;

public class FileIoException extends RuntimeException{

  public FileIoException() {
    super();
  }

  public FileIoException(String message) {
    super(message);
  }
}
