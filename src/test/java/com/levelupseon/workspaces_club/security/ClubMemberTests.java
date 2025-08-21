package com.levelupseon.workspaces_club.security;

import com.levelupseon.workspaces_club.entity.ClubMember;
import com.levelupseon.workspaces_club.entity.ClubMemberRole;
import com.levelupseon.workspaces_club.repository.ClubMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.IntStream;

@SpringBootTest
public class ClubMemberTests {

  @Autowired
  private ClubMemberRepository repository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Test
  public void insertDummies() {
    //1 - 80까지는 USER만 저장
    //81 - 90까지는 USER, MANAGER
    //91 - 100까지는 USER, MANAGER,ADMIN

    IntStream.range(0, 100).forEach(i -> {
      ClubMember clubMember = ClubMember.builder()
              .email("user" + i + "@levelupseon.com")
              .name("사용자" + i)
              .fromSocial(false)
              .password(passwordEncoder.encode("1111"))
              .build();

      //default role
      clubMember.addMemberRole(ClubMemberRole.USER);
      if(i > 80) {
        clubMember.addMemberRole(ClubMemberRole.MANAGER);
      }
      if(i > 91) {
        clubMember.addMemberRole(ClubMemberRole.ADMIN);
      }
      repository.save(clubMember);
    });
  }
}
