package co.kr.board.config.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	
	//common error
    INVALID_PARAMETER(400, "파라미터 값을 확인해주세요."),
    NOT_FOUND(404, "존재하않습니다."),
    INTERNAL_SERVER_ERROR(500, "서버 에러입니다. 서버 팀에 연락주세요!"),
    DB_ERROR(400,"데이터베이스에 문제가 발생했습니다."),
	ONLY_USER(403,"회원만 이용이 가능합니다."),
	
	//user error
	USERID_DUPLICATE(400,"회원 아이디가 중복이 됩니다."),
	USEREMAIL_DUPLICATE(400,"회원 이메일이 중복이 됩니다."),
	NOT_FINDUSERID(404,"회원아이디를 찾을 수가 없습니다."),
	NOT_USER(404,"회원이 존재하지 않습니다.")
	NOT_PASSWORD_MATCH(400,"비밀번호가 일치하지 않습니다."),
	
	//board error
	NOT_BOARDDETAIL(404,"작성한글을 찾을 수 없습니다."),
	BOARD_POST_DENIED(400,"작성할 권한이 없습니다."),
	BOARD_DELETE_DENIED(400,"삭제할 권한이 없습니다."),
	BOARD_EDITE_DENIED(400,"수정할 권한이 없습니다."),
	
	//comment error
	COMMENT_POST_DENINED(400,"작성할 권한이 없습니다."),
	COMMENT_EDITE_DENINED(400,"수정할 권한이 없습니다."),
	COMMENT_DELETE_DENIED(400,"삭제할 권한이 없습니다.");
		
	private final int status;

    private final String message;
}
