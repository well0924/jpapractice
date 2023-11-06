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
    }).done(function(data){
        //토큰을 파싱하고 로컬스토리지에 토큰값 저장하기.
        let tokenResult = tokenParse(data.accessToken);
        setItemWithExpireTime('Authorization',data.accessToken,tokenResult.exp);
        //토큰값 가져오기.
        let token = getItems();
        console.log(token);
        //권한에 따른 페이지 이동
        loginSuccessProc(token.value);
    }).fail(function (error) {
        console.log(error);
        alert("존재하지 않는 회원입니다.");
    });
}
