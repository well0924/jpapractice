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
	
	location.href='/page/board/modify/'+id;
}
