/**
 * 
 */
let index ={
      init:function(){
         $("#btn-delete").on("click",()=>{
            answer = confirm("상세글을 삭제하시겠습니까?");
            if (answer == true) {
            this.deleteById();
      
            }
         } );
         $("#btn-update").on("click",()=>{
            this.update();
         } );
         $("#btn-back").on("click",()=>{
            this.backFunction();
         } );
         
      },
      backFunction:function(){
         let site= $("#site").val();
         location.href="/board/reportForm/"+site;
      },
      deleteById: function(){
         let orderId= $("#orderId").val();
         $.ajax({
            type:"DELETE",//insert
            url:"/api/board/"+orderId,
            contentType:"application/json;charset=utf-8",
            dataType:"json"//요창을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 문자열 (생긴게 json이라면)=>javascript
         }).done(function(r){
            alert("삭제가 완료되었습니다.");
            opener.parent.location.reload();
            window.close();
         }).fail(function(r){
            //var message = JSON.parse(r.responseText);
            //console.log((message));
            //alert('서버 오류');
         }); 
      },
      update: function(){
         let orderId= $("#orderId").val();
         let site= $("#site").val();
         let data= {
            title : $("#title").val(),
             content : $("#content").val()
         }
         $.ajax({
            type:"PUT",//insert
            url:"/api/board/"+orderId,
            data: JSON.stringify(data),
            contentType:"application/json;charset=utf-8",
            dataType:"json"//요창을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 문자열 (생긴게 json이라면)=>javascript
         }).done(function(r){
            alert("수정이 완료되었습니다.");
            alert(r);
            location.href="/board/"+orderId+"/updateForm"+site;
         }).fail(function(r){
            //var message = JSON.parse(r.responseText);
            //console.log((message));
            //alert('서버 오류');
         }); 
      }
} 
index.init();