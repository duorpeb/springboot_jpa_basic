package com.example.bootJPA.service;

import com.example.bootJPA.dto.CommentDTO;
import com.example.bootJPA.entity.Comment;
import com.example.bootJPA.repository.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService{
  // 초기화
  private final CommentRepository commentRepository;


  // 댓글 수정
  @Override
  public boolean updateCmt(CommentDTO cdto) {
    Optional<Comment> optionalComment = commentRepository.findById(cdto.getCno());

    if(optionalComment.isPresent()){
      Comment cmt = optionalComment.get();

      cmt.setContent(cdto.getContent());
      commentRepository.save(cmt);

      return true;
    }

    return false;
  }


  // 댓글 삭제
  @Override
  public boolean deleteCmt(long cno) {
    // return type 이 void 라면, commentRepository.deleteById(cno); 한 줄로 사용 가능
     //     Board bd = boardRepository.findById(bno).orElseThrow(() -> new EntityNotFoundException("해당 게시물이 없습니다!"));
    commentRepository.deleteById(cno);

    // 삭제 확인
    Optional<Comment> optionalComment = commentRepository.findById(cno);
    if(optionalComment.isEmpty()){ return true; }


    return false;
  }


  // 댓글 추가
  @Override
  public boolean postCmt(CommentDTO cdto) {
    Long isOk = commentRepository.save(convertDtoToEntity(cdto)).getBno();

    // isOk != null === isOk != null ? true : false
    return isOk != null;
  }

  // 댓글 리스트 불러오기
  @Override
  public Page<CommentDTO> getCmtList(Long bno, int page) {
    Pageable pageable = PageRequest.of(page, 5, Sort.by("cno").descending());

    Page<Comment> list = commentRepository.findByBno(bno, pageable);
    log.info("getCmtList(Long bno, int page) 의 list : {}", list.getContent());

    return list.map(this::convertEntityToDto);
  }


}
