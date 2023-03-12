/**
 * 글 수정 페이지 js
 */
//글 수정기능
function modifypost(){
	let token = localStorage.getItem('X-AUTH-TOKEN');
	let refresh = localStorage.getItem('refreshToken');

	let id = $('#boardid').val();
	let title = $('#boardtitle').val();
	let contents = $('#boardcontents').val();

	let formdate = new FormData();
	
	let date= {
			boardTitle : title,
			boardContents : contents
	};
	
	formdate.append("boardupdate",new Blob([JSON.stringify(date)],{type:"application/json"}));
	
	$.ajax({
		
		url:'/api/board/update/'+id,
		type:'patch',
		data:formdate,
		headers: {
			'X-AUTH-TOKEN':token,
			'refreshToken':refresh
		},
		contentType: false,  
        processData: false,
        cache: false,
 		enctype: 'multipart/form-data',    
        dataType: "json",
	}).done(function(resp){
		console.log(resp);
		console.log(resp.status);
		console.log(resp.data);
		
		if(resp.status == 200){
			alert('글이 수정되었습니다.');
 			location.href='/page/board/list';	
		}
		
		if(resp.status== 400){
			
			if(resp.data.hasOwnProperty('valid_boardTitle')){
				$('#valid_boardTitle').text(resp.data.valid_boardTitle);
				$('#valid_boardTitle').css('color','red');
			}else{
				$('#valid_boardTitle').text('');
			}
			if(resp.data.hasOwnProperty('valid_boardContents')){
				$('#valid_boardContents').text(resp.data.valid_boardContents);
				$('#valid_boardContents').css('color','red');
			}else{
				$('#valid_boardContents').text('');
			}
		}
	
	});
}

//글삭제o.k
function deletepost(){
	let token = localStorage.getItem('X-AUTH-TOKEN');
	let refresh = localStorage.getItem('refreshToken');
	let id = $('#boardid').val();
	
	$.ajax({
		
		url:'/api/board/delete/'+id,
		type:'delete',
		headers:{
			'X-AUTH-TOKEN':token,
			'refreshToken':refresh
		},
		data: null,
		contentType:'application/json; charset=utf-8'
	
	}).done(function(resp){
		
		console.log(resp);
		console.log(resp.data);
		
		alert('글이 삭제되었습니다.');
		location.href='/page/board/list';
	
	}).fail(function(error){
		console.log(error);
	});
}

//글 목록
function mainpage(){
	location.href='/page/board/list';
}