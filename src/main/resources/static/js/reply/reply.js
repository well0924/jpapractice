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
	
	console.log(boardid);
	
	$.ajax({
	
		url:'/api/reply/list/'+boardid,
		type:'get',
		dataType:'json',
		contentType:'application/json; charset=utf-8'
	
	}).done(function(resp){
	
		let html = "";
		
		var count = resp.data.length;
	
		console.log(resp);
		console.log(count);
		
		//게시물이 있는 경우
		if(resp.data.length > 0){
			for(let i = 0; i<resp.data.length; i++){
				 
				html += "<br>";
				html += "<div class='mb-2'>";
                	html += "<input type='hidden' id='commentId_"+ resp.data[i].replyId +"' value='" + resp.data[i].replyId + "'>"
                	html += "<span style='float:right;' align='right' id='commentDate_"+ resp.data[i].replyId +"'> " + resp.data[i].createdAt + " </span>";
                	html += "<div class='mb-1 comment_container' >"
                		html += "<h5 id='commentText_" + resp.data[i].replyId + "' style='display: inline'>" + resp.data[i].replyWriter +"</h5>";
                		html += "<h5 id='commentText_" + resp.data[i].replyId + "' style='display: inline'>" + resp.data[i].replyContents +"</h5>";
                	html += "</div>"
                	html +='<button type="button" class="btn btn-primary" onClick="replyUpdate('+resp.data[i].replyId+'\)">'+'수정'+'</button>';
                	html +='<button type="button" class="btn btn-primary" onClick="replyDelete('+resp.data[i].replyId+'\)">'+'삭제'+'</button>';
                	html += "<hr>";
                html += "</div>"
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
	
	let content=$('#replycontents').val();
	let boardid=$('#boardid').val();

	let data = {			
		replyContents : content,
		boardId : boardid
	}
	
	$.ajax({
	
		url:'/api/reply/write/'+boardid,
		type:'post',
		data:JSON.stringify(data),
		dataType:'json',
		contentType:'application/json; charset=utf-8'
	
	}).done(function(resp){
	
		console.log(resp);
		console.log(resp.status);
		console.log(resp.data);
	
		if(resp.status == 200){
		
			alert('댓글이 작성 되었습니다.');	
			$('#replywriter').val(" ");
			$('#replycontents').val(" ");
			getCommentlist();
		}
		
		if(resp.status == 400){
			console.log("valid!");
			
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
function replyDelete(replyId){
	
	let Isconfirm = confirm('삭제하겠습니까?');
	
	if(Isconfirm){
		$.ajax({
			url:'/api/reply/delete/'+replyId,
			type:'delete',
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
