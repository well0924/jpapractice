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
$(document).ready(function(){
	//좋아요 갯수
	likeCount();
});
//좋아요 갯수
function likeCount(){
	let id = $('#boardid').val();
	let token = localStorage.getItem('Authorization');
	let result = JSON.parse(token);
	$.ajax({
		url:'/api/like/count/'+id,
		type:'get',
		dataTye: 'json',
		contentType: 'application/json; charset = utf-8',
		headers:{'Authorization': 'Bearer '+result.value}
	}).done(function(resp){
		console.log(resp.data);

		$('#likecount').html(resp.data);
	});
}
//좋아요 중복기능
function addDuplicatedLike(){
	let id = $('#boardid').val();
	let token = localStorage.getItem('Authorization');
	let result = JSON.parse(token);
	$.ajax({
		url:'/api/like/duplicated/'+id,
		type:'get',
		headers:{'Authorization':  'Bearer '+result.value},
		contentType:'application/json; charset = utf-8',
		dataTye: 'json'
	}).done(function(resp){
		console.log(resp);
		console.log(resp.data);
		if(resp.data== true){
			alert("좋아요 취소");
		}
		if(resp.data== false){
			alert("좋아요 추가");
		}
	});
}