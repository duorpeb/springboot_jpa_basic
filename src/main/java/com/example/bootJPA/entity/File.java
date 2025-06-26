package com.example.bootJPA.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Table(name="file")
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class File extends TimeBase{
  /** 초기화
   *
   *    > length 의 default 값은 256
   *
   *    > Column 생성 시 default 값 초기화 방법은 columnDefinition 을 사용
   *
   *    > 별도의 설정을 할 필요가 없다면 @Column Annotation 을 사용하지 않아도 됨
   */
  @Id
  private String uuid;
  @Column(name = "save_dir", nullable = false)
  private String saveDir;
  @Column(name="file_name", nullable = false)
  private String fileName;
  @Column(name="file_type", nullable = false, columnDefinition = "int default 0")
  private int fileType;
  @Column
  private long bno;
  @Column(name="file_size")
  private long fileSize;
}
