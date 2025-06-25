package com.example.bootJPA.service;

import com.example.bootJPA.dto.BoardDTO;
import com.example.bootJPA.entity.Board;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BoardService {
  /** Long insert(BoardDTO bdto);
   *
   * > Interface 는 추상 메서드만 가능하기에 default 메서드를 사용
   *
   * > View 에서 가져온 BoardDTO 객체를 board TABLE 에 저장하기 위해 board TABLE 에 맞는 객체로 변환 해야 함
   *   또한, board TABLE 에서 가져온 객체를 View 에 뿌리기 위한 BoardDTO 객체로 변환 해야 함
   *
   * > BoardDTO 의 멤버 변수는 bno, title, writer, content, regDate, modDate
   *   board TABLE 의 Attribute 는 bno, title, writer, content
   *
   */
  // BoardDTO => board TABLE 의 객체로 변환
  default Board convertDtoToEntity(BoardDTO bdto){
    return
    Board.builder()
         .bno(bdto.getBno())
         .title(bdto.getTitle())
         .writer(bdto.getWriter())
         .content(bdto.getContent())
         .build();
  }

  // board TABLE 의 객체 => BoardDTO
  default BoardDTO convertEntitytoDto(Board bd){
    return
    BoardDTO.builder()
            .bno(bd.getBno())
            .title(bd.getTitle())
            .writer(bd.getWriter())
            .content(bd.getContent())
            .regDate(bd.getRegDate())
            .modDate(bd.getModDate())
            .build();
  }
  
  // 게시물 작성
  Long insert(BoardDTO bdto);

  // 전체 게시물 가져오기
  List<BoardDTO> getList();

  // 페이지네이션 된 전체 게시물 가져오기
  Page<BoardDTO> getPageList(int pageNo);

  // 게시물 상세 조회
  BoardDTO getDetail(long bno);
  
  // 게시물 삭제
  void delete(Long bno);

  // 게시물 수정
  Long modify(BoardDTO bdto);

  // Search 가 포함된 게시물을 가져와서 페이지네이션
  Page<BoardDTO> getList(int pageNo, String type, String keyword);
}
