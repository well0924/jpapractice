/**
 * 회원가입 페이지
 */

//아이디 중복 o.k
function usernameCheck(){
	let userid = $('#userId').val();
	console.log(userid);
		$.ajax({
			url:'/api/member/login-check/'+ userid,
			type:'get',
			dataType:'json',
			contentType:'application/json; charset = utf-8'	
		}).done(function(resp){
			console.log(resp.data);
			console.log(resp);
			
			if(resp.data == true){
				$('#duplicatedid').text('아이디 중복!');
				$('#duplicatedid').css('color','red');
			}else{
				$('#duplicatedid').text('사용가능!');
				$('#duplicatedid').css('color','blue');
			}					
		});	
}

//아이디 재입력 o.k
function reUsername(){
	let id =$('#userId').val('');
	$('#duplicatedid').val('');
}

function pwSame(){
	let pwd = $('#pwd').val();
	let pwdcheck = $('#pwd_check').val();
	
	if(pwd.trim()==pwdcheck.trim()){
		$('#pw_check_msg').text('비밀번호가 일치 합니다.');	
		$('#pw_check_msg').css('color','blue');
	}else{
		$('#pw_check_msg').text('비밀번호가 일치하지 않습니다.');	
		$('#pw_check_msg').css('color','red');
	}
}
//회원이메일 중복 확인
function useremailCheck(){
	let email = $('#userEmail').val();
	
	$.ajax({
		url:'/api/member/email-check/'+email,
		type:'get',
		dataType:'json',
		contentType:'application/json; charset = utf-8'
	}).done(function(resp){
		console.log(resp);
		console.log(resp.data);
		
		if(resp.data == true){
			$('#duplicatedemail').text('사용가능!');
			$('#duplicatedemail').css('color','blue');
		}
		if(resp.data == false){
			$('#duplicatedemail').text('이메일 중복!');
			$('#duplicatedemail').css('color','red');
		}
	});		
}

//회원이메일
function reUseremail(){
	let email = $('#userEmail').val('');
	$('#duplicatedemail').val('');
}

//회원가입o.k
function signUp(){
	let id= $('#userId').val();
	let pw= $('#pwd').val();
	let email = $('#userEmail').val();
	let name = $('#memberName').val();
	
	const joindate = {
			username : id,
			password : pw,
			memberName : name,
			userEmail : email
	};
	
	$.ajax({
		url:'/api/member/create',
		type:'post',
		data:JSON.stringify(joindate),
		dataType:'json',
		contentType:'application/json; charset = utf-8'
	}).done(function(resp){
		console.log(resp);
		console.log(resp.data);
		
		if(resp.status == 400){
			
			if(resp.data.hasOwnProperty('valid_username')){
				$('#valid_userid').text(resp.data.valid_username);
				$('#valid_userid').css('color','red');
			}else{
				$('#valid_userid').text('');
			}
			if(resp.data.hasOwnProperty('valid_password')){
				$('#valid_password').text(resp.data.valid_password);
				$('#valid_password').css('color','red');
			}else{
				$('#valid_password').text('');
			}
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
		}else if(resp.status == 200){
			alert('회원가입이 되었습니다.');
 			location.href='/page/main/mainpage';
		}
	});
}