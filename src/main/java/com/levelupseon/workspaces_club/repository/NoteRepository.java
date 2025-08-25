package com.levelupseon.workspaces_club.repository;

import com.levelupseon.workspaces_club.entity.Note;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {

  @EntityGraph(attributePaths = "writer", type = EntityGraph.EntityGraphType.LOAD)
  //EntityGraph : 쿼리 한 번에 조인해서 가져옴
  @Query("select n from Note n where n.num = :num")
  Optional<Note> getWithWriter(Long num);

  @EntityGraph(attributePaths = {"writer"}, type = EntityGraph.EntityGraphType.LOAD)
  @Query("select n from Note n where n.writer.email = :email")
  List<Note> getList(String email);
}
