/*
 * 로그인후 권한에 따라 화면 변환
 */
window.onload= function (){
    //로컬스토리지에 토큰값이 있는 경우
    let tokenId = localStorage.getItem('Authorization');

    let result = tokenParse(tokenId);

    if(tokenId){
        console.log(result);

        let username = result.userId;
        let role = result.role;
        let expire = result.exp;
        let expireTime = new Date(expire *1000);

        console.log("아이디:"+username);
        console.log("권한:"+role);
        console.log("유효기간:"+expire);
        console.log(expireTime);

        //1분마다 주기적으로 유효기간을 검증한다.
        setInterval(function(){validTokenExpiredTime(expire)},60000);

        if(role === 'ROLE_ADMIN'){
            console.log('관리자');
            $(".userId").text(username + "님 환영합니다.");
            $('.admin-token').css("display","block");//관리자 페이지 오픈
            $('#loginPage').css("display","none");//로그인 숨기기
            $('#main').css("display","block");//메인 페이지 오픈
            $('.logout').css("display","block");//로그아웃 오픈
        }else if(role === 'ROLE_USER'){
            console.log('회원');
            $('.admin-token').css("display","none");//관리자 페이지 숨기기
            $('#loginPage').css("disabled","none");//로그인 숨기기
            $('#main').css("display","block");//메인 페이지 오픈
            $('.logout').css("display","block");//로그아웃 오픈
            $('#mypage').css("display","block")//마이페이지 오픈
        }
    }else{//토큰이 없는 경우
        console.log('익명');
        $(".userId").text('로그인을 해주세요.');
        $('#loginPage').css("display","block");//로그인 오픈
        $('.logout').css("display","none");//로그아웃 숨기기
    }
};

/*
 * 로컬스토리지에 저장하기.(기간 포함)
 */
function setItemWithExpireTime(keyName,keyValue,ttl){
    //로컬스토리지에 저장할 객체
    const obj = {
        value : keyValue,
        expire : ttl
    }
    // 객체를 JSON 문자열로 변환
    const objString = JSON.stringify(obj);
    // 로컬스토리지에 토큰값을 저장
    window.localStorage.setItem(keyName, objString);
}

/*
 * 로컬스토리지에 저장된 값을 가져오기.
 */
function getItems(){
    //value값
   let value =localStorage.getItem('Authorization');
   let result = JSON.parse(value);
   return result;
}

/*
 * 토큰 파싱
 */
function tokenParse(token){
    if(token){
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(window
            .atob(base64)
            .split('')
            .map(function(c) {
                return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);}).join(''));

        let result = JSON.parse(jsonPayload);
        //at토큰 유효기간 검증
        validTokenExpiredTime(result.exp);
        return result;
    }
}

/*
 * 로컬스토리지에 저장된 토큰을 파싱후 권한에 맞게 페이지 이동
 */
function loginSuccessProc(tokenId){
    //토큰값이 있다면 파싱후 권한에 맞는 페이지로 이동하기.
    let result = tokenParse(tokenId);

    if(result.role === 'ROLE_ADMIN'){
        //관리자로 로그인을 할 경우에는 어드민 페이지 활성화, 로그인 비활성화
        //관리자 페이지 활성화
        location.href='/page/member/adminlist';
    }else if(result.role === 'ROLE_USER'){
        location.href='/page/main/mainpage';
    }else{
        console.log('익명');
    }
}

/*
 * 로컬스토리지에 저장된 토큰의 유효기간 확인
 */
function validTokenExpiredTime(exp){
    //AccessToken의 유효기간
    let expirationDate = new Date(exp*1000);
    //현재 시간
    let currentDate =  new Date();
    let differentTime = expirationDate - currentDate;
    let TokenValue = getItems();

    if(TokenValue!=null){
        console.log(differentTime);
        console.log(TokenValue.value);
        if(differentTime <120000){ //토큰의 유효기간이 만료되기 2분전에 주기적으로 재발급을 실행.
            console.log("재발급을 시작");
            reissue(TokenValue.value);
            console.log("재발급 끗.");
        }else{
            console.log("토큰 유효");
        }
    }
}

/*
 * 토큰 재발급
 */
function reissue(token){
    //저장된 accessToken이 유효한지 확인
    console.log('재발급');
    //ajax를 사용해서 rt와 at를 헤더에 넣어서 토큰을 재발급을 하고 로그인 절차와 똑같이 한다.->순수 자바스크립트 코드로 변경하기.
    $.ajax({
        url:'/api/login/reissue',
        type:'post',
        headers:{'Authorization ': token},
        dataTye:'json',
        contentType:'application/json; charset=UTF-8'
    }).done(function(data,xhr,request){
        alert('재발급');
        console.log('재발급');
        //작동이 되면 at를 로컬 스토리지에 저장을 하고 rt는 쿠키에 저장을 한다.
        console.log(data);
        console.log(xhr);
        console.log(request.getResponseHeader('Authorization'));
        let tokenResult = tokenParse(data.accessToken);
        console.log(tokenResult.exp);
        setItemWithExpireTime('Authorization',data.accessToken,tokenResult.exp);
        let token = localStorage.getItem('Authorization');
        console.log(token);
    });
    console.log("재발급 끗");
}