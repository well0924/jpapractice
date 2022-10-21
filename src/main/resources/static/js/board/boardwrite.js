/**
 * 글 작성 페이지 js
 */
 
//메인페이지 이동
function main(){
	location.href="/page/board/list";
}

//글 작성기능 o.k
function savepost(){
	
	let title = $('#boardtitle').val();
	let contents = $('#boardcontents').val();
	
	let date= {
			boardTitle : title,
			boardContents : contents
	};
		
		$.ajax({
		
			url:'/api/board/write',
			type:'post',
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
				if(resp.data.hasOwnProperty('valid_boardContents')){
					$('#valid_boardContents').text(resp.data.valid_boardContents);
					$('#valid_boardContents').css('color','red');
				}else{
					$('#valid_boardContents').text('');
				}
			}
			
			if(resp.status == 200){
				alert('글이 작성되었습니다.');
	 			location.href='/page/board/list';	
			}
		
		});	
}