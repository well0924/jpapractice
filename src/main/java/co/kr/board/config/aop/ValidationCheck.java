package co.kr.board.config.aop;

import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

@Service
@Component
public class ValidationCheck {
	
	//유효성 체크용	
	@Transactional
    public Map<String, String> validateHandling(Errors errors) {
		
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {
        	
            String validKeyName = String.format("valid_%s", error.getField());
            
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }

        return validatorResult;
    }
}
