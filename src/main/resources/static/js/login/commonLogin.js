let tokenId = localStorage.getItem('X-AUTH-TOKEN');
console.log(tokenId);
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
    console.log(result.roles);
    if(result.roles != "ROLE_ADMIN"){//권한이 관리자가 아닌경우에는 숨김
        document.getElementById('admin-token').style.display ='none';//none은 노출
        console.log('관리자임');
    }else if(result.roles === "ROLE_USER"){//관리자면 보이기.
        console.log('회원임');
        document.getElementById('admin-token').style.display='none';//block은 숨김.
    }

}
if(!tokenId){
    //로그인을 하기 전인 경우
    if(result === null){
        document.getElementById('admin-token').style.display ='block';
    }
}