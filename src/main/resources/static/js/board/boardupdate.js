/**
 * 
 */
 //글 수정기능
function modifypost(){
	
	let id = $('#boardid').val();
	let title = $('#boardtitle').val();
	let author = $('#boardauthor').val();
	let contents = $('#boardcontents').val();
	
	let date= {
			
			boardTitle : title,
			boardAuthor : author,
			boardContents : contents
	};
	
	$.ajax({
		
		url:'/api/board/update/'+id,
		type:'put',
		data:JSON.stringify(date),
		dataType:'json',
		contentType:'application/json; charset=utf-8'
	
	}).done(function(resp){
		console.log(resp);
		console.log(resp.status);
		console.log(resp.data);
		
		if(resp.status== 400){
			
			if(resp.data.hasOwnProperty('valid_boardTitle')){
				$('#valid_boardTitle').text(resp.data.valid_boardTitle);
				$('#valid_boardTitle').css('color','red');
			}else{
				$('#valid_boardTitle').text('');
			}
			if(resp.data.hasOwnProperty('valid_boardAuthor')){
				$('#valid_boardAuthor').text(resp.data.valid_boardAuthor);
				$('#valid_boardAuthor').css('color','red');
			}else{
				$('#valid_boardAuthor').text('');
			}
			if(resp.data.hasOwnProperty('valid_boardContents')){
				$('#valid_boardContents').text(resp.data.valid_boardContents);
				$('#valid_boardContents').css('color','red');
			}else{
				$('#valid_boardContents').text('');
			}
		}
		
		if(resp.status == 200){
			alert('글이 수정되었습니다.');
 			location.href='/page/board/list';	
		}
	
	});
}

//글삭제
function deletepost(){
	
	let id = $('#boardid').val();
	
	$.ajax({
		
		url:'/api/board/delete/'+id,
		type:'delete',
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