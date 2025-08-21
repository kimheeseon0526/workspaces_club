package com.levelupseon.workspaces_club.security;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@Log4j2
public class PasswordTest {
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Test
  public void testEncoder() {
    String password = "1111";
    String enPw = passwordEncoder.encode(password);
    log.info(enPw);

    boolean matchResult = passwordEncoder.matches(password, enPw);
    log.info(matchResult);
  }
}
