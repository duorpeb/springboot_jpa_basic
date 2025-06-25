package com.example.bootJPA.repository;

import com.example.bootJPA.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *    > JpaRepository"<"id, id의 type>
 *
 * */
public interface CommentRepository extends JpaRepository<Comment, Long>{
  // id 가 아닌 다른 조건으로 값을 검색하는 경우를 위한 메서드
  Page<Comment> findByBno(long bno, Pageable pageable);
}
