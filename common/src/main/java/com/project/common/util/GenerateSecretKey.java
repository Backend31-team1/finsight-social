
package com.project.common.util;

import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

public class GenerateSecretKey {

  public static void main(String[] args) {
    SecretKey key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS512);
    String base64Key = Encoders.BASE64.encode(key.getEncoded());
    System.out.println("Base64 Secret Key: " + base64Key);
  }
}

