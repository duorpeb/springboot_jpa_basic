package com.example.bootJPA.repository;

import com.example.bootJPA.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/** JpaRepository(엔티티 이름, 기본키의 자료형)
 * */
public interface FileRepository extends JpaRepository<File, String> {
  // findByBno() - bno 에 맞는 File List 가져오기
  List<File> findByBno(long bno);
}
