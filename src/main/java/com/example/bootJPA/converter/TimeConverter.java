package com.example.bootJPA.converter;

import java.time.LocalDate;
import java.time.LocalDateTime;


/** LocalDateTime()
 *
 *    > LocalDateTime은 날짜 (LocalDate) 와 시간 (LocalTime) 을 모두 다루는 java.time 패키지의 불변
 *      (immutable) 클래스
 *
 *    > 생성 : now(), of(), parse()
 *      조회 : getYear(), getMonthValue(), getHour() 등
 *      연산 : plusXXX(), minusXXX(), withXXX()
 *      변환 : toLocalDate(), toLocalTime()
 *      비교 : isAfter(), until()
 *      포맷팅 : format(), DateTimeFormatter
 *
 * */
public final class TimeConverter {
  /** timeOrDate(LocalDateTime t) - 날짜와 시간을 추출하는 메서드
   *
   *    > toLocalDate() java.time.LocalDateTime 객체에서 날짜 부분 (년-월-일)만 추출해 주는 메서드로
   *      java.time.LocalDateTime 객체를 return
   *
   *    > 반환된 LocalDate 객체는 변경 불가능하며, 원본 LocalDateTime도 그대로 유지
   *
   *    > LocalDateTime 은 (연, 월, 일, 시, 분, 초, 나노초) 로 구성되어 있음
   *
   */
  public static String timeOrDate(LocalDateTime t){
    // LocalDateTime 객체에서 날짜와 시간을 추출
    String date = t.toLocalDate().toString(),
           time = t.toLocalTime().toString().substring(0,8);

    if(LocalDate.now().toString().equals(date)) { return time; }

    return date;
  }
}
