package com.example.bootJPA.entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name="comment")
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment extends TimeBase{
  // 초기화
   // cno (PK)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
  private Long cno;
   // bno
  @Column(nullable = false)
  private Long bno;
   // writer
  @Column(length = 256, nullable = false)
  private String writer;
   // content
  @Column(length = 2000)
  private String content;
}
