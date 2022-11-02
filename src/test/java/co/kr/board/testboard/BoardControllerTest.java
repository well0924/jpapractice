package co.kr.board.testboard;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import co.kr.board.board.controller.BoardApiController;

@ExtendWith(MockitoExtension.class)
public class BoardControllerTest {
	
	@InjectMocks
	BoardApiController boardcontroller;
	
	
}
