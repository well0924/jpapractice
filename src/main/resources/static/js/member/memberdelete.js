/**
 * 회원탈퇴 페이지
 */

//회원삭제(탈퇴)기능
function memberdelete(){
	
	let id = $('#deleteid').val();
	
	const isConfirm = confirm('탈퇴하시겠습니까?');
	
	if(isConfirm){
		$.ajax({
			url:'/api/member/'+id+'/member',
			type:'delete',
			data:null,
			contentType:'application/json; charset= utf-8'
		}).done(function(resp){
			alert('탈퇴되었습니다.');
			location.href='/page/board/list';
		});	
	}
}  