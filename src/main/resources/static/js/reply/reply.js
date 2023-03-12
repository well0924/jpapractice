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

		//게시물이 있는 경우
		if(resp.data.length > 0){
			for(let i = 0; i<resp.data.length; i++){
				 
				 html +='<div class="commentArea" style="border-bottom:1px solid darkgray; margin-bottom: 15px;">';
				 	html +='<div class="commentInfo'+resp.data[i].replyId+'">';
				 	html +='댓글번호:'+resp.data[i].replyId+'/작성자:'+resp.data[i].replyWriter;
				 	html +='</br>';
	             	html +='<div class="commentcontent'+resp.data[i].replyId+'">';
	             	html += '<p>내용:'+resp.data[i].replyContents+'</p>';
	             		html +='<div>';
	             			html +='<button type="button" class="btn btn-primary" onclick="commentDelete('+resp.data[i].replyId+')">'+'삭제'+'</button>';                
	             		html +='</div>';
	             	html +='</div>';
	             	html +='</div>';
	             html +='</div>';
	             html +='<hr>';
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
	let token = localStorage.getItem('X-AUTH-TOKEN');
	let content=$('#replycontents').val();
	let boardid=$('#boardid').val();

	let data = {			
		replyContents : content,
		boardId : boardid
	}
	
	$.ajax({
		url:'/api/reply/write/'+boardid,
		type:'post',
		headers:{
			'X-AUTH-TOKEN':token,
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
	let token = localStorage.getItem('X-AUTH-TOKEN');
	let Isconfirm = confirm('삭제하겠습니까?');
	
	if(Isconfirm){
		$.ajax({
			url:'/api/reply/delete/'+replyId,
			type:'delete',
			headers:{
				'X-AUTH-TOKEN':token,
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

//댓글 내용 변경
function commentUpdate(replyId,replyContents){
	console.log("????????");
	var html = '';
	
	html +='<div class="commentcontent'+replyId+'">'; 
     	html += '<input type="text" name="replyContents" id="contents"></input>';
 		html +='<div>';
 			html +='<button type="button" class="btn btn-primary" onclick="commentUpdate('+replyId+',\''+replyContents+'\')";>'+'수정'+'</button>';	                
 		html +='</div>';
	html +='</div>';               
	             	

	$('.commentconetent'+replyId).html(html);	
}
//댓글 수정기능
function commentUpdateProc(replyId){
	console.log("??");	
	
	let contents = $('[name=content_'+replyId+']').val();
	const data = {replyContents : contents, replyId : replydId};
	$.ajax({
		url:'/api/reply/update/'+replyId,
		type:'put',
		data:JSON.stringify(data),
		dataType:'json',
		contentType:'application/json; charset=utf-8'
	}).done(function(resp){
		console.log(resp);
		alert('댓글이 수정 되었습니다.');
		getCommentlist();
	});
}