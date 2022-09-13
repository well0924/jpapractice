package co.kr.board.board.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.board.board.domain.dto.BoardRequestDto;
import co.kr.board.board.domain.dto.BoardResponseDto;
import co.kr.board.board.service.BoardService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/api/board/*")
@AllArgsConstructor
public class BoardRestController {
	
	private final BoardService service;
	
	
	@GetMapping("/list")
	public ResponseEntity<List<BoardResponseDto>>articlelist(@Valid BoardResponseDto dto)throws Exception{
		
		List<BoardResponseDto>list =null;
		try {
			list = service.findAll();
			log.info(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(list,HttpStatus.OK);
	}
	
	//글 작성
	@PostMapping("/write")
	public ResponseEntity<Integer>writeproc(@Valid @RequestBody BoardRequestDto dto )throws Exception{

		ResponseEntity<Integer>entity = null;

		int result = 0;

		try {

			result = service.boardsave(dto);
		
			entity = new ResponseEntity<Integer>(result,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return entity;
	}
	
	//글 조회
	@GetMapping("/detail/{id}")
	public ResponseEntity<BoardResponseDto> detailarticle(@PathVariable(value="id")Integer boardId)throws Exception{
		
		ResponseEntity<BoardResponseDto>entity = null;
		
		BoardResponseDto detail = null;
		
		try {	   
			
			detail = service.getBoard(boardId);
			
			if(detail != null) {
				entity = new ResponseEntity<BoardResponseDto>(detail,HttpStatus.OK);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return entity;
	}
	
	//글 삭제
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Integer>deletearticle(@PathVariable(value="id")Integer boardId )throws Exception{
		
		ResponseEntity<Integer> entity = null;		
		
		try {
			service.deleteBoard(boardId);
			entity = new ResponseEntity<Integer>(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return entity;
	}
	
}
