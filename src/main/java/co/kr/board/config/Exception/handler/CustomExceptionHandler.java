package co.kr.board.config.Exception.handler;

import co.kr.board.config.Exception.dto.ErrorCode;
import co.kr.board.config.Exception.dto.ErrorDto;
import lombok.Getter;

@Getter
public class CustomExceptionHandler extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final ErrorCode errorcode;

	public CustomExceptionHandler(String message, ErrorCode error) {
		super(message);
		this.errorcode = error;
	}

	public CustomExceptionHandler(ErrorCode error) {
		super(error.getMessage());
		this.errorcode = error;
	}

    public ErrorCode getErrorCode() {
        return errorcode;
    }
}
