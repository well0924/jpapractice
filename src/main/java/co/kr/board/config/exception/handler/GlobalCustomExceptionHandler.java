package co.kr.board.config.exception.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import co.kr.board.config.exception.dto.ErrorDto;
import co.kr.board.config.exception.dto.Response;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;

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

	//SSE 알림 관련
	@ExceptionHandler(AsyncRequestTimeoutException.class)
	public ResponseEntity<?> sseTimeoutException(AsyncRequestTimeoutException e) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<String>("", headers, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
