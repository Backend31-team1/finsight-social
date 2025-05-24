/**
 *
 * jwt secret 키 생성용 프로덕션 클래스.
 * 사실 제거 해도 무방하지만 일단 공유합니다.
 * application-secret.yml에 값을 넣어주고 application.yml에서 가져와서 사용하는 방식입니다!

package com.project.common.util;

import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

public class GenerateSecretKey {

  public static void main(String[] args) {
    SecretKey key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
    String base64Key = Encoders.BASE64.encode(key.getEncoded());
    System.out.println("Base64 Secret Key: " + base64Key);
  }
}
*/
