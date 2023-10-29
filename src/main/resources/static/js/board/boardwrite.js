/**
 * 글 작성 페이지 js
 */
 
//게시글 목록으로 이동하기.
function main(){
	let categoryName = "";
	location.href="/page/board/list/"+categoryName;
	history.back();
}

//글 작성기능 o.k
function savepost(){
	//로컬 스토리지에 저장된 at
	let token = localStorage.getItem('Authorization');
	let result = JSON.parse(token);
	let formdate = new FormData();

	let title = $('#boardtitle').val();
	let contents = $('#boardcontents').val();
	let categoryId = $('#categoryId').val();
	let password = $('#pw').val();
	let date = {boardTitle :title,boardContents : contents,password : password};

	let inputFiles = $('#attachfiles');
	let files = inputFiles[0].files;
	let filecount = 4;

	if(files.length>filecount){
		alert('파일은 4개까지 입니다.');
		return false;
	}

	if(inputFiles != null){
		for(var i =0; i<files.lenght;i++){
			console.log(files[i]);
			formdate.append("image",files[i]);
		}
	}

	formdate.append("boardsave",new Blob([JSON.stringify(date)], {type: "application/json"}));	
		
		$.ajax({		
			url:'/api/board/write?categoryId='+categoryId,
			type:'post',
			data: formdate,
			headers:{
				'Authorization': 'Bearer '+result.value,
			},
			contentType: false,  
            processData: false,
            cache: false,
     		enctype: 'multipart/form-data',    
            dataType: "json",
		}).done(function(resp){
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
				if(resp.status == 401){//인증이 안되는 경우
					tokenReissue();//토큰 재발급
				}
				if(resp.status == 200){
					alert('글이 작성되었습니다.');
		 			location.href='/page/board/list/freeboard';
				}
		});
}	
