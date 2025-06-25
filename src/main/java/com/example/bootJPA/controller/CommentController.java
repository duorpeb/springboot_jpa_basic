package com.example.bootJPA.controller;

import com.example.bootJPA.dto.CommentDTO;
import com.example.bootJPA.handler.PagingHandler;
import com.example.bootJPA.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment/*")
@Slf4j
public class CommentController {
  // 초기화
  private final CommentService csv;


  // update() - 댓글 수정
  @ResponseBody
  @PutMapping("/update")
  public String update(@RequestBody CommentDTO cdto){
    String isOk = "";

    if(csv.updateCmt(cdto)){ isOk = "1"; }

    return isOk;
  }

  // delete() - 댓글 삭제
  @ResponseBody
  @DeleteMapping("/delete")
  public String delete(@RequestParam("cno") long cno){
    String isOk = "";

    if(csv.deleteCmt(cno)){ isOk = "1"; }

    return isOk;
  }


  // getList() - 해당 게시물의 댓글 목록 가져오기
  @ResponseBody
  @GetMapping(value="/{bno}/{page}")
  public PagingHandler getList(@PathVariable("bno") Long bno, @PathVariable("page") int page){
    int pageInit = page - 1;

    Page<CommentDTO> cvoList = csv.getCmtList(bno, pageInit);

    PagingHandler ph = new PagingHandler(cvoList, page);
    log.info("Controller 의 getList() - ph.cvoList : {}", ph.getCvoList());
    return ph;
  }


  // post() - 댓글 추가
  @ResponseBody
  @PostMapping("/post")
  public String post(@RequestBody CommentDTO cdto){
    log.info("/comment/post 의 cdto : {}", cdto);

    String isOk = "";

    if(csv.postCmt(cdto)){ isOk = "1"; }

    return isOk;
  }
}
