package com.levelupseon.workspaces_club.controller;

import com.levelupseon.workspaces_club.dto.NoteDTO;
import com.levelupseon.workspaces_club.entity.Note;
import com.levelupseon.workspaces_club.service.NoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@RequestMapping("/notes/")
@RequiredArgsConstructor
public class NoteController {
  private final NoteService noteService;

  @PostMapping(value = "")
  public ResponseEntity<Long> register(@RequestBody NoteDTO noteDTO) {
    log.info("=============");
    log.info(noteDTO);

    Long num = noteService.register(noteDTO);

    return new ResponseEntity<>(num, HttpStatus.OK);
  }

  //특정한 번호의 Note 확인하는 방법
  @GetMapping(value = "/{num}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<NoteDTO> read(@PathVariable("num") Long num) {
    log.info("=============");
    log.info(num);

    return new ResponseEntity<>(noteService.get(num), HttpStatus.OK);
  }

  //http://localhost:8080/notes/all?email=user91@levelupseon.com
  //특정 이메일로 등록한 리스트를 다 가져옴
  @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<NoteDTO>> getList(String email) {
    //NoteDTO 리스트를 JSON으로 내려주면서, HTTP 상태 코드도 함께 제어하는 응답
    log.info("=============");
    log.info(email);

    return new ResponseEntity<>(noteService.getAllWithWriter(email), HttpStatus.OK);
  }

  @PutMapping(value = "/{num}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> modify(@RequestBody NoteDTO noteDTO /*@PathVariable("num") Long num*/) {
    log.info("========");
    log.info(noteDTO);

//    noteDTO.setNum(num);
    noteService.modify(noteDTO);

    return new ResponseEntity<>("modified", HttpStatus.OK);
  }

  @DeleteMapping(value = "/{num}", produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> remove(@PathVariable("num") Long num) {
    log.info("========");
    log.info(num);
    noteService.remove(num);

//    return new ResponseEntity<>("removed", HttpStatus.OK);
    return ResponseEntity.ok("removed");
  }


}
