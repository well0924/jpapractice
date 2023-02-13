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
	
	let formdate = new FormData();
	
	let date = {boardTitle :title,boardContents : contents};

	var inputFiles = $('#attachfiles');

	var files = inputFiles[0].files;

	let filecount = 4;

	console.log(inputFiles);
	console.log(files);

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
				
				if(resp.status == 200){
					alert('글이 작성되었습니다.');
		 			location.href='/page/board/list';	
				}
		});
}	
