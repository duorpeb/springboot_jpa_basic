package com.example.bootJPA.service;

import com.example.bootJPA.dto.BoardAndFileDTO;
import com.example.bootJPA.dto.BoardDTO;
import com.example.bootJPA.dto.FileDTO;
import com.example.bootJPA.entity.Board;
import com.example.bootJPA.entity.File;
import com.example.bootJPA.repository.BoardRepository;
import com.example.bootJPA.repository.FileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{
  // 초기화
  private final BoardRepository boardRepository;
  private final FileRepository fileRepository;


  /* fileDel(String uuid)
  *
  * */
  @Override
  public boolean fileDel(String uuid) {
    Optional<File> optionalFile = fileRepository.findById(uuid);

    if(optionalFile.isPresent()){
      fileRepository.deleteById(uuid);
      return true;
    }

    return false;
  }


  /** getList() - 검색이 추가된 페이지네이션
   *
   *
   * */
  @Override
  public Page<BoardDTO> getList(int pageNo, String type, String keyword) {
    Pageable pageable = PageRequest.of(pageNo, 10,
        Sort.by("bno").descending());

    Page<Board> bdList = boardRepository.searchBoard(type, keyword, pageable);
    // View 객체 (BoardDTO) 로 변환
    Page<BoardDTO> result = bdList.map(this::convertEntitytoDto);

    return result;
  }


 /* @Override
  public Board convertDtoToEntity(BoardDTO bdto) {
    return BoardService.super.convertDtoToEntity(bdto);
  }

  @Override
  public BoardDTO convertEntitytoDto(Board bd) {
    return BoardService.super.convertEntitytoDto(bd);
  }*/

  /** public Long insert(BoardDTO bdto) { ... }
   *
   *  > 저장 객체의 형태는 board TABLE 의 객체 형태
   *
   *  > save() 는 insert, update 의 역할을 수행하며 entity 객체를 Parameter 로 전송
   *
   * */
  @Transactional
  @Override
  public Long insert(BoardAndFileDTO bdto) {
    // bdto 삽입
    Long bno = boardRepository.save(convertDtoToEntity(bdto.getBdto())).getBno();

    if(bno > 0 && bdto.getFdtoList()!= null){
      for(FileDTO f : bdto.getFdtoList()){
        f.setBno(bno);
        bno = fileRepository.save(convertDtoToEntity(f)).getBno();
      }
    }

    return bno;
  }


  /**
   * getList() - 전체 게시물 불러오기
   * <p>
   * > Controller 로 List"<"BoardDTO"">" 를 보내야 하는데 DB 에서 가져오는 return 은 List"<"Board">" 형태 이기에
   * List"<"BoardDTO"> 형태로 변환해야 함
   * <p>
   * > findAll 은 전체 값을 return
   * findById 는 where id = #{id} 와 같은 id 가 일치하는 값을 return
   * <p>
   * > 정렬 하고 싶은 경우, Sort.by(Sort.Direction.DESC, "정렬 기준 Column") 를 사용
   * <p>
   * > JPA 를 사용하는 경우 for 문 보다는 Stream 을 많이 사용
   */
  @Override
  public List<BoardDTO> getList() {
    List<Board> bdList = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "bno"));


/*  for 문을 사용하는 경우
    List<BoardDTO> bdtoList = new ArrayList<>();

    for(Board b : bdList){
      bdtoList.add(convertEntitytoDto(b));
    }

    return bdtoList; */


    /* Stream 을 사용하는 경우 */
    List<BoardDTO> bdtoList
        = bdList.stream()
                .map(b -> convertEntitytoDto(b)).toList();

    return bdtoList;
  }


  /* getPageList(int pageNo) - 페이지네이션된 전체 리스트 가져오기
  *
  * */
  @Override
  public Page<BoardDTO> getPageList(int pageNo) {
    // 초기화
     // 페이지네이션 객체
    Pageable pageable = PageRequest.of(pageNo, 10, Sort.by("bno").descending());
     // DB 객체 (board) 로 받기위한 board 객체 초기화
    Page<Board> bdList = boardRepository.findAll(pageable);
     /* View 객체 (BoardDTO) 로 받기위한 BoardDTO 초기화
     *  
     *  > 아래의 코드는 Page<BoardDTO> bdtoList = bdList.map(thi::convertEntityToDto); 형태로 작성 가능  
     * 
     *  > 페이지네이션 (Page<T>) 를 사용한 객체는 Stream 형태로 return 되기에 바로 map 사용이 가능
     * */ 
    Page<BoardDTO> bdtoList = bdList.map(b -> convertEntitytoDto(b));

    return bdtoList;
  }


  /**
   * getDetail() - 게시물 상세 조회
   *
   *    > findBy() 를 사용하는 경우 Optional Board 형태로 반환
   *
   *    > Optional 이란 자바 8에 도입된 컨테이너 클래스로 내부에 값이 있거나 (Optional.of(value))
   *      없거나(Optional.empty()) 둘 중 하나의 상태만 가질 수 있음
   *
   *      # 값을 직접 꺼내기 전에 존재 여부를 강제 체크하도록 유도하여 NPE(NullPointerException) 을
   *        방지하는 컨테이너 클래스
   *
   *      # Optional.isEmpty() 는 null 인 경우 true, null 이 아닌 경우 false 를 반환
   *
   *      # Optional.isPresent() 는 값이 있는지를 확인하고 있다면 true 없다면 false 를 반환
   *
   *      # Optional.get 은() 객체를 가져옴
   */
  @Transactional
  @Override
  public BoardAndFileDTO getDetail(long bno) {
    // DB 객체로 받아와 View 객체로 반환
    Optional<Board> optionBd = boardRepository.findById(bno);

    if(optionBd.isPresent()){
      // bno 에 일치하는 BoardDTO 가져오기
      BoardDTO bdto = convertEntitytoDto(optionBd.get());

      // bno 에 일치하는 File List 가져오기
      List<File> fileList = fileRepository.findByBno(bno);

      // File -> FileDTO
      List<FileDTO> fdtoList = fileList.stream().map(this::convertEntityToDto).toList();

      // BoardAndFileDTO 생성
      BoardAndFileDTO badto = new BoardAndFileDTO(bdto, fdtoList);

      return badto;
    }

    return null;
  }


  /** delete() - 게시물 삭제
   *
   *  > findeOne(PK)
   * */
  @Transactional
  @Override
  public void delete(Long bno) {
    Board bd = boardRepository.findById(bno).orElseThrow(() -> new EntityNotFoundException("해당 게시물이 없습니다!"));

    boardRepository.delete(bd);
    // 위의 2줄 코드를 boardRepository.deleteById(bno); 한 줄로 대체 가능
  }

  
  /* modify() - 게시물 수정
  *
  *   > save(id) 는 id 가 없으면 insert 작업을 수행하고 없으면 update 작업을 수행하는데
  *     id 가 존재하지 않는다면 에러 (EntityNotFoundException) 발생
  *
  *     ㄴ 따라서, save(id) 를 사용하는 경우 정보 유실의 위험도가 높아지고 DB 의 설계 원칙 중 하나인 안정성을
  *        준수하지 못함 (reg_date 같은 경우 데이터가 아예 사라짐)
  *
  *     ㄴ 또한 변동 감지 (Dirty Checking) 의 미작동 가능성이 높아짐
  * ￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣
  *   > 따라서 수정 시 바로 save(id) 작업을 수행하기 보다 findById(id) 를 통해 먼저 조회한 뒤 영속 상태를 만든 후 수정
  *
  *   > @Transactional Annotation
  *     ㄴ @Transactional 은 Dirty Checking 만으로 업데이트가 가능 (save() 없이도 가능)
  *
  *     ㄴ Dirty Checking 이란 영속성 컨텍스트에 유지되고 있는 엔티티 객체의 변경 여부를 자동으로 감지하고
  *       트랜잭션 커밋 시점에 변경된 부분만 데이터베이스에 반영(UPDATE)해 주는 기능을 의미
  *
  *     ㄴ 엔티티가 영속성 컨텍스트에 올라가 있는 상태를 영속상태라고 하며 엔티티가 영속상태 일 때,
  *        해당 객체의 필드가 변경되면 Transaction 이 종료되기 전 JPA 가 변경된 부분만을 자동 감지하여
  *        UPDATE Query 문을 실행
  *
  *     ㄴ save() 없이 수정된 필드 (명시적으로 호출되지 않은 필드) 를 DB 에 자동 반영
  * */
  @Transactional
  @Override
  public Long modify(BoardAndFileDTO badto) {
    /* Case B) @Transactional 을 사용한 수정 - @Transactional Annotation 추가 */
     /* DB 저장용 객체 초기화 방법 - 1)
     *
     * Optional<Board> opBd = boardRepository.findById(bdto.getBno());
     * opBd.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다!"));
     * Board bd = opBd.get();
     * */

    /* DB 저장용 객체 초기화 방법 - 2)
    *
    *   > DB 저장용 객체 초기화 방법 1을 아래처럼 한 줄로 작성 가능
    *
    * */
    BoardDTO bdto = badto.getBdto();
    Board bd = boardRepository.findById(bdto.getBno()).orElseThrow(
        () -> new EntityNotFoundException("존재하지 않는 게시글입니다!"));

     // UPDATE 작업 수행
    bd.setTitle(bdto.getTitle());
    bd.setContent(bdto.getContent());

    if(badto.getFdtoList() != null ){
      for(FileDTO fdto : badto.getFdtoList()){
        fdto.setBno(bdto.getBno());
        Long bno = fileRepository.save(convertDtoToEntity(fdto)).getBno();
      }
    }

    return bd.getBno();


    /* Case A) findById(id) 를 통해 먼저 조회한 뒤 영속 상태를 만든 후 수정 */
/*    Optional<Board> opBd = boardRepository.findById(bdto.getBno());

    if(opBdto.isPresent()){
      // DB 저장용 객체 초기화
      Board bd = opBd.get();
      // UPDATE 작업 수행
      bd.setTitle(bdto.getTitle());
      bd.setTitle(bdto.getContent());

      return  boardRepository.save(bd).getBno();
    } else { return -666L; } */
  }
}
