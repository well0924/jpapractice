package co.kr.board.board.controller;


import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

import co.kr.board.board.domain.Board;
import co.kr.board.board.domain.dto.BoardDto;
import co.kr.board.board.service.BoardService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/board/*")
public class BoardRestController {
	
	private final BoardService service;
		
	@GetMapping("/list")
	public ResponseEntity<Page<Board>>articlelist(@PageableDefault(sort="boardId",direction = Sort.Direction.DESC,size=5)Pageable pageable)throws Exception{
		
		ResponseEntity<Page<Board>>entity = null;
		
		Page<Board>list =null;
		
		try {
			list = service.findAll(pageable);
			
			if(list != null) {
			
				entity= new ResponseEntity<Page<Board>>(list,HttpStatus.OK);
			
			}else {
			
				entity= new ResponseEntity<Page<Board>>(HttpStatus.BAD_REQUEST);
			
			}
		} catch (Exception e) {
			e.printStackTrace();
			entity= new ResponseEntity<Page<Board>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return entity;
	}
	
	@PostMapping("/write")
	public ResponseEntity<Integer>writeproc(@Valid @RequestBody BoardDto.BoardRequestDto dto)throws Exception{

		ResponseEntity<Integer>entity = null;

		int result = 0;

		try {
				result = service.boardsave(dto);
				
				if(result > 0) {
					
					entity = new ResponseEntity<Integer>(result,HttpStatus.OK);
				
				}else if(result < 0) {
					
					entity = new ResponseEntity<Integer>(result,HttpStatus.BAD_REQUEST);
				}
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<Integer>(result,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return entity;
	}
	
	@GetMapping("/detail/{id}")
	public ResponseEntity<BoardDto.BoardResponseDto> detailarticle(@PathVariable(value="id")Integer boardId)throws Exception{
		
		ResponseEntity<BoardDto.BoardResponseDto>entity = null;
		
		BoardDto.BoardResponseDto detail = null;
		
		try {	   
			
			detail = service.getBoard(boardId);
			
			if(detail != null) {
				entity = new ResponseEntity<BoardDto.BoardResponseDto>(detail,HttpStatus.OK);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<BoardDto.BoardResponseDto>(detail,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return entity;
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Integer>deletearticle(@PathVariable(value="id")Integer boardId )throws Exception{
		
		ResponseEntity<Integer> entity = null;		
		
		try {
			service.deleteBoard(boardId);
			entity = new ResponseEntity<Integer>(HttpStatus.OK);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			entity = new ResponseEntity<Integer>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return entity;
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<Integer>updatearticle(@PathVariable(value="id")Integer boardId,@Valid @RequestBody BoardDto.BoardRequestDto dto)throws Exception{
		
		ResponseEntity<Integer>entity = null;
		
		int result = 0;
		
		try {
			result = service.updateBoard(boardId, dto);
			
			if(result > 0) {
			
				entity = new ResponseEntity<Integer>(result ,HttpStatus.OK);
			
			}else {
			
				entity = new ResponseEntity<Integer>(result,HttpStatus.BAD_REQUEST);
			
			}
		} catch (Exception e) {
			
			e.printStackTrace();
			entity = new ResponseEntity<Integer>(result,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return entity;
	}
}
