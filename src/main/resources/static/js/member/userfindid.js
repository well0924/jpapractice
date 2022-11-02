/**
 * 회원 아이디찾기 페이지
 */
 
 //유효성 검사
function validation(){
	let name = $('#username').val();
	let email = $('#user_email').val();
	
	if(name.trim().length == 0){
		alert('이름을 입력해주세요.');
		return false;
	}
	
	if(email.trim().lenght == 0){
		alert('이메일을 입력해주세요.');
		return false;
	}
	return true;
}
//회원아이디 찾기
function finduserid(){
	let name = $('#username').val();
	let email = $('#user_email').val();
	
	let data ={
			membername : name, 
			useremail : email
	};
	
	if(validation()){
		$.ajax({
		
			url:'/api/login/userfind/'+name+'/'+email,
			type:'post',
			data:JSON.stringify(data),
			dataType:'json',
			contentType:'application/json; charset = utf-8'	
		
		}).done(function(resp){
			
			console.log(resp);
			console.log(resp.status);
			console.log(resp.data);
			
			if(resp.status == 400){
				
				if(resp.data.hasOwnProperty('valid_membername')){
					$('#valid_username').text(resp.data.valid_membername);
					$('#valid_username').css('color','red');
				}else{
					$('#valid_username').text('');
				}
				if(resp.data.hasOwnProperty('valid_useremail')){
					$('#valid_useremail').text(resp.data.valid_useremail);
					$('#valid_useremail').css('color','red');
				}else{
					$('#valid_useremail').text('');
				}
			}
			
			if(resp.status ==200){
				console.log(resp);
				$('#finduserid').text('아이디는'+resp.data+'입니다.');
				$('#finduserid').css('color','blue');
			}else{
				$('#finduserid').text('아이디가 존재하지 않습니다.');
				$('#finduserid').css('color','red');
			}
		});
	}	
}