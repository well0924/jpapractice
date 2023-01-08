package co.kr.board.redis;

import co.kr.board.config.redis.RedisService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("redisService 테스트 코드")
public class RedisCrudTest {
    @Autowired
    private RedisService redisService;

    @Test
    @DisplayName("redis crud test -create")
    public void InsertTest(){
        //given
        String key = "rediskey";
        String value = "testcreated";
        //when
        redisService.setValues(key,value);
        //then
        assertThat(value).isEqualTo("testcreated");
    }

    @Test
    @DisplayName("redis crud test -read")
    public void readTest(){
        String key = "rediskey";
        String value = "testcreated";

        String result = redisService.getValues(key);

        assertThat(result).isEqualTo(value);
    }

    @Test
    @DisplayName("redis crud test -delete")
    public void DeleteTest(){
        String key = "rediskey";
        redisService.deleteValues(key);
        assertThat(key).isNotNull();
    }
}
