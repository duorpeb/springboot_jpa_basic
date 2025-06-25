package com.example.bootJPA.entity;

import jakarta.persistence.*;
import lombok.*;

/** JPA Annotation, JPA 기본 개념
 * 
 * > @Entity 는 DB 의 TABLE 을 의미하는 Annotation
 * 
 * > JPA 를 사용하는 경우, 객체 생성 클래스를 VO (Value Object) 가 아닌 DTO (Data Transfer Object) 를 사용
 *
 * > JPA Auditing 은 reg_date, mod_date 등 (등록일과 수정일 같은 컬럼 등) 을 모든 클래스에서
 *   동일하게 사용되는 컬럼으로 취급하고 해당 컬럼들을 별도로 관리
 *
 * > @id 는 PRIMARY KEY 를 의미하며 기본키 생성 전략을 GeneratedValue 라고 함
 *   # AUTO_INCREMENT 는 GenerateType.IDENTITY 속성
 *
 *   # 일반 컬럼은 @Column Annotation 을 사용
 *
 *   # @Column 에 별도의 설정이 있는 경우 @Column(설정=값) 형태로 설정
 *
 * ======================================================================================================
 *
 * > 일반적으로 TABLE 명을 클래스명으로 자동 설정하지만 @Table(name="~") 으로 변경 가능
 *
 * > extends TimeBase 로 public class TimeBase 의 reg_date, 와 mod_date 의 필드를 공유
 *
 * */
@Table(name="board")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Board extends TimeBase{
  // 초기화 (board TABLE 생성)
   // bno
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long bno;
   // title, length=256 은 VARCHAR(256) 을 nullable 은 NOT NULL 을 의미
  @Column(length = 256, nullable = false)
  private String title;
   // writer
  @Column(length = 256, nullable = false)
  private String writer;
   // content
  @Column(length = 2000, nullable = false)
  private String content;
}
