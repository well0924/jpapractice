//비밀번호 재설정
function passwordSetting(){

    let userName = document.getElementById("userid").value;
    let password = document.getElementById("user_password").value;
    console.log(userName);
    console.log(password);
    const data = {password : password}
    $.ajax({
        url:'/api/member/password-change/'+userName,
        type:'put',
        data:JSON.stringify(data),
        dataType:'json',
        contentType:'application/json; charset=utf-8'
    }).done(function(resp){
        console.log(resp);
        alert("비밀번호가 변경되었습니다.");
        location.href='/page/login/login';
    });
}
