package com.levelupseon.workspaces_club.security.service;

import com.levelupseon.workspaces_club.security.dto.ClubAuthMemberDTO;
import com.levelupseon.workspaces_club.entity.ClubMember;
import com.levelupseon.workspaces_club.entity.ClubMemberRole;
import com.levelupseon.workspaces_club.repository.ClubMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ClubOAuthUserDetailsService extends DefaultOAuth2UserService {

  private final ClubMemberRepository repository;

  private final PasswordEncoder passwordEncoder;
  private final ClubMemberRepository clubMemberRepository;

  public OAuth2User loadUser(OAuth2UserRequest userRequest)
          throws OAuth2AuthenticationException {
    log.info("-----------------------------------");
    log.info("userRequest: {} ", userRequest);

    String clientName = userRequest.getClientRegistration().getClientName();

    log.info("clientName: {} ", clientName);
    log.info(userRequest.getAdditionalParameters());

    OAuth2User oAuth2User = super.loadUser(userRequest);

    log.info("=============================");
    oAuth2User.getAttributes().forEach((key, value) -> {
      log.info("key={}, value={}", key, value);
    });

    String email = null;

    if (clientName.equals("Google")) {
      email = oAuth2User.getAttributes().get("email").toString();
    }

    log.info("email: {}", email);

    ClubMember member = saveSocialMember(email);



//    return oAuth2User;

    ClubAuthMemberDTO clubAuthMember = new ClubAuthMemberDTO(
            member.getEmail(),
            member.getPassword(),
            true,
            member.getRoleSet().stream().map(
                    role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                            .collect(Collectors.toList()),
            oAuth2User.getAttributes()
            );

    clubAuthMember.setName(member.getName());
    log.info("clubAuthMember.password: {}", clubAuthMember.getPassword());

    return clubAuthMember;
  }

  private ClubMember saveSocialMember(String email){
    Optional<ClubMember> result = repository.findByEmail(email, true);

    if(result.isPresent()){
      log.info("--------------------------");
      log.info(result.get());
      log.info("--------------------------");
      return result.get();
    }

    ClubMember clubMember = ClubMember.builder()
            .email(email)
            .name(email)
            .password(passwordEncoder.encode("1111"))
            .fromSocial(true)
            .build();

    clubMember.addMemberRole(ClubMemberRole.USER);
    repository.save(clubMember);
    log.info("--------------------------");
    log.info(clubMember.toString());
    log.info("--------------------------");

    return clubMember;
  }
}
