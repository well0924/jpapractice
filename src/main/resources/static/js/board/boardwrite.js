/**
 * 글 작성 페이지 js
 */
 
//메인페이지 이동
function main(){
	location.href="/page/board/list";
}
//토큰 재발급
function tokenReissue(){
	let token = localStorage.getItem('X-AUTH-TOKEN');
	let refreshToken = localStorage.getItem('refreshToken');

	const tokenData = {
		accessToken : token,
		refreshToken : refreshToken
	}

	$.ajax({
		url:'/api/login/reissue',
		type:'post',
		dataType: 'application/json; charset=utf-8',
		data: tokenData
	}).done(function(resp){
		localStorage.setItem('X-AUTH-TOKEN',resp.accessToken);
		localStorage.setItem('refreshToken',resp.refreshToken);
	});
}
//글 작성기능 o.k
function savepost(){
	let token = localStorage.getItem('X-AUTH-TOKEN');
	let refresh = localStorage.getItem('refreshToken');
	let title = $('#boardtitle').val();
	let contents = $('#boardcontents').val();
	let formdate = new FormData();
	let date = {boardTitle :title,boardContents : contents};
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
			url:'/api/board/write',
			type:'post',
			data: formdate,
			headers:{
				'X-AUTH-TOKEN':token,
				'refreshToken':refresh
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
