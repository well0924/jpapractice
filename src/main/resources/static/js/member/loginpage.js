/*
*  로그인 페이지
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
        console.log(data.accessToken);
        console.log(xhr);
        console.log(request.getResponseHeader('Authorization'));
        let ttl = new Date(1698604892);
        //at를 로컬스토리지에 저장하기.
        setItemWithExpireTime('Authorization',data.accessToken,ttl);
        //토큰을 파싱
        let tokenId = localStorage.getItem('Authorization');
        //토큰값이 있다면 파싱후 권한에 맞는 페이지로 이동하기.
        if(tokenId){
            var base64Url = tokenId.split('.')[1];
            var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
            var jsonPayload = decodeURIComponent(window
                .atob(base64)
                .split('')
                .map(function(c) {
                    return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);}).join(''));

            var result = JSON.parse(jsonPayload);
            console.log(result);
            console.log(result.role);
            //권한에 맞게 분기 처리.
            if(result.role === 'ROLE_ADMIN'){
                console.log('관리자')
                //관리자로 로그인을 할 경우에는 어드민 페이지 활성화, 로그인 비활성화
                //관리자 페이지 활성화
                location.href='/page/member/adminlist';
            }else if(result.role === 'ROLE_USER'){
                console.log('회원')
                location.href='/page/main/mainpage';
            }else{
                console.log('익명');
            }
        }
        //location.href='/page/main/mainpage';
    }).fail(function (error) {
        console.log(error);
        alert("존재하지 않는 회원입니다.");
    });
}
//로컬스토리지에 저장하기.(기간 포함)
function setItemWithExpireTime(keyName,keyValue,ttl){
    //로컬스토리지에 저장할 객체
    const obj = {
        value : keyValue,
        expire : Date.now() + ttl
    }
    // 객체를 JSON 문자열로 변환
    const objString = JSON.stringify(obj);

    // setItem
    window.localStorage.setItem(keyName, objString);
}