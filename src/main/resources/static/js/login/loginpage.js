/*
*  로그인 기능
*/
function loginproc(){
    let userid = $('#user_id').val();
    let userpw = $('#user_pw').val();

    let logindata = {
        username : userid,
        password : userpw
    };

    $.ajax({
        url:'/api/login/signup',
        type:'POST',
        data:JSON.stringify(logindata),
        dataTye:"json",
        contentType:'application/json; charset=utf-8'
    }).done(function(data,xhr,request){
        console.log(data);
        console.log(request.getResponseHeader('Authorization'));
        let ttl = new Date(1698604892);
        //at를 로컬스토리지에 저장하기.
        setItemWithExpireTime('Authorization',data.accessToken,ttl);
        let token = localStorage.getItem('Authorization');
        loginSuccessProc(token);
    }).fail(function (error) {
        console.log(error);
        alert("존재하지 않는 회원입니다.");
    });
}
