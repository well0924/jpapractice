package co.kr.board.Exception;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class ExceptionResponse {
	
	private String title;
    private Integer status;
    private LocalDateTime timestamp;
    private String developerMessage;
}
