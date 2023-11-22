/**
 *  글 상세 조회 페이지
 */

//목록가기o.k
function listpage(){
	location.href='/page/board/list/freeboard';
}
//수정페이지 o.k
function modifypage(){
	
	let id = $('#boardid').val();
	let pwd = $('#board-password').val();
	//비밀번호가 있으면 비밀번호 확인 부분으로 없는 경우에는 게시글 수정화면으로 이동하기.
	if(pwd != null){
		location.href='/page/board/passwordcheck';
	}else{
		location.href='/page/board/modify/'+id;
	}
}
