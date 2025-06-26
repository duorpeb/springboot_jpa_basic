package com.example.bootJPA.handler;

import com.example.bootJPA.dto.FileDTO;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class FileHandler {
  // 초기화
   // 파일 경로 설정 - 게시물 이미지
  private final String UP_DIR = "D:\\Jstl_Servlet_Spring\\_myProject\\_Java\\_fileUpload\\";


  // 메서드
  public List<FileDTO> upFiles(MultipartFile[] files){
    List<FileDTO> fdtoList = new ArrayList<>();

    // 폴더 생성
     // 현재 날짜의 폴더 생성을 위한 초기화
    LocalDate today = LocalDate.now(); // 2025-06-19
     // String 으로 변환
    String strToday = today.toString().replace("-", File.separator);
     // 폴더 생성을 위한 파일 객체 생성
    File folders = new File(UP_DIR, strToday);
     // 기존 파일이 없는 경우에만 생성
    if(!folders.exists()){
      // mkdir 은 폴더 1개 (2025) 만, mkdirs 는 하위 폴더 (2025\06\19)까지 생성
      folders.mkdirs();
    }


    // files 를 가지고 FileVO 를 생성
    for(MultipartFile f : files){
      FileDTO fdto = new FileDTO();

      /** FileVO 초기화
       *
       * > FileVO 의 멤버 변수
       *    private String uuid;
       *    private String saveDir;
       *    private String fileName;
       *    private int fileType;
       *    private long bno;
       *    private long fileSize;
       *    private String regDate;
       *
       * */
      // 파일 저장 디렉토리 초기화
      fdto.setSaveDir(strToday);
      // 파일 사이즈 초기화
      fdto.setFileSize(f.getSize());
      // 파일 이름 초기화
      String strFileName = f.getOriginalFilename();
      fdto.setFileName(strFileName);
      // uuid 초기화
      UUID uuid = UUID.randomUUID();
      String uuidStr = uuid.toString();
      fdto.setUuid(uuidStr);
      // 파일 타입 초기화 - 파일 타입 초기화는 파일 타입을 검사 한 후 진행
      int type = f.getContentType().startsWith("image") ? 1 : 0;
      fdto.setFileType(type);

      /** 폴더에 파일을 실제로 저장하는 코드
       *
       * > 파일 저장 폴더에 저장 시 uuid + "_" + getOriginalFilename() 형태로 저장
       *
       * > image 파일인 경우 Thumbnail Image 까지 저장 (uuid + "_th_" + getOriginalFilename)
       *
       * */
      //
      String fileName = uuidStr + "_" + strFileName;
      String thFileName = uuidStr + "_th_" + strFileName;
      // 파일 저장을 위한 파일 객체 생성
      File saveFile = new File(folders, fileName);

      try {
        // FIle.transferTo() - 파일을 실제로 디스크에 저장
        f.transferTo(saveFile);

        if(type == 1){
          File thumbnail = new File(folders, thFileName);
          Thumbnails.of(saveFile).size(100,100).toFile(thumbnail);
        }

      } catch (Exception e) {
        log.info(" File 저장 시 catch : {}", e.toString());
        e.printStackTrace();
      }

      fdtoList.add(fdto);
    }
    log.info("FileHandler 에서 fvoList : {}", fdtoList);

    return fdtoList;
  }


  /** isCheckImg(MultipartFile profile) - 이미지파일 인지 확인
   *
   * > Spring 에서 tika 를 사용하여 image 파일인지 체크한 구문
   *
   *  	// isImageFile(MultipartFile file) - MultipartFile 인 경우 img 파일인지 확인하는 메서드
   * 	private boolean isImageFile(MultipartFile file) {
   * 	    try {
   * 	        String mime = new Tika().detect(file.getInputStream());
   * 	        return mime != null && mime.startsWith("image/");
   * 	          } catch (IOException e) {
   * 	        return false;
   *     }
   *   }
   */
  private boolean isCheckImg(MultipartFile profile){
    // startsWith(String prefix) 는 해당 문자열 (prefix) 이 특정 접두어 (시작 문자열) 로 시작하는지를 검사하는 Java의 메서드로 true or false 를 반환
    boolean bool = profile.getContentType().startsWith("image/");

    return bool;
  }
}
