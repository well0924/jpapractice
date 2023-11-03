//비밀번호 재설정
function passwordSetting(){

    let userName = document.getElementById('userid');
    let password = document.getElementById('user-password');

    $.ajax({
        url:'api/member/password-change/'+userName,
        type:'put',
        data:JSON.stringify(password),
        dataType:'json',
        contentType:'application/json; charset=utf-8'
    }).done(function(resp){
        console.log(resp);
        alert("비밀번호가 변경되었습니다.");
    });
}
