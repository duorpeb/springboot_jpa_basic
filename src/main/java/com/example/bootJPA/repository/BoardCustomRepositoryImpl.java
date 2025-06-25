package com.example.bootJPA.repository;

import com.example.bootJPA.entity.Board;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
// Boolean 은 querydsl 의 BooleanExpression
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
// Board import
import java.util.List;

import static com.example.bootJPA.entity.QBoard.board;

public class BoardCustomRepositoryImpl implements BoardCustomRepository{
  // 초기화
  private final JPAQueryFactory queryFactory;

  public BoardCustomRepositoryImpl(EntityManager entityManager){
    this.queryFactory = new JPAQueryFactory(entityManager);
  }

  /** 구현
   *
   *    > BooleanExpression 은 일반적으로 동적 쿼리를 작성할 때, 사용하는 객체
   *
   */
  @Override
  public Page<Board> searchBoard(String type, String keyword, Pageable pageable) {
    // 초기화
    BooleanExpression condition = null;

    // 동적 검색 조건 추가
    if(type != null && keyword != null){
      // Type 이 여러 개인 경우 배열로 처리
      String[] typeArr = type.split("");

      for(String t : typeArr){
        switch(t){
          case "t":
            condition = (condition == null) ? board.title.containsIgnoreCase(keyword)
                : condition.or(board.title.containsIgnoreCase(keyword));
            break;

          case "w":
            condition = (condition == null) ? board.writer.containsIgnoreCase(keyword)
                : condition.or(board.writer.containsIgnoreCase(keyword));
            break;

          case "c":
            condition = (condition == null) ? board.content.containsIgnoreCase(keyword)
                : condition.or(board.content.containsIgnoreCase(keyword));
            break;
        }
      }
    }
    // 쿼리 작성 및 페이징 적용
    List<Board> result
        = queryFactory.selectFrom(board)
                      .where(condition)
                      .orderBy(board.bno.desc())
                      .offset(pageable.getOffset())
                      .limit(pageable.getPageSize())
                      .fetch();
    /** 검색된 데이터의 전체 개수를 직접 조회 해야 함
     *
     *    > getPageList(int pageNo) 를 사용하는 경우, Page<Board> bdList = boardRepository.findAll(pageable);
     *      구문에서 페이징 및 전체 개수를 조회했지만 동적 쿼리를 작성하여 페이징을 하는 경우 직접 전체 개수를
     *      구해야 함
     * */
    long ttc
        = queryFactory.selectFrom(board)
                      .where(condition)
                      .fetch().size();

    return new PageImpl<>(result, pageable, ttc);
  }
}
