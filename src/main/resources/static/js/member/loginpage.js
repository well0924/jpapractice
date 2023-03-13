/*
*  로그인 페이지
 */

function loginproc(){
    let userid = $('#user_id').val();
    let userpw = $('#user_pw').val();

    let logindata = {
        username : userid,
        password : userpw
    }

    $.ajax({
        url:'/api/login/signup',
        type:'POST',
        data:JSON.stringify(logindata),
        dataTye:"json",
        contentType:'application/json; charset=utf-8'
    }).done(function(data){
        localStorage.setItem('X-AUTH-TOKEN',data.data.accessToken);
        localStorage.setItem('refreshToken',data.data.refreshToken);
        location.href='/page/main/mainpage';
    }).fail(function (error) {
        alert("존재하지 않는 회원입니다.");
    });
}