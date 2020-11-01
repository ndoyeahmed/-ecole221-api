package com.ecole.school.services.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class Utils {
  public String generateUniqueId() throws UnsupportedEncodingException, NoSuchAlgorithmException {
    MessageDigest salt = MessageDigest.getInstance("SHA-256");
    salt.update(UUID.randomUUID().toString().getBytes("UTF-8"));
    String digest = bytesToHex(salt.digest());
    return digest;
  }

  public String bytesToHex(byte[] bytes) {
    StringBuilder builder = new StringBuilder();
    for (byte b : bytes) {
      builder.append(String.format("%02x", b));
    }
    return builder.toString();
  }

  public String createCode(String libelle) {

    return libelle.trim().substring(0, 3);
  }
}
