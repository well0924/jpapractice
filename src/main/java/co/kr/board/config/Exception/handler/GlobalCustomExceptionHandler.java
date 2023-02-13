package co.kr.board.config.Exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import co.kr.board.config.Exception.dto.ErrorDto;
import co.kr.board.config.Exception.dto.Response;

@RestControllerAdvice
public class GlobalCustomExceptionHandler {
	
	@ExceptionHandler(value= Exception.class)
	public Response<?>IllegalArgumentException(Exception e){
		
		return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
	}
	
	@ExceptionHandler({CustomExceptionHandler.class})
	protected ResponseEntity<ErrorDto> HandleCustomException(CustomExceptionHandler ex) {
        return new ResponseEntity<>(
        		new ErrorDto(ex.getErrorCode().getStatus(), ex.getErrorCode().getMessage()), HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }
}
