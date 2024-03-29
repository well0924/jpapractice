/**
 * 로그인시에 공통적으로 사용되는 부분
 **/

/**
 * 로그인후 권한에 따라 화면 변환
 **/
window.onload= function (){
    //로컬스토리지에 토큰값이 있는 경우
    let tokenId = localStorage.getItem('Authorization');
    console.log(tokenId);
    let result = tokenParse(tokenId);

    if(tokenId){
        let username = result.userId;
        let role = result.role;
        let expire = result.exp;
        let expireTime = new Date(expire *1000);
        console.log(result);
        console.log(document.cookie);
        console.log("아이디:"+username);
        console.log("권한:"+role);
        console.log("유효기간:"+expire);
        console.log(expireTime);
        //알림기능
        notification(username);
        
        $('#userDetail').attr('href','/page/mypage/detail/'+username);//마이페이지
        $('#userComment').attr('href','/page/mypage/my-comment/'+username);//내가 작성한 댓글
        $('#userBoard').attr('href','/page/mypage/my-article/'+username);//내가 작성한 글
        $('#scrapList').attr('href','/page/mypage/list/'+username);//스크랩을 한 글 목록

        //1분마다 주기적으로 유효기간을 검증한다.
        setInterval(function(){validTokenExpiredTime(expire)},60000);


        //권한에 따라 사이드바를 변경
        if(role === 'ROLE_ADMIN'){
            console.log('관리자');
            $('#admin-side-bar').css("display","block");
            $('#admin-page').css("display","block");
            $('#user-side-bar').css("display","none");
            $('.user-side-bar-page').css("display","none");
            $(".userId").text(username + "님 환영합니다.");//로그인한 회원의 아이디 표시
            $('.admin-token').css("display","block");//관리자 페이지 오픈
            $('#loginPage').css("display","none");//로그인 숨기기
            $('#main').css("display","block");//메인 페이지 오픈
            $('.logout').css("display","block");//로그아웃 오픈
        }else if(role === 'ROLE_USER'){
            console.log('회원');
            $('#admin-side-bar').css("display","none");
            $('#admin-page').css("display","none");
            $('#user-side-bar').css("display","block");
            $('#user-side-bar-page').css("display","block");
            $(".userId").text(username + "님 환영합니다.");//로그인한 회원의 아이디 표시
            $('.admin-token').css("display","none");//관리자 페이지 숨기기
            $('#loginPage').css("display","none");//로그인 숨기기
            $('#main').css("display","block");//메인 페이지 오픈
            $('.logout').css("display","block");//로그아웃 오픈
            $('#mylist').css("display","block")//마이페이지 오픈
            $('#mypage').attr('href','/page/mypage/list/'+username);//마이페이지
        }
    }else{//토큰이 없는 경우
        console.log('익명');
        $('#admin-side-bar').css("display","none");//관리자 페이지 사이드바 숨김.
        $('#user-side-bar').css("display","block");//회원 페이지 사이드바 오픈
        $('#admin-page').css("display","none");
        $(".userId").text('로그인을 해주세요.');
        $('#loginPage').css("display","block");//로그인 오픈
        $('.logout').css("display","none");//로그아웃 숨기기
    }
};

/**
 * 로컬스토리지에 저장하기.(기간 포함)
 **/
function setItemWithExpireTime(keyName,keyValue,ttl){
    //로컬스토리지에 저장할 객체(토큰값,유효기간)
    const obj = {
        value : keyValue,
        expire : ttl
    }
    // 객체를 JSON 문자열로 변환
    const objString = JSON.stringify(obj);
    // 로컬스토리지에 토큰값을 저장
    window.localStorage.setItem(keyName, objString);
}

/**
 * 로컬스토리지에 저장된 값을 가져오기.
 **/
function getItems(){
    //value값
   let value =localStorage.getItem('Authorization');
   let result = JSON.parse(value);
   return result;
}

/**
 * 토큰 파싱
 **/
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

/**
 * 로컬스토리지에 저장된 토큰을 파싱후 권한에 맞게 페이지 이동
 **/
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

/**
 * 로컬스토리지에 저장된 토큰의 유효기간 확인
 **/
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

        if(differentTime == 120000){ //토큰의 유효기간이 만료되기 1분전에 재발급하기.
            console.log("재발급을 시작");
            //ajax를 사용해서 rt와 at를 헤더에 넣어서 토큰을 재발급을 하고 로그인 절차와 똑같이 한다.
            //이방법으로 하는 경우에는 작동은 되지만 다른 방법을 생각해 봐야 할 것 같다.
            console.log(TokenValue);
            console.log("액세스토큰::"+TokenValue.value);
            var refreshToken = getRefreshToken();
            console.log("리프레시토큰::"+refreshToken);
            $.ajax({
                url:'/api/login/reissue',
                type:'post',
                xhrFields: {
                    withCredentials: true
                },
                headers:{
                    'Authorization':'Bearer '+TokenValue.value,
                    'Cookie': 'refresh-token='+refreshToken
                },
                dataTye:'json'
            }).always(function(data){
                console.log('재발급성공!!');
                //작동이 되면 at를 로컬 스토리지에 저장을 하고 rt는 쿠키에 저장을 한다.
                console.log(data);
                //기존에 있던 로컬스토리지에 있는 값을 제거
                localStorage.clear();
                let accessToken = tokenParse(data.accessToken);
                //받은 데이터에 at를 로컬 스토리지에 저장하기.
                setItemWithExpireTime('Authorization',data.accessToken,accessToken.exp);
                //rt는 쿠키에 저장
                console.log("재발급 끗. 토큰저장!");
            }).fail(function(err){
                console.log(err);
            });
        }else{
            console.log("토큰 유효");
        }
    }
}

/**
 * 쿠키에 저장된 값 가져오기.(xss 공격에 취약한데 어떻게 처리를 해야될지.....)
 **/
function getRefreshToken() {
    // document.cookie로부터 쿠키 값을 읽어옵니다.
    var cookies = document.cookie.split(';');
    for (var i = 0; i < cookies.length; i++) {
        var cookie = cookies[i].trim();
        // "refresh-token="으로 시작하는 쿠키를 찾아 해당 값을 반환합니다.
        if (cookie.startsWith('refresh-token=')) {
            return cookie.substring('refresh-token='.length);
        }
    }
    return null;
}

/**
 * 쿠키값 삭제하기.
 **/
function deleteCookie(name) {
    document.cookie = name + "=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
}

/**
 * SSE 알림기능
 **/
function notification(username){

    // EventSource로 구독
    const eventSource = new EventSource("/api/notice/subscribe"+"?userName="+username);

    eventSource.onopen = function (event){
        console.log(event);
        console.log(username);
    }

    //이벤트 알림
    eventSource.addEventListener("open",function(event){
        console.log("connect");
        let message = event.data;
        console.log(message);
        new Notification("알림:"+message);
        
    });
    //에러가 났을경우
    eventSource.onerror = function(error) {
        console.error("Error:", error);
    };
}
