package co.kr.board.config.redis;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import co.kr.board.config.Exception.dto.ErrorCode;
import co.kr.board.config.Exception.handler.CustomExceptionHandler;
import lombok.AllArgsConstructor;

import java.time.Duration;

@Service
@AllArgsConstructor
public class RedisService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    private final String REDIS_KEY_PREFIX = "LOGOUT_";
    private final String EXPIRED_DURATION = "EXPIRE_DURATION";
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

        if(!refreshToken.equals(redisRT)) {
            throw new CustomExceptionHandler(ErrorCode.TOKEN_REFRESH_EXPIRED);
        }
    }
}
