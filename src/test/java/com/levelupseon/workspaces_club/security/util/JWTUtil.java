package com.levelupseon.workspaces_club.security.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;

import java.time.ZonedDateTime;
import java.util.Date;

@Log4j2
public class JWTUtil {
  private String secretKey = "club12345678";

  //1month
  private long expire = 60 * 24 * 30;

  public String generateToken(String content) throws Exception {
    return Jwts.builder()
            .issuedAt(new Date())
            .expiration(Date.from(ZonedDateTime.now().
            plusMinutes(expire).toInstant()))
            .claim("sub", content)
            .signWith(SignatureAlgorithm.HS256, secretKey.getBytes("UTF-8")).compact();
  }
//  public String validateAndExtract(String tokenStr) throws Exception {
//    String contentValue = null;

//    try{
//      DefaultJws defaultJws = (DefaultJws) Jwts.parser()
//              .setSigningKey(secretKey.getBytes("UTF-8")).parseClaimsJws(tokenStr);
//
//      log.info(defaultJws);
//      log.info(defaultJws.getBody().getClass());
//    }
//  }
}
