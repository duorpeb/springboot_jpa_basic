package com.example.bootJPA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/** Annotation
 *
 * > @EnableJpaAuditing 은 JPA 의 Audit 를 사용하기 위한 Annotation (JPA 필수 Annotation)
 *
 * > Scheduling 사용 시 이 부분에 @Enablescheduling Annotation 작성
 *
 * */
@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class BootJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootJpaApplication.class, args);
	}

}
