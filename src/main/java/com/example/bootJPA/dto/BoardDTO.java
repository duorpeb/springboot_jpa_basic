package com.example.bootJPA.dto;

import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

import static com.example.bootJPA.converter.TimeConverter.timeOrDate;

/** public class BoardDTO
 *
 * > BoardDTO 는 bno, title, writer, content, reg_date, mod_date 를 가짐
 *
 * */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardDTO {
  // 초기화
  private Long bno;
  private String title;
  private String writer;
  private String content;
  private LocalDateTime regDate, modDate;


  // 메서드
   //
  public String getRegTimeOrDate(){ return timeOrDate(regDate); }
   //
  public String getModTimeOrDate() {
    if(regDate.equals(modDate)) { return "-"; }

    return timeOrDate(modDate);
  }
}
