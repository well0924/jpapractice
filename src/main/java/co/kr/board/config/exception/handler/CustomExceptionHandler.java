package co.kr.board.config.exception.handler;

import co.kr.board.config.exception.dto.ErrorCode;
import lombok.Getter;

@Getter
public class CustomExceptionHandler extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ErrorCode errorcode;
	
	public CustomExceptionHandler(String message,ErrorCode error) {
		super(message);
		this.errorcode = error;
	}
	
	public CustomExceptionHandler(ErrorCode error) {
		super(error.getMessage());
		this.errorcode = error;
	}
}
