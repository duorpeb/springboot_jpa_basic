package com.example.bootJPA.handler;

import com.example.bootJPA.dto.BoardDTO;
import com.example.bootJPA.dto.CommentDTO;
import com.example.bootJPA.entity.Comment;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

import java.util.List;

@Slf4j
@Getter
@Setter
@ToString
public class PagingHandler {
  // 초기화
  private int startIdx;
  private int endIdx;
   // 전체 페이지 수
  private int ttcPage;
   // 전체 데이터 개수
  private long ttc;
  private boolean hasPrev, hasNext;
  private int pageNo;
   // 검색을 위한 초기화
  private String type;
  private String keyword;
   // 댓글 리스트
  private List<CommentDTO> cvoList;


  // 생성자
  public PagingHandler(Page<BoardDTO> list, int pageNo, String type, String keyword){
    // 초기화
     // 시작 번지
    this.pageNo = pageNo;
     // 전체 데이터 개수
    this.ttc = list.getTotalElements();
     // 전체 페이지 개수
    this.ttcPage = list.getTotalPages();
     // 페이지네이션 페이징 단위 정의 (e.g., 페이징 단위가 10일 경우 (qty=10) 1~10, 11~20, ...)
    this.endIdx = (int)Math.ceil(this.pageNo/10.0)*10;
    this.startIdx = endIdx - 9;
     // 전체 데이터 개수만큼 마지막 페이지 설정
    this.endIdx = (endIdx > ttcPage) ? ttcPage : endIdx;
     // 이전, 다음 초기화
    this.hasPrev = this.startIdx > 0;
    this.hasNext = this.endIdx < this.ttc;
    // 검색 변수 초기화
    this.type = type;
    this.keyword = keyword;
  }


  // 생성자
  public PagingHandler(Page<CommentDTO> list, int pageNo){
    // 초기화
     // 시작 번지
    this.pageNo = pageNo;
     // 전체 데이터 개수
    this.ttc = list.getTotalElements();
     // 전체 페이지 개수
    this.ttcPage = list.getTotalPages();
     // 페이지네이션 페이징 단위 정의 (e.g., 페이징 단위가 10일 경우 (qty=10) 1~10, 11~20, ...)
    this.endIdx = (int)Math.ceil(this.pageNo/5.0)*5;
    this.startIdx = endIdx - 5;
     // 전체 데이터 개수만큼 마지막 페이지 설정
    this.endIdx = (endIdx > ttcPage) ? ttcPage : endIdx;
     // 이전, 다음 초기화
    this.hasNext = this.endIdx < this.ttc;
     /** 댓글 리스트 초기화
      *
      *   > getContent() 는 현재 요청한 “페이지” 안에 담긴 실제 엔티티(또는 DTO) 목록을
      *     List"<"CommentDTO">" 형태로 꺼내주는 메서드
      *
      *   > Page 객체는 내부적으로 콘텐츠(Content) + 페이징 메타데이터 (총 요소 수, 총 페이지 수, 현재 페이지 번호 등)
      *     를 갖고 있는데 getContent() 만 호출하면 메타데이터를 제외한 “지금 이 페이지에서 보여줄 순수한 댓글 리스트”만
      *     추출해서 return
      *
      * */
    this.cvoList = list.getContent();
  }

}
