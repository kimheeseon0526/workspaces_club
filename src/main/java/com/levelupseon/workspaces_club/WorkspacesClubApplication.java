package com.levelupseon.workspaces_club;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class WorkspacesClubApplication {

  public static void main(String[] args) {

    SpringApplication.run(WorkspacesClubApplication.class, args);
  }

}
