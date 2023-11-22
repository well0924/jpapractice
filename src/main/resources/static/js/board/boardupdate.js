/**
 * 글 수정 페이지 js
 */
//해시태그 입력
const hashtagsInput = document.getElementById("hashtagName");
const hashtagsContainer = document.getElementById("hashtags-container");
const hiddenHashtagsInput = document.getElementById("hashtags-hidden");
//해시태그 목록
let hashtags = [];

//해시태그 추가
function addHashtag(tag) {
	tag = tag.replace(/[\[\]]/g, '').trim();
	if(tag && !hashtags.includes(tag)) {
		const span = document.createElement("span");
		span.innerText = "#" + tag + " ";
		span.classList.add("hashtag");
		//제거 버튼을 생성
		const removeButton = document.createElement("button");
		removeButton.innerText = "x";
		removeButton.classList.add("remove-button");
		//제거버튼을 누르면 입력한 해시태그를 삭제
		removeButton.addEventListener("click", () => {
			hashtagsContainer.removeChild(span);
			hashtags = hashtags.filter((hashtag) => hashtag !== tag);
			hiddenHashtagsInput.value = hashtags.join(",");
		});

		span.appendChild(removeButton);
		hashtagsContainer.appendChild(span);
		hashtags.push(tag);
		hiddenHashtagsInput.value = hashtags.join(",");
	}
}

//해시태그에서 엔터를 누르면 해시태그값을 입력
hashtagsInput.addEventListener("keydown", (event) => {
	if (event.key === 'Enter') {
		event.preventDefault();
		const tag = hashtagsInput.value.trim();
		if (tag) {
			addHashtag(tag);
			hashtagsInput.value = "";
		}
	}
});
console.log(hashtags);

//글 수정기능
function modifypost(){
	let getToken = getItems();
	let token = getToken.value;
	let id = $('#boardid').val();
	let title = $('#boardtitle').val();
	let contents = $('#boardcontents').val();
	let categoryId = $('#categoryId').val();
	let password = $('#pw').val();
	let inputFiles = $('#files');
	let files = inputFiles[0].files;
	let filecount = 4;
	let formdate = new FormData();
	
	let date= {
			boardTitle : title,
			boardContents : contents,
			password : password,
			hashTagName:hashtags
	};

	if(files.length>filecount){
		alert('파일은 4개까지 입니다.');
		return false;
	}

	formdate.append("boardupdate",new Blob([JSON.stringify(date)],{type:"application/json"}));

	//첨부파일이 있는 경우 formdate에 추가.
	if(inputFiles != null){
		for(var i =0; i<files.lenght;i++){
			console.log(files[i]);
			formdate.append("image",files[i]);
		}
	}

	$.ajax({
		
		url:'/api/board/update/'+id,
		type:'patch',
		data:formdate,
		headers: {
			'Authorization':'Bearer '+token
		},
		contentType: false,  
        processData: false,
        cache: false,
 		enctype: 'multipart/form-data',    
        dataType: "json",
	}).done(function(resp){
		if(resp.status == 200){
			alert('글이 수정되었습니다.');
 			location.href='/page/board/list/freeboard';
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
	let token = localStorage.getItem('Authorization');
	let id = $('#boardid').val();
	
	$.ajax({
		
		url:'/api/board/delete/'+id,
		type:'delete',
		headers:{
			'Authorization':'Bearer '+token},
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