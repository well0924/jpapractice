package co.kr.board.controller.api;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import co.kr.board.config.Exception.dto.ErrorCode;
import co.kr.board.config.Exception.handler.CustomExceptionHandler;
import co.kr.board.config.redis.CacheKey;
import co.kr.board.domain.Dto.BoardDto;
import co.kr.board.domain.Member;
import co.kr.board.repository.MemberRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import co.kr.board.service.BoardService;
import co.kr.board.config.Exception.dto.Response;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@Log4j2
@RestController
@AllArgsConstructor
@RequestMapping("/api/board/*")
public class BoardApiController {
	private final BoardService service;
	private final MemberRepository memberRepository;

	@GetMapping("/list/{cname}")
	@ResponseStatus(code=HttpStatus.OK)
	public Response<Page<BoardDto.BoardResponseDto>>articleList(
			@PathVariable(value = "cname",required = true) String categoryName,
			@PageableDefault(sort="id",direction = Sort.Direction.DESC,size=10)Pageable pageable){
				
		Page<BoardDto.BoardResponseDto> list = service.findAllPage(pageable,categoryName);
		
		return new Response<>(HttpStatus.OK.value(),list);
	}

	@GetMapping("/list/search")
	@ResponseStatus(code=HttpStatus.OK)
	public Response<Page<BoardDto.BoardResponseDto>>searchList(@PageableDefault(sort="id",direction = Sort.Direction.DESC,size=5)Pageable pageable,@RequestParam(required = false,value = "searchVal")String searchVal){

		Page<BoardDto.BoardResponseDto>list = service.findAllSearch(searchVal,pageable);

		return new Response<>(HttpStatus.OK.value(),list);
	}

	@GetMapping("/detail/{id}")
	@ResponseStatus(code=HttpStatus.OK)
	@Cacheable(value = CacheKey.BOARD,key = "#boardId",unless = "#result == null")
	public Response<BoardDto.BoardResponseDto> detailArticle(@PathVariable(value="id")Integer boardId){
		log.info("detail");
		BoardDto.BoardResponseDto detail = service.getBoard(boardId);
		return new Response<>(HttpStatus.OK.value(),detail);
	}

	@PostMapping(value = "/write")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Response<Integer>writeArticle(
			@RequestPart(value="filelist",required = false) List<MultipartFile> files,
			@Valid @RequestPart(value = "boardsave") BoardDto.BoardRequestDto dto,
			BindingResult bindingresult,
			@RequestParam(required = true,defaultValue = "2")int categoryId)throws Exception{

		//url: localhost:8085/api/board/write?categoryId=2
		Member member = getMember();
		int insertResult = service.boardsave(dto,member,categoryId,files);

		log.info("title: {},content: {},image:{}",dto.getBoardTitle(),dto.getBoardContents(),files);

		return new Response<>(HttpStatus.OK.value(),insertResult);
	}
	@DeleteMapping("/delete/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public Response<?>deleteArticle(@PathVariable(value="id")Integer boardId)throws Exception{
		Member member = getMember();
		service.deleteBoard(boardId,member);
		return new Response<>(HttpStatus.OK.value(),200);
	}
	
	@PatchMapping("/update/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public Response<?>updateArticle(
			@PathVariable(value="id")Integer boardId,
			@Valid @RequestBody BoardDto.BoardRequestDto dto, BindingResult bindingresult,
			@RequestPart(value = "filelist",required = false)List<MultipartFile>fileList)throws Exception{

		Member member = getMember();
		int updateResult = service.updateBoard(boardId, dto,member,fileList);

		return new Response<>(HttpStatus.OK.value(),updateResult);
	}

	@GetMapping("/download")
	public void download(HttpServletResponse response) throws IOException {

		String path = "C:\\upload\\\\825443066852500..png";

		byte[] fileByte = FileUtils.readFileToByteArray(new File(path));

		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode("tistory.png", "UTF-8")+"\";");
		response.setHeader("Content-Transfer-Encoding", "binary");

		response.getOutputStream().write(fileByte);
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}

	private Member getMember(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = (String)authentication.getPrincipal();
		Member member = memberRepository.findByUsername(username).orElseThrow(()->new CustomExceptionHandler(ErrorCode.NOT_FOUND));
		return member;
	}
}
