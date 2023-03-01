package co.kr.board.category.controller;

import co.kr.board.category.domain.dto.CategoryCreateRequest;
import co.kr.board.category.domain.dto.CategoryDto;
import co.kr.board.category.service.CategoryService;
import co.kr.board.config.Exception.dto.Response;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public Response<List<CategoryDto>>categoryList()throws Exception{
        List<CategoryDto>list = categoryService.categoryList();
        return new Response<>(HttpStatus.OK.value(),list);
    }

    @PostMapping("/createcategory")
    @ResponseStatus(HttpStatus.CREATED)
    public Response<?> create(@RequestBody CategoryCreateRequest req){
        categoryService.create(req);
        return new Response<>(HttpStatus.OK.value(),200);
    }

    @DeleteMapping("/deletecategory/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response<?>categorydelete(@PathVariable(required = true) Integer id){
        categoryService.delete(id);
        return new Response<>(HttpStatus.OK.value(),200);
    }
}
