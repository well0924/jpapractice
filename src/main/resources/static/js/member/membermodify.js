/**
 * 회원 수정 페이지 및 내 정보 페이지
 */
//회원 수정
function memberupdate(){
	
	let idx = $('#userno').val();
	let id = $('#user_id').val();
	let pwd = $('#user_pw').val();
	let name = $('#user_name').val();
	let email = $('#user_email').val();
	
	let memberdate={
		username : id,
		userpw : pwd,
		membername : name,
		useremail : email
	}
	
	$.ajax({
		url:'/api/login/memberupdate/'+idx+'/member',
		type:'put',
		data:JSON.stringify(memberdate),
		dataType:'json',
		contentType:'application/json; charset=utf-8'
	}).done(function(resp){
		alert('정보가 수정이 되었습니다.');
		location.href='/page/board/list';
	});
}
//회원 탈퇴
function memberdelete(){
	
	let idx = $('#userno').val();
	
	$.ajax({
		url:'/api/login/memberdelete/'+idx+'/member',
		type:'delete',
		data:null,
		contentType:'application/json; charset=utf-8'
	}).done(function(resp){
		alert("정보가 삭제 되었습니다.");
		location.href='/page/board/list';
	});
}
//회원 목록
function adminlist(){
	location.href='/page/login/adminlist';
}
 