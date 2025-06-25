package com.example.bootJPA.repository;


import com.example.bootJPA.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/** public interface BoardRepository
 *
 *  > JpaRepository"<"TABLE명, id (PK) 의 자료형">" 형식으로 작성해야 함
 *
 * */
public interface BoardRepository extends JpaRepository<Board, Long>, BoardCustomRepository {
  // JPQL 의 사용 형식 - @Query("select b from BOard b where b.writer = ?1")

  // findByWriter(String writer) - id 가 아닌 다른 조건으로 값을 검색할 때, 사용
  List<Board> findByWriter(String writer);
}
