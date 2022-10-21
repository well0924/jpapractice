package co.kr.board.config.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import co.kr.board.config.exception.dto.ErrorDto;
import co.kr.board.config.exception.dto.Response;

@RestControllerAdvice
public class GlobalCustomExceptionHandler {
	
	@ExceptionHandler(value= Exception.class)
	public Response<?>IllegalArgumentException(Exception e)throws Exception{
		
		return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
	}
	
	@ExceptionHandler({CustomExceptionHandler.class})
	protected ResponseEntity<ErrorDto> HandleCustomException(CustomExceptionHandler ex) {
        return new ResponseEntity<ErrorDto>(
        		new ErrorDto(ex.getErrorcode().getStatus(), ex.getErrorcode().getMessage()), HttpStatus.valueOf(ex.getErrorcode().getStatus()));
    }
}
