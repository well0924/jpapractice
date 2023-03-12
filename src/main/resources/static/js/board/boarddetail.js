/**
 *  글 상세 조회 페이지
 */

//목록가기o.k
function listpage(){
	location.href='/page/board/list';
}

//수정페이지 o.k
function modifypage(){
	
	let id = $('#boardid').val();
	
	location.href='/page/board/modify/'+id;
}

//좋아요 중복기능
function addDuplicatedLike(){
	let id = $('#boardid').val();
	let token = localStorage.getItem('X-AUTH-TOKEN');
	let refreshToken = localStorage.getItem('refreshToken');

	$.ajax({
		url:'/api/like/duplicated/'+id,
		type:'get',
		headers:{
			'X-AUTH-TOKEN':token,
			'refreshToken':refreshToken
		},
		contentType:'application/json; charset = utf-8',
		dataTye: 'json'
	}).done(function(resp){
		console.log(resp);
		console.log(resp.data);
		if(resp.data==true){
			alert('plus');
			addLike(id);
		}
		if(resp.data==false){
			alert('minus');
			minusLike(id);
		}
	});
}

//좋아요 +1
function addLike(id){
	let token = localStorage.getItem('X-AUTH-TOKEN');
	let refreshToken = localStorage.getItem('refreshToken');

	$.ajax({
		url:'/api/like/plus/'+id,
		type: 'post',
		headers: {
			'X-AUTH-TOKEN':token,
			'refreshToken':refreshToken
		},
		data:{boardId : id},
		dataTye: 'json',
		contentType: 'application/json; charset = utf-8'
	}).done(function(resp){
		alert('좋아요가 추가되었습니다.');
	});
}
//좋아요 -1
function minusLike(id){
	let boardId = $('#boardid').val();
	let token = localStorage.getItem('X-AUTH-TOKEN');
	let refreshToken = localStorage.getItem('refreshToken');
	$.ajax({
		url:'/api/like/minus/'+id,
		type:'post',
		data: {boardId:id},
		dataTye:'json',
		contentType:'application/json; charset=utf-8'
	}).done(function(resp){
		alert('좋아요가 취소 되었습니다.');
	});
}