package com.levelupseon.workspaces_club.security.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Log4j2
@Getter
@Setter
@ToString
public class ClubAuthMemberDTO extends User implements OAuth2User {

  private String email;
  private String name;
  private boolean fromSocial;
  private Map<String, Object> attr;

  private String password;

  public ClubAuthMemberDTO(
          String username,
          String password,
          boolean fromSocial,
          Collection<? extends GrantedAuthority> authorities,
          Map<String, Object> attr
  ) {
    this(username, password, fromSocial, authorities);
    this.attr = attr;

    log.info("=== ClubAuthMemberDTO 생성자 ===");
    log.info("password: {}", password);
  }

  public ClubAuthMemberDTO(
          String username,
          String password,
          boolean fromSocial,
          Collection<? extends GrantedAuthority> authorities
                           ) {
    super(username, password, authorities);
    this.password = password;
    this.email = username;
    this.fromSocial = fromSocial;
  }

   @Override
  public Map<String, Object> getAttributes() {

    return attr == null ? Map.of() : attr;
  }

  @Override
  public String getName() {
    //소셜 정보가 DB에 저장 안되어 있을 때는 attr에서 가져오고
    //DB에 저장되어있으면 DB에서 가져온 값으로 사용
    return attr != null && attr.get("email") != null ? (String) attr.get("email") : email; // 또는 "sub"
  }

  @Override
  public String getPassword() {
    return password;
  }
}
