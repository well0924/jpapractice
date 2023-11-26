package co.kr.board.service;

import co.kr.board.config.Redis.CacheKey;
import co.kr.board.domain.Category;
import co.kr.board.domain.Dto.CategoryCreateRequest;
import co.kr.board.domain.Dto.CategoryDto;
import co.kr.board.repository.CategoryRepository;
import co.kr.board.config.Exception.dto.ErrorCode;
import co.kr.board.config.Exception.handler.CustomExceptionHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Log4j2
@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private StringRedisTemplate stringRedisTemplate;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    //카테고리 목록
    @Cacheable("category")
    public List<CategoryDto> categoryList() {
        List<CategoryDto> list = categoryRepository.categoryList();
        String cachedCategories = stringRedisTemplate.opsForValue().get(CacheKey.CATEGORY);
        if(cachedCategories!=null){
            log.info("목록::"+list);
            list = deserializeCategories(cachedCategories);
        }else{
            stringRedisTemplate.opsForValue().set(CacheKey.CATEGORY,serializeCategories(list));
        }
        return list;
    }

    //카테고리 등록
    @Transactional
    @Cacheable(value=CacheKey.CATEGORY,key = "#req.parentId",unless = "#result == null")
    public void create(CategoryCreateRequest req) {
        Category parent = Optional.ofNullable(req.getParentId())
                .map(id -> categoryRepository.findById(id)
                        .orElseThrow(()->new CustomExceptionHandler(ErrorCode.CATEGORY_NOT_FOUND)))
                .orElse(null);

        categoryRepository.save(new Category(req.getName(),parent));
    }

    //카테고리 삭제
    @Transactional
    @CacheEvict(value = CacheKey.CATEGORY,key = "#id")
    public void delete(Integer id) {
        Category category = categoryRepository
                .findById(id)
                .orElseThrow(()->new CustomExceptionHandler(ErrorCode.CATEGORY_NOT_FOUND));

        if(category!=null){
            categoryRepository.deleteById(id);
        }
    }

    private String serializeCategories(List<CategoryDto> categories) {
        try {
            return objectMapper.writeValueAsString(categories);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing categories", e);
        }
    }

    private List<CategoryDto> deserializeCategories(String cachedCategories) {
        try {
            TypeReference<List<CategoryDto>> typeReference = new TypeReference<List<CategoryDto>>(){};
            return objectMapper.readValue(cachedCategories, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing categories", e);
        }
    }
}
