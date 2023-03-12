package co.kr.board.config.Exception.dto;

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
	NOT_SEARCH(400,"검색결과가 없습니다."),
	
	//user error
	USERID_DUPLICATE(400,"회원 아이디가 중복이 됩니다."),
	USER_EMAIL_DUPLICATE(400,"회원 이메일이 중복이 됩니다."),
	NOT_FIND_USERID(404,"회원아이디를 찾을 수가 없습니다."),
	NOT_USER(404,"회원이 존재하지 않습니다."),
	NOT_PASSWORD_MATCH(400,"비밀번호가 일치하지 않습니다."),
	
	//board error
	NOT_BOARD_DETAIL(404,"작성한글을 찾을 수 없습니다."),
	BOARD_POST_DENIED(400,"작성할 권한이 없습니다."),
	BOARD_DELETE_DENIED(400,"삭제할 권한이 없습니다."),
	BOARD_EDITE_DENIED(400,"수정할 권한이 없습니다."),
	
	//comment error
	COMMENT_POST_DENIED(400,"작성할 권한이 없습니다."),
	COMMENT_EDITE_DENIED(400,"수정할 권한이 없습니다."),
	COMMENT_DELETE_DENIED(400,"삭제할 권한이 없습니다."),

	//like error
	LIKE_NOT_FOUND(400,"좋아요를 찾을 수 없습니다."),

	//Category Error
	CANNOT_CONVERT_NESTED_STRUCTURED_EXCEPTION(500,"계층형으로 변환을 할 수 없습니다."),
	CATEGORY_NOT_FOUND(400,"카테고리를 찾을수 없습니다."),
	TOKEN_REFRESH_EXPIRED(500,"토큰이 만료되었습니다."),
	REFRESH_TOKEN_AUTHENTICATION_VALID(500,"토큰의 유저 정보가 일치하지 않습니다.");
		
	private final int status;

    private final String message;
}
