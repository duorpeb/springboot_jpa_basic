package com.example.bootJPA.handler;

import com.example.bootJPA.entity.File;
import com.example.bootJPA.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/** FileSweeper 
 * 
 *    > 매일 정해진 시간에 스케줄러를 실행하여 DB 에 등록된 파일이 폴더에 존재하는지 비교하여 
 *      DB 에 없는데 폴더에 존재하는 파일을 삭제하는 클래스
 * 
 *    > DB 에서 직접 조회 (FileRepository 를 사용) 하여 비교 진행
 *
 * */
@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class FileSweeper {
  // 초기화
   // fileRepository
  private final FileRepository fileRepository;
   // 실제 저장된 경로
  private final String PATH = "D:\\Jstl_Servlet_Spring\\_myProject\\_Java\\_fileUpload\\";

  /** fileSweeper() - DB 와 오늘 날짜의 폴더를 비교하여 DB 에 존재하지 않는데 폴더에 존재하는 파일 삭제
   *
   *    > cron 방식 = 초 분 시 일 월 요일 년도(생략가능)
   */
  @Scheduled(cron = "0 28 17 * * *")
  public void fileSweeper(){
    // 확인
    log.info("====================== FileSweeper 실행 시작 시간 : {}", LocalDateTime.now());

    // DB 에 등록되어 있는 모든 파일 목록을 가져오기
    List<File> dbList = fileRepository.findAll();

    /** 파일 경로 + 파일명을 붙인 실제 파일 리스트를 생성
     *
     *    > 실제 파일 리스트는 아래의 형태
     *
     *      # 일반 파일 - "D:\\Jstl_Servlet_Spring\\_myProject\\_Java\\_fileUpload\\년도\\월\\일\\uuid_파일명.확장자"
     *
     *      # 썸네일 파일 - "D:\\Jstl_Servlet_Spring\\_myProject\\_Java\\_fileUpload\\년도\\월\\일\\uuid_th_파일명.확장자";
     *
     * */
    List<String> currFiles = new ArrayList<>();

    for(File f : dbList){
      String filePath = f.getSaveDir() + java.io.File.separator + f.getUuid();

      String fileName = f.getFileName();

      currFiles.add(PATH + filePath + "_" + fileName);

      // Thumbnail 이 존재한다면
      if(f.getFileType() == 1){
        currFiles.add(PATH + filePath + "_th_" + fileName);
      }
    }
    log.info("====================== FileSweeper >>>>> currFiles (DB 에 존재하는 모든 파일 목록) :{}", currFiles);

    // 실제 저장되어 있는 모든 파일 목록과 DB 의 모든 파일 목록을 비교하여 DB 에 없는 파일을 삭제
     // 비교할 값
    String scTodayValue = LocalDate.now().toString();
     // 파일 경로
    String scTodayPath = scTodayValue.replace("-", java.io.File.separator);

     // 오늘 날짜의 폴더 안에 있는 파일의 모든 목록을 가져오기
    java.io.File dir = Paths.get(PATH + scTodayPath).toFile();
     // 전체 객체를 배열로 나눠서 리턴
    java.io.File[] fileObjArr = dir.listFiles();
     // 확인
    log.info("====================== FileSweeper >>>>> fileObjArr (실제 폴더에 존재하는 모든 파일 목록) :{}", fileObjArr);

    // currFiles 와 fileObjArr 를 비교하여 DB 에 없지만 폴더에 존재하는 파일을 삭제
    for(java.io.File f : fileObjArr){
      // 해당 파일의 경로를 문자열로 추출
      // (e.g., "D:\\Jstl_Servlet_Spring\\_myProject\\_Java\\_fileUpload\\년도\\월\\일\\uuid_파일명.확장자")
      String storedFileName = f.toPath().toString();

      if(!currFiles.contains(storedFileName)){
        log.info("====================== FileSweeper >>>>> 삭제된 파일의 이름 : {}", storedFileName);
        f.delete();
      }
    }


    log.info("====================== FileSweeper 실행 종료 시간 : {}", LocalDateTime.now());
  }

}
