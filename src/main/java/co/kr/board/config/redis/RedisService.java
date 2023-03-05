package co.kr.board.config.redis;

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
        
        if(refreshToken.equals(redisRT)) {
            throw new CustomExceptionHandler(ErrorCode.valueOf("토큰이 만료되었습니다."));
        }
    }
    public void setBlackList(String key, Object o, Long second) {
        stringRedisTemplate.opsForValue().set(REDIS_KEY_PREFIX + key, o.toString(), Duration.ofMillis(second));
    } //로그아웃시에 기존의 액세스 토큰을 로그아웃된 토큰으로 만료되었음을 표시하게 하기위한 메소드

    public boolean hasKeyBlackList(String key) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(REDIS_KEY_PREFIX + key));
    } //토큰이 탈취되어 잘못된 인증을 시도하는 접근이 있는지 확인하는 메소드

    public String getData(String key){
        ValueOperations<String,String> valueOperations = stringRedisTemplate.opsForValue();
        return valueOperations.get(key);
    } //레디스에 저장되어있는 데이터를 가져오는 메소드

    public void setDataExpire(String key,String value,long duration){
        ValueOperations<String,String> valueOperations = stringRedisTemplate.opsForValue();
        Duration expireDuration = Duration.ofMillis(duration);
        valueOperations.set(key,value,expireDuration);
    } //이미 생성되어있거나 생성되어있지 않은 데이터를 만료기한까지 설정해 저장하는 메소드
}
