/*
* 로그아웃
*/
//로그아웃 로직
function logoutProc(){
    let token = localStorage.getItem('Authorization');
    $.ajax({
        url:'/api/login/logout',
        type:'post',
        header:{'Authorization': 'Bearer '+token,}
    }).done(function (status) {
        //로그아웃이 되면 로컬 스토리지에 있는 at를 삭제
        alert('로그아웃 되었습니다.');
        localStorage.clear();
        location.href="/page/main/mianpage";
    });
}