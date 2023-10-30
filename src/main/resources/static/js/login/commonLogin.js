/*
 * 로그인후 권한에 따라 화면 변환
 */
window.onload= function (){
    //로컬스토리지에 토큰값이 있는 경우
    let tokenId = localStorage.getItem('Authorization');

    let result = tokenParse(tokenId);

    if(tokenId){
        let username = result.userId;
        let role = result.role;
        console.log(username);
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
//로컬스토리지에 저장하기.(기간 포함)
function setItemWithExpireTime(keyName,keyValue,ttl){
    //로컬스토리지에 저장할 객체
    const obj = {
        value : keyValue,
        expire : Date.now() + ttl
    }
    // 객체를 JSON 문자열로 변환
    const objString = JSON.stringify(obj);
    // 로컬스토리지에 토큰값을 저장
    window.localStorage.setItem(keyName, objString);
}

//토큰 파싱
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
        console.log(result);
        console.log(result.role);
        return result;
    }
}

//로컬스토리지에 저장된 토큰을 파싱후 권한에 맞게 페이지 이동
function loginSuccessProc(tokenId){
    //토큰값이 있다면 파싱후 권한에 맞는 페이지로 이동하기.
    let result = tokenParse(tokenId);

    if(result.role === 'ROLE_ADMIN'){
        console.log('관리자')
        //관리자로 로그인을 할 경우에는 어드민 페이지 활성화, 로그인 비활성화
        //관리자 페이지 활성화
        location.href='/page/member/adminlist';
        document.cookie;
        console.log(document.cookie);
    }else if(result.role === 'ROLE_USER'){
        console.log('회원')
        location.href='/page/main/mainpage';
        document.cookie;
        console.log(document.cookie);
    }else{
        console.log('익명');
    }
}
//로컬스토리지에 저장된 토큰의 유효기간 확인
function validTokenExpiredTime(){

}
//유효기간이 얼마 안남았을 경우에 토큰을 재발급
function reissueToken(){

}