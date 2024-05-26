/*
* 게시글 좋아요
*/

$(document).ready(function(){
    //좋아요 갯수
    likeCount();
});

//좋아요 갯수
function likeCount(){

    let id = $('#boardid').val();
    let token = localStorage.getItem('Authorization');
    let result = JSON.parse(token);

    $.ajax({
        url:'/api/like/count/'+id,
        type:'get',
        dataTye: 'json',
        contentType: 'application/json; charset = utf-8',
        headers:{'Authorization': 'Bearer '+result.value}
    }).done(function(resp){
        console.log(resp.data);
        let html = "";
        html +='<i class="fas fa-solid fa-heart">'+resp.data+'</i>';
        $('#likecount').html(html);
    });
}

//좋아요 중복기능
function addDuplicatedLike(){

    let id = $('#boardid').val();
    let token = localStorage.getItem('Authorization');
    let result = JSON.parse(token);

    $.ajax({
        url:'/api/like/duplicated/'+id,
        type:'get',
        headers:{'Authorization':  'Bearer '+result.value},
        contentType:'application/json; charset = utf-8',
        dataTye: 'json'
    }).done(function(resp){
        console.log(resp);
        console.log(resp.data);
        if(resp.data== true){
            alert("좋아요 취소");
        }
        if(resp.data== false){
            alert("좋아요 추가");
        }
    });
}