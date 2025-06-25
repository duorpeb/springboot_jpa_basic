package com.example.bootJPA.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/** public class TimeBase
 *
 *  > 등록일, 수정일을 별도로 관리하는 클래스
 *
 *  > Auditing 설정 시 @MappedSuperclass, @EntityListeners(value={AuditingEntityListener.class}) 는 반드시 설정
 * */
@Getter
@MappedSuperclass
@EntityListeners(value={AuditingEntityListener.class})
public class TimeBase {
  @CreatedDate
  @Column(name="reg_date",updatable = false)
  private LocalDateTime regDate;

  @LastModifiedDate
  @Column(name="mod_date")
  private LocalDateTime modDate;
}
