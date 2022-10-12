package co.kr.board.config.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import co.kr.board.config.exception.Response;

@RestControllerAdvice
public class CustomExceptionHandler {
	
	@ExceptionHandler(value= IllegalArgumentException.class)
	public Response<String>illefalArgumentException(Exception e)throws Exception{
		
		return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
	}
}
