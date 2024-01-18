package co.kr.board.config.Redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableRedisRepositories
public class RedisConfig {
	
    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;
    @Value("${spring.redis.password}")
    private String password;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setPort(redisPort);
        redisStandaloneConfiguration.setHostName(redisHost);
        redisStandaloneConfiguration.setPassword(password);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }
    
    //redisTemplate을 사용
    @Bean
    public RedisTemplate<?,?> redisTemplate() {
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }
    
    //redisCache를 사용
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory){
        //redisCache설정
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration
                .defaultCacheConfig()
                .disableCachingNullValues()//null value는 캐시를 안함.
                .entryTtl(Duration.ofSeconds(CacheKey.DEFAULT_EXPIRE_SEC))//캐시의 기본 유효기간 설정
                .computePrefixWith(CacheKeyPrefix.simple())
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));//redis 캐시 설정 방식을 StringRedisSerializer으로 설정

        //캐시키별  default 유효기간 설정
        Map<String,RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        //캐시를 사용할 때 각각의 유효기간을 설정
        cacheConfigurations.put(CacheKey.USER, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(CacheKey.USER_EXPIRE_SEC)));
        cacheConfigurations.put(CacheKey.BOARD,RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(CacheKey.BOARD_EXPIRE_SEC)));
        cacheConfigurations.put(CacheKey.LIKE,RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(CacheKey.LIKE_EXPIRE_SEC)));
        cacheConfigurations.put(CacheKey.CATEGORY,RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(CacheKey.CATEGORY_EXPIRE_SEC)));

        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .withInitialCacheConfigurations(cacheConfigurations).build();
    }

}
