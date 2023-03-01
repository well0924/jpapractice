package co.kr.board.config.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import co.kr.board.config.Exception.dto.ErrorCode;
import co.kr.board.config.Exception.handler.CustomExceptionHandler;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RedisService {
	
	private final RedisTemplate<String, String> redisTemplate;
    //redis에 값저장
    public void setValues(String key, String data) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, data);
    }
    //redis에 값 꺼내기
    public String getValues(String key) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(key);
    }
    //redis에서 값 제거
    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }
    //refreshToken 값 확인하기.
    public void checkRefreshToken(String username, String refreshToken) {
        String redisRT = this.getValues(username);
        
        if(refreshToken.equals(redisRT)) {
            throw new CustomExceptionHandler(ErrorCode.valueOf("토큰이 만료되었습니다."));
        }
    }
	
}
