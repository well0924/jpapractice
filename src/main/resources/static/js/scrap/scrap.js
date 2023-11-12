//게시물 스크랩 추가하기.
function addScrap(){
    //로컬 스토리지에 저장된 at
    let token = getItems();
    let result = token.value;
    let id = document.getElementById("boardid").value;
    console.log(id);
    $.ajax({
        url:'/api/scrap/duplicate/'+id,
        type:'get',
        headers:{
            'Authorization': 'Bearer '+result,
        },
        dataType:'json'
    }).done(function(resp){
        console.log(resp.data);
        if(resp.data ===false){
            alert('게시글을 스크랩했습니다.');
        }else{
            alert('이미 스크랩한 글입니다.');
        }
    });
}