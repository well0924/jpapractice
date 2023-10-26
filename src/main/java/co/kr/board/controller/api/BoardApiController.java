package co.kr.board.controller.api;

import javax.validation.Valid;
import co.kr.board.config.redis.CacheKey;
import co.kr.board.domain.Dto.BoardDto;
import co.kr.board.domain.SearchType;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import co.kr.board.service.BoardService;
import co.kr.board.config.Exception.dto.Response;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Log4j2
@RestController
@AllArgsConstructor
@RequestMapping("/api/board/*")
public class BoardApiController {
	private final BoardService service;

	//게시글 목록(카테고리 + 페이징 + 정렬)
	@Secured({"ROLE_USER","ROLE_ADMIN"})
	@GetMapping("/list/{cname}")
	@ResponseStatus(code=HttpStatus.OK)
	public Response<Page<BoardDto.BoardResponseDto>>articleList(
			@PathVariable(value = "cname",required = true) String categoryName,
			@PageableDefault(sort="id",direction = Sort.Direction.DESC,size=10)Pageable pageable){
				
		Page<BoardDto.BoardResponseDto> list = service.findAllPage(pageable,categoryName);
		
		return new Response<>(HttpStatus.OK.value(),list);
	}

	//게시글 검색(페이징+정렬+검색)
	@Secured({"ROLE_USER","ROLE_ADMIN"})
	@GetMapping("/list/search")
	@ResponseStatus(code=HttpStatus.OK)
	public Response<Page<BoardDto.BoardResponseDto>>searchList(
			@PageableDefault(sort="id",direction = Sort.Direction.DESC,size=5)Pageable pageable,
			@RequestParam(required = false,value = "searchType") String searchType,
			@RequestParam(required = false,value = "searchVal")String searchVal){

		Page<BoardDto.BoardResponseDto>list = service.findAllSearch(searchVal, String.valueOf(SearchType.toSearch(searchType)),pageable);

		return new Response<>(HttpStatus.OK.value(),list);
	}
	
	//게시글 조회
	@GetMapping("/detail/{id}")
	@Secured({"ROLE_ADMIN,ROLE_USER"})
	@ResponseStatus(code=HttpStatus.OK)
	@Cacheable(value = CacheKey.BOARD,key = "#boardId",unless = "#result == null")
	public Response<BoardDto.BoardResponseDto> detailArticle(@PathVariable(value="id")Integer boardId){
		log.info("detail");
		BoardDto.BoardResponseDto detail = service.getBoard(boardId);
		return new Response<>(HttpStatus.OK.value(),detail);
	}

	//게시글 작성
	@PostMapping(value = "/write")
	@Secured({"ROLE_ADMIN,ROLE_USER"})
	@ResponseStatus(code = HttpStatus.CREATED)
	public Response<Integer>writeArticle(
			@RequestPart(value="filelist",required = false) List<MultipartFile> files,
			@Valid @RequestPart(value = "boardsave") BoardDto.BoardRequestDto dto,
			BindingResult bindingresult,
			@RequestParam(required = true,defaultValue = "2")int categoryId)throws Exception{

		//url: localhost:8085/api/board/write?categoryId=2
		int insertResult = service.boardsave(dto,categoryId,files);

		log.info("title: {},content: {},image:{}",dto.getBoardTitle(),dto.getBoardContents(),files);

		return new Response<>(HttpStatus.OK.value(),insertResult);
	}
	
	//게시글 삭제
	@DeleteMapping("/delete/{id}")
	@CacheEvict(value = CacheKey.BOARD)
	@Secured({"ROLE_ADMIN,ROLE_USER"})
	@ResponseStatus(code = HttpStatus.OK)
	public Response<?>deleteArticle(@PathVariable(value="id")Integer boardId)throws Exception{

		service.deleteBoard(boardId);
		return new Response<>(HttpStatus.OK.value(),200);
	}
	
	//게시글 수정
	@PatchMapping("/update/{id}")
	@Secured("{ROLE_ADMIN,ROLE_USER}")
	@ResponseStatus(code = HttpStatus.OK)
	public Response<?>updateArticle(
			@PathVariable(value="id")Integer boardId,
			@Valid @RequestPart(value = "boardupdate") BoardDto.BoardRequestDto dto, BindingResult bindingresult,
			@RequestPart(value = "filelist",required = false)List<MultipartFile>fileList)throws Exception{

		int updateResult = service.updateBoard(boardId, dto,fileList);

		return new Response<>(HttpStatus.OK.value(),updateResult);
	}
	
	//내가 작성한 글 확인하기.
	@GetMapping("/my-article/{id}")
	@Secured({"ROLE_ADMIN,ROLE_USER"})
	@ResponseStatus(code = HttpStatus.OK)
	public Response<?>memberArticle(@PathVariable("id") String username,Pageable pageable)throws Exception{
		Page<BoardDto.BoardResponseDto>list = service.memberArticle(username,pageable);
		return new Response<>(HttpStatus.OK.value(),list);
	}

	//최근에 작성한 글
	@GetMapping("/article-top5")
	@Secured({"ROLE_USER","ROLE_ADMIN"})
	public Response<?>findArticleTop5(){
		List<BoardDto.BoardResponseDto>list = service.findBoardTop5();
		return new Response<>(HttpStatus.OK.value(),list);
	}

	//게시물 선택 삭제
	@DeleteMapping("/select-delete")
	@Secured({"ROLE_ADMIN"})
	public Response<?>boardSelectDelete(@RequestBody List<String> boardId){
		service.boardSelectDelete(boardId);
		return new Response<>(HttpStatus.NO_CONTENT.value(),null);
	}

	//게시물 비밀번호 확인
	@GetMapping("/password-check/{password}/{id}")
	@Secured({"ROLE_ADMIN,ROLE_USER"})
	public Response<?>boardPasswordCheck(@PathVariable String password,@PathVariable Integer boardId)throws Exception{
		BoardDto.BoardResponseDto responseDto = service.passwordCheck(password,boardId);
		return new Response<>(HttpStatus.OK.value(),responseDto);
	}
}
