package co.kr.board.controller.api;

import co.kr.board.domain.Dto.CategoryCreateRequest;
import co.kr.board.domain.Dto.CategoryDto;
import co.kr.board.service.CategoryService;
import co.kr.board.config.exception.dto.Response;
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
    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public Response<List<CategoryDto>>categoryList(){
        List<CategoryDto>list = categoryService.categoryList();
        return new Response<>(HttpStatus.OK.value(),list);
    }

    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Response<?> createCategory(@RequestBody CategoryCreateRequest req){
        categoryService.createCategory(req);
        return new Response<>(HttpStatus.CREATED.value(),200);
    }

    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response<?>deleteCategory(@PathVariable(required = true,value = "id") Integer id){
        categoryService.deleteCategory(id);
        return new Response<>(HttpStatus.OK.value(),200);
    }
}
