/**
 * 
 */
let index ={
		init:function(){
			$("#btn-board-save").on("click",()=>{
			var test = $("#orderId").val();
				this.save(test);
			} );
		},
		save: function(test){
		//	alert("user 의 save 함수호출됨");
		let data = {
				orderId:$("#orderId").val(),
				title :$("#title").val(),
				content :$("#content").val()
			};
			//console.log(data);
			//ajax통신을 이용해서 세개의 데이터를 json으로 변경하여 insert 요청하기 
			//ajax호출시 default가 비동시 호출
			$.ajax({
				//회원가입수행 요청(100초 가정)
				type:"POST",//insert
				url:"/api/board",
				data:JSON.stringify(data),
				contentType:"application/json;charset=utf-8",
				dataType:"json"//요창을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 문자열 (생긴게 json이라면)=>javascript
			}).done(function(r){
				alert("글쓰기가 완료되었습니다.");
				location.href="/board/"+test;
			}).fail(function(r){
				//var message = JSON.parse(r.responseText);
				//console.log((message));
				alert('서버 오류');
			}); 
		}
} 
index.init();