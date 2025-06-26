package com.example.bootJPA.dto;

import com.example.bootJPA.entity.TimeBase;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.*;

import java.sql.Time;
import java.time.LocalDateTime;

import static com.example.bootJPA.converter.TimeConverter.timeOrDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileDTO {
  private String uuid;
  private String saveDir;
  private String fileName;
  private int fileType;
  private long bno;
  private long fileSize;
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
