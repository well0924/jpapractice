package co.kr.board.controller.api;

import javax.validation.Valid;
import co.kr.board.domain.Dto.BoardDto;
import co.kr.board.domain.Const.SearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import co.kr.board.service.BoardService;
import co.kr.board.config.exception.dto.Response;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Log4j2
@RestController
@AllArgsConstructor
@RequestMapping("/api/board")
public class BoardApiController {

	private final BoardService service;

	//게시글 목록(카테고리 + 페이징 + 정렬)
	@Secured({"ROLE_USER","ROLE_ADMIN"})
	@GetMapping("/{cname}")
	@ResponseStatus(HttpStatus.OK)
	public Response<Page<BoardDto.BoardResponseDto>>listBoard(
			@PathVariable(value = "cname") String categoryName,
			@PageableDefault(sort="id",direction = Sort.Direction.DESC)Pageable pageable){
				
		Page<BoardDto.BoardResponseDto> list = service.findAllPage(pageable,categoryName);
		
		return new Response<>(HttpStatus.OK.value(),list);
	}

	//게시글 검색(페이징+정렬+검색)
	@Secured({"ROLE_USER","ROLE_ADMIN"})
	@GetMapping("/search")
	@ResponseStatus(HttpStatus.OK)
	public Response<Page<BoardDto.BoardResponseDto>>searchBoard(
			@PageableDefault(sort="id",direction = Sort.Direction.DESC,size=5)Pageable pageable,
			@RequestParam(required = false,value = "searchType") String searchType,
			@RequestParam(required = false,value = "searchVal")String searchVal){

		Page<BoardDto.BoardResponseDto>list = service.findAllSearch(searchVal, String.valueOf(SearchType.toSearch(searchType)),pageable);

		return new Response<>(HttpStatus.OK.value(),list);
	}
	
	//게시글 조회
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Response<BoardDto.BoardResponseDto> findBoard(@PathVariable(value="id")Integer boardId){
		log.info("detail");
		BoardDto.BoardResponseDto detail = service.getBoard(boardId);
		return new Response<>(HttpStatus.OK.value(),detail);
	}

	//게시글 작성
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	@PostMapping(value = "/write")
	@ResponseStatus(HttpStatus.CREATED)
	public Response<Integer>createBoard(
			@RequestPart(value="filelist",required = false) List<MultipartFile> files,
			@Valid @RequestPart(value = "boardsave") BoardDto.BoardRequestDto dto,
			BindingResult bindingresult,
			@RequestParam(defaultValue = "2")int categoryId)throws Exception{

		//url: localhost:8085/api/board/write?categoryId=2
		int insertResult = service.boardCreate(dto,categoryId,files);

		log.info("title: {},content: {},image:{}",dto.getBoardTitle(),dto.getBoardContents(),files);

		return new Response<>(HttpStatus.CREATED.value(),insertResult);
	}
	
	//게시글 삭제
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Response<?>deleteBoard(@PathVariable(value="id")Integer boardId)throws Exception{
		service.boardDelete(boardId);
		return new Response<>(HttpStatus.NO_CONTENT.value(),200);
	}
	
	//게시글 수정
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	@PatchMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Response<?>modifyBoard(
			@PathVariable(value="id")Integer boardId,
			@Valid @RequestPart(value = "boardupdate") BoardDto.BoardRequestDto dto,
			BindingResult bindingresult,
			@RequestPart(value = "filelist",required = false)List<MultipartFile>fileList)throws Exception{

		int updateResult = service.updateBoard(boardId,dto,fileList);

		return new Response<>(HttpStatus.OK.value(),updateResult);
	}
	
	//내가 작성한 글 확인하기.
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	@GetMapping("/my-article/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Response<?>boardByMember(@PathVariable("id") String username,Pageable pageable){
		Page<BoardDto.BoardResponseDto>list = service.memberArticle(username,pageable);
		return new Response<>(HttpStatus.OK.value(),list);
	}

	//최근에 작성한 글
	@Secured({"ROLE_USER","ROLE_ADMIN"})
	@GetMapping("/article-top5")
	public Response<?>recentTop5Board(){
		List<BoardDto.BoardResponseDto>list = service.recentTop5Board();
		return new Response<>(HttpStatus.OK.value(),list);
	}

	//게시물 선택 삭제
	@Secured({"ROLE_ADMIN"})
	@PostMapping("/select-delete")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Response<List<String>>selectDeleteBoard(@RequestBody List<Integer> boardId){
		service.boardSelectDelete(boardId);
		return new Response<>(HttpStatus.NO_CONTENT.value(), null);
	}
	
	//게시물 비밀번호 확인
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	@GetMapping("/password-check/{password}/{id}")
	public Response<?>passwordCheckBoard(@PathVariable("password") String password,@PathVariable("id") Integer boardId){
		BoardDto.BoardResponseDto responseDto = service.passwordCheck(password,boardId);
		return new Response<>(HttpStatus.OK.value(),responseDto);
	}
	
	//게시글 비밀글로 전환 및 초기화
	@Secured({"ROLE_USER","ROLE_ADMIN"})
	@GetMapping("/change-board/{id}")
	public Response<?>changeSecretBoard(@PathVariable("id")Integer boardId) throws Exception {
		String result = service.checkPassword(boardId);
		log.info("result::"+result);
		if(result == null){//값이 null인 경우
			//4자리 비밀번호 발급 + 이메일 발송
			service.changeSecretBoard(boardId);
		} else {//비밀번호가 있는 경우
			service.passwordReset(boardId);
		}
		return new Response<>(HttpStatus.OK.value(),result);
	}

}
