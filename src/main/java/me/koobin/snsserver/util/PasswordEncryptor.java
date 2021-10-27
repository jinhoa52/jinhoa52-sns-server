package me.koobin.snsserver.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncryptor {

  private PasswordEncryptor() {
    throw new IllegalStateException("Utility class");
  }

  public static String encrypt(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt());
  }

  public static boolean isMatch(String password, String hashedPassword) {
    return BCrypt.checkpw(password, hashedPassword);
  }
}
