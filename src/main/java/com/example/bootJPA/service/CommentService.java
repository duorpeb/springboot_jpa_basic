package com.example.bootJPA.service;

import com.example.bootJPA.dto.CommentDTO;
import com.example.bootJPA.entity.Comment;
import org.springframework.data.domain.Page;

/** CommentService
 *
 * > View 에서 가져온 BoardDTO 객체를 board TABLE 에 저장하기 위해 board TABLE 에 맞는 객체로 변환 해야 함
 *   또한, board TABLE 에서 가져온 객체를 View 에 뿌리기 위한 BoardDTO 객체로 변환 해야 함
 *
 * > Interface 는 추상 메서드만 가능하기에 default 메서드를 사용
 *
 * > BoardDTO 의 멤버 변수는 bno, title, writer, content, regDate, modDate
 *   board TABLE 의 Attribute 는 bno, title, writer, content
 *
 */
public interface CommentService {
  // View 객체 (CommentDTO) -> DB 객체 (CommentEntity)
  default Comment convertDtoToEntity(CommentDTO cdto){
    return Comment.builder()
                   .cno(cdto.getCno())
                   .bno(cdto.getBno())
                   .writer(cdto.getWriter())
                   .content(cdto.getContent())
                   .build();
  }

  // entity => DTO
  default CommentDTO convertEntityToDto(Comment comment){
    return CommentDTO.builder()
        .cno(comment.getCno())
        .bno(comment.getBno())
        .writer(comment.getWriter())
        .content(comment.getContent())
        .regDate(comment.getRegDate())
        .modDate(comment.getModDate())
        .build();
  }

  // 댓글 추가
  boolean postCmt(CommentDTO cdto);

  // 해당 게시물의 댓글 리스트 불러오기
  Page<CommentDTO> getCmtList(Long bno, int page);

  // 댓글 삭제
  boolean deleteCmt(long cno);

  // 댓글 수정
  boolean updateCmt(CommentDTO cdto);
}
