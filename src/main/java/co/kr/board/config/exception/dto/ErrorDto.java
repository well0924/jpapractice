package co.kr.board.config.exception.dto;

import groovy.transform.builder.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ErrorDto {
	
	private Integer errorcode;
	
	private String message;
}
