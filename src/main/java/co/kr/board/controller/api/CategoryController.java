package co.kr.board.controller.api;

import co.kr.board.domain.Dto.CategoryCreateRequest;
import co.kr.board.domain.Dto.CategoryDto;
import co.kr.board.service.CategoryService;
import co.kr.board.config.Exception.dto.Response;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryService categoryService;

    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public Response<List<CategoryDto>>categoryList(){
        List<CategoryDto>list = categoryService.categoryList();
        return new Response<>(HttpStatus.OK.value(),list);
    }

    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @PostMapping("/createcategory")
    @ResponseStatus(HttpStatus.CREATED)
    public Response<?> create(@RequestBody CategoryCreateRequest req){
        categoryService.create(req);
        return new Response<>(HttpStatus.OK.value(),200);
    }

    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @DeleteMapping("/deletecategory/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response<?>categorydelete(@PathVariable(required = true,value = "id") Integer id){
        categoryService.delete(id);
        return new Response<>(HttpStatus.OK.value(),200);
    }
}
