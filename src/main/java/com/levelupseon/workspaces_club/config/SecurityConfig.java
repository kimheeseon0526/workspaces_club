package com.levelupseon.workspaces_club.config;

import com.levelupseon.workspaces_club.security.filter.ApiCheckFilter;
import com.levelupseon.workspaces_club.security.filter.ApiLoginFilter;
import com.levelupseon.workspaces_club.security.handler.ClubLoginSuccessHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Log4j2
public class SecurityConfig {

  // spring3 이상부터 password encoding 필수
  @Bean
  public PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder(); // springboot가 기본제공하는 인코더
  }

  @Bean
  public ClubLoginSuccessHandler clubLoginSuccessHandler(){
    return new ClubLoginSuccessHandler(passwordEncoder());
  }

  @Bean
  public ApiCheckFilter apiCheckFilter(){
    //notes 한글자로도 있어야함
    return new ApiCheckFilter("/notes/**/*");
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

//  @Bean
//  public InMemoryUserDetailsManager inDetailService(){
//    UserDetails user = User.builder()
//            .username("user1")
//            .password(passwordEncoder().encode("1111"))
//            .roles("USER")
//            .build();
//    return new InMemoryUserDetailsManager(user);
//  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
    // spring security 6이상 버전에서 아래와 같이 변경됨(교재와 다르다)

    ApiLoginFilter apiLoginFilter = new ApiLoginFilter("/api/login");
    apiLoginFilter.setAuthenticationManager(authenticationManager);

    http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests((auth) -> {
              auth.requestMatchers("/sample/all").permitAll() //permitAll:로그인안해도 보임
                      .requestMatchers("/notes/**").permitAll()
                      .requestMatchers("/sample/member").hasRole("USER")
                      .requestMatchers("/sample/admin").hasRole("ADMIN")
                      .requestMatchers("/member/modify", "/member/modify/**").hasRole("USER")
                      .requestMatchers("/error").permitAll();
              ;
            })
            // permitAll로 되어있지 않은 모든 페이지는 권한 없는 페이지!!
            // formLogin 로그인 화면 구성시 권한없는페이지 대신 로그인페이지로 보낸다.
            // 로그인 성공시 리다이렉트 지정.
            // false → 직접 접근한 URL이 있으면 거기로 먼저 보내주고, 없을 때만 "/sample/all"로 이동.
            // true → 로그인 성공시 무조건 페이지 이동
            .formLogin(form ->
                    form.defaultSuccessUrl("/sample/all", false))
            .logout(Customizer.withDefaults())
            .oauth2Login(form ->
                    form.defaultSuccessUrl("/sample/all", false)
                            .successHandler(clubLoginSuccessHandler())
            )
            .rememberMe(token
                    -> token.tokenValiditySeconds(60 * 2)) // (60 * 60 * 24) 1일간 유효
//                    .userDetailsService(ClubUserDetailsService)
            .addFilterBefore(apiCheckFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(apiLoginFilter, UsernamePasswordAuthenticationFilter.class)
    ;
    return http.build();
  }




}