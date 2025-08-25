package com.levelupseon.workspaces_club.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

//회원이 메모장 용도로 사용
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Service
public class Note extends BaseEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long num;

  private String title;
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  private ClubMember writer;

  public void changeTitle(String title) {
    this.title = title;
  }

  public void changeContent(String content) {
    this.content = content;
  }

}
