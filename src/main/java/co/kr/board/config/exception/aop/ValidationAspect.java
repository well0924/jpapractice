package co.kr.board.config.exception.aop;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import co.kr.board.config.exception.dto.Response;
import lombok.extern.log4j.Log4j2;

//aop
@Log4j2
@Aspect
@Component
public class ValidationAspect {
	
	
	//@Around: 함수의 앞,뒤 모두 제어
	//패키지 내에서 ApiController라는 이름의 클래스 내에 있는 모든 메서드에 적용
	@Around("execution(* co.kr.board.**..*ApiController.*(..))")
	public Object apiAdvice(ProceedingJoinPoint proceedingjoinpoint)throws Throwable{
		
		String typeName = proceedingjoinpoint.getSignature().getDeclaringTypeName();
	    String name = proceedingjoinpoint.getSignature().getName();
	    Object[] args = proceedingjoinpoint.getArgs();
		
		for(Object arg : args) {
			if(arg instanceof BindingResult) {
				BindingResult bindingResult = (BindingResult)arg;
				
				if(bindingResult.hasErrors()) {
					
					Map<String,String>errorMap = new HashMap<>();
					
					for(FieldError error : bindingResult.getFieldErrors()) {
						String validationkey = String.format("valid_%s", error.getField());
						
						log.info(typeName + "." + name + "() => 필드 : " + error.getField() + ", 메세지 : " + error.getDefaultMessage());
						
						errorMap.put(validationkey, error.getDefaultMessage());
					}
					
					return new Response<>(HttpStatus.BAD_REQUEST.value(),errorMap);
				}
			}
		}
		return proceedingjoinpoint.proceed();
	}
}
