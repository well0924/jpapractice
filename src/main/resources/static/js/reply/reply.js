/**
 * 게시글 댓글 기능
 */
 
 //댓글 목록 보이기.
$(document).ready(function(){
	getCommentlist();
});
//댓글 목록 출력하기.o.k
function getCommentlist(){
	
	let boardid = $('#boardid').val();
	let token = localStorage.getItem('Authorization');
	let result = JSON.parse(token);
	console.log(boardid);
	
	$.ajax({
		url:'/api/reply/'+boardid,
		headers:{'Authorization':'Bearer '+result.value},
		type:'get',
		dataType:'json',
		contentType:'application/json; charset=utf-8'
	}).done(function(resp){
	
		let html = "";
		var count = resp.data.length;
		console.log(count);
		console.log(resp.data);
		//게시물이 있는 경우
		if(resp.data.length > 0){
			for(let i = 0; i<resp.data.length; i++){

				 html +='<div class="commentArea" style="border-bottom:1px solid darkgray; margin-bottom: 15px;">';
				 	html +='<div class="commentInfo'+resp.data[i].replyId+'">';
					html +='작성자:'+resp.data[i].replyWriter;
				 	html +='</br>';
	             	html +='<div class="commentcontent'+resp.data[i].replyId+'">';
	             	html += '<p>내용:'+resp.data[i].replyContents+'</p>';
	             		html +='<div>';
	             			html +='<button type="button" class="btn btn-primary" onclick="commentDelete('+resp.data[i].replyId+')">'+'삭제'+'</button>';                
	             		html +='</div>';
	             	html +='</div>';
	             	html +='</div>';
	             html +='</div>';
	             html +='</br>';
			}	
		}else{
			 //댓글이 없는 경우
	         html += "<div class='mb-2'>";
             	html += "<h6><strong>등록된 댓글이 없습니다.</strong></h6>";
             html += "</div>";
		}
		
		$('#replies').html(html);
	}).fail(function(error){
		console.log(error);
	});
}

//댓글 작성 o.k
function replyInsert(){
	let token = localStorage.getItem('Authorization');
	let result = JSON.parse(token);
	let content=$('#replycontents').val();
	let boardId=$('#boardid').val();

	let data = {			
		replyContents : content,
		boardId : boardId
	}
	
	$.ajax({
		url:'/api/reply/'+boardId,
		type:'post',
		headers:{
			'Authorization':'Bearer '+result.value,
		},
		data:JSON.stringify(data),
		dataType:'json',
		contentType:'application/json; charset=utf-8'
	
	}).done(function(resp){
	
		if(resp.status == 200){
		
			alert('댓글이 작성 되었습니다.');	
			$('#replywriter').val(" ");
			$('#replycontents').val(" ");
			getCommentlist();
		}
		
		if(resp.status == 400){

			if(resp.data.hasOwnProperty('valid_replyContents')){
				$('#valid_replyContents').text(resp.data.valid_replyContents);
				$('#valid_replyContents').css('color','red');
			}else{
				$('#valid_replyContents').text('');
			}
		}
	});
}
//댓글 삭제o.k
function commentDelete(replyId){
	let token = localStorage.getItem('Authorization');
	let result = JSON.parse(token);
	let IsConfirm = confirm('삭제하겠습니까?');
	
	if(IsConfirm){
		$.ajax({
			url:'/api/reply/'+replyId,
			type:'delete',
			headers:{
				'Authorization':'Bearer '+result.value,
			},
			dataType:'json',
			contentType:'application/json; charset=utf-8'
			
		}).done(function(resp){
			console.log(resp);
			
			alert("삭제 되었습니다.");
			
			$('#replies').empty();
			
			getCommentlist();
		}).fail(function(error){
			
			console.log(error);
			$('#replies').empty();
			getCommentlist();
		});
	}
}
