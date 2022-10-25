/**
 * 글 작성 페이지 js
 */
 
//메인페이지 이동
function main(){
	location.href="/page/board/list";
}

//파일첨부유효성검사
function fileCheck(){
	var filecount = 4;

	var inputFiles = $("input[name='filelist']");
	var files = inputFiles[0].files;
	
	console.log(inputFiles);
	console.log(files);
	
	if(files.length>filecount){
		alert('파일은 4개까지 입니다.');
		return false;
	}
	
	if(inputFiles != null){
		for(var i =0; i<files.lenght;i++){
			
			console.log(files[i]);
			
			formdate.append("filelist",files[i]);
		}
	}
	return true;
}

//글 작성기능 o.k
function savepost(){
	
	let title = $('#boardtitle').val();
	let contents = $('#boardcontents').val();
	
	let date = {
		"boardTitle":title,
		"boardContents":contents};
	
	var formdate = new FormData();
	
	formdate.append("jsonData",new Blob([JSON.stringify(date)],{type:"application/json"}));
	
	if(fileCheck()){
		$.ajax({		
			url:'/api/board/write',
			type:'post',
			data:formdate,
			processData: false,
			contentType : false,
			cache:false,
			enctype: 'multipart/form-data',
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
}