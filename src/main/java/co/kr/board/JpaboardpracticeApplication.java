package co.kr.board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class JpaboardpracticeApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpaboardpracticeApplication.class, args);
	}

}
