package com.example.bootJPA.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BoardAndFileDTO {
  private  BoardDTO bdto;
  private List<FileDTO> fdtoList;
}

