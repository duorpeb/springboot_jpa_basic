package com.example.bootJPA.controller;

import com.example.bootJPA.dto.BoardAndFileDTO;
import com.example.bootJPA.dto.BoardDTO;
import com.example.bootJPA.dto.FileDTO;
import com.example.bootJPA.entity.Board;
import com.example.bootJPA.handler.FileHandler;
import com.example.bootJPA.handler.PagingHandler;
import com.example.bootJPA.service.BoardService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;


@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/board/*")
public class BoardController {
  // 초기화
  private final BoardService bsv;
  private final FileHandler fh;

  // Mapping
  @GetMapping("/register")
  public void register(){}

  /** 파일이 있는 경우의 insert
   *
   *
   * */
  @PostMapping("/insert")
  public String insert(BoardDTO bdto, @RequestParam(name = "file", required = false)MultipartFile[] files){
    // Init
    List<FileDTO> fdtoList = null;

    if(files != null && files[0].getSize() > 0){
      // 파일 핸들러 작업
      fdtoList = fh.upFiles(files);
    }

    Long bno = bsv.insert(new BoardAndFileDTO(bdto, fdtoList));

    return "redirect:/board/list";
  }


/* 파일이 없는 경우의 insert
  @PostMapping("/insert")
  public String insert(BoardDTO bdto){
    *//**
     *  > 이전의 MyBatis 에서는 insert, update, delete 는 Query 의 실행으로 영향을 받은 ROW 의 개수를 Return
     *    했지만 JPA 는 id 를 return
     * *//*
    Long bno = bsv.insert(bdto);
    log.info("BoardController 의 insert 시 반환받은 id : {}", bno);

    return "/index";
  }*/


  /* public void list(Model m){ ... }
  *
  * > Page<T> 사용 시, 시작 번지는 0 부터 시작
  *   (e.g., SELECT * FROM board ORDER BY bno DESC LIMIT 0, 10)
  *
  * > @RequestParam 사용 시 name, required 말고도 defaultValue 설정 가능
  * */
  @GetMapping("/list")
  public void list(Model m, @RequestParam(name="pageNo", required = false, defaultValue = "1") int pageNo
    ,@RequestParam(name="type", required = false) String type, @RequestParam(name = "keyword",required = false) String keyword){
    // 초기화
     // 시작 번지
//    pageNo = 1;
     // 페이지네이션 된 전체 게시물, Page<BoardDTO> bdtoList = bsv.getPageList(pageNo-1);

    // 페이지네이션 - Search 가 없는 경우 PagingHandler ph = new PagingHandler(bdtoList, pageNo);

    /* 페이지네이션 없는 전체 게시물 조회
      List<BoardDTO> list = bsv.getList();
      m.addAttribute("list", list);
    */
    int page = pageNo-1;
    Page<BoardDTO> pageList = bsv.getList(page, type, keyword);
    // Page<T> 의 log
    log.info("============================== Page<T> 의 log ==============================");
    log.info("Page<T> 의 전체 글 수 : {}", pageList.getTotalElements());
    log.info("Page<T> 의 전체 페이지수 : {}", pageList.getTotalPages());
    log.info("Page<T> 의 현재 페이지 번호 : {}", pageList.getNumber());
    log.info("Page<T> 에서 한 페이지에 담을 최대 데이터 개수 : {}", pageList.getSize());
    log.info("Page<T> 에서 현재 페이지 정보 : {}", pageList.getPageable());
    log.info("Page<T> 에서 다음 페이지가 존재하는지의 여부 (boolean) : {}", pageList.hasNext());
    log.info("Page<T> 에서의 다음 페이지 요청 정보 : {}", pageList.nextPageable());
    log.info("Page<T> 에서의 이전 페이지가 존재하는지의 여부 (boolean) : {}", pageList.hasPrevious());
    log.info("Page<T> 에서의 이전 페이지 요청 정보 : {}", pageList.previousPageable());
    log.info("============================== Page<T> 의 log fin ==============================");

    PagingHandler ph = new PagingHandler(pageList, pageNo, type, keyword);

    m.addAttribute("list", pageList);
    m.addAttribute("ph", ph);
  }

  
  /* @GetMapping("/detail") - 게시물 상세 조회 
  *
  *   > 2025-06-24T09:49:02.928066 를 2025-06-24 로 변환하고 싶은 경우, Controller 에서
  *     변환해서 set 하는 것이 아닌 html 에서 #temporals 를 사용
  *
  *   > e.g., th:value="${#temporals.format(bdto.regDate, 'yyyy-MM-dd')}"
  *
  * */
  @GetMapping("/detail")
  public void detail(@RequestParam("bno") Long bno, Model m){
    /**
     *  > 이전의 MyBatis 에서는 insert, update, delete 는 Query 의 실행으로 영향을 받은 ROW 의 개수를 Return
     *    했지만 JPA 는 id 를 return
     * */
    BoardAndFileDTO bAfDto = bsv.getDetail(bno);

    m.addAttribute("bAfDto", bAfDto);
  }

  
  /* @GetMapping("/delete") - 게시물 삭제 
  * 
  * 
  * */
  @GetMapping("/delete")
  public String delete(@RequestParam("bno") long bno, RedirectAttributes re){
    bsv.delete(bno);

    return "redirect:/board/list";
  }

  
  /* @PostMapping("/modify) - 게시물 수정
  * 
  * */
  @PostMapping("/modify")
  public String modify(BoardDTO bdto, RedirectAttributes re, @RequestParam(name = "file", required = false)
                       MultipartFile[] files){
    // 확인
    log.info("/modify 의 bdto : {}", bdto);

    // 초기화
    List<FileDTO> fdtoList = null;

    if(files != null && files[0].getSize() > 0){
      fdtoList = fh.upFiles(files);
    }

    Long isOk = bsv.modify(new BoardAndFileDTO(bdto, fdtoList));

    /*re.addAttribute("bno", bdto.getBno());*/

    return "redirect:/board/detail?bno=" + bdto.getBno();
  }


  /* @DeleteMapping("/fileDel") - 파일 삭제
  *
  * */
  @ResponseBody
  @DeleteMapping
  public String fileDel(@RequestParam("uuid") String uuid){
    String isOk = "";

    if(bsv.fileDel(uuid)){ isOk = "1"; }

    return isOk;
  }
}
