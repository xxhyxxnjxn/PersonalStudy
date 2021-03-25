/**
 * 
 */

let index ={
		init:function(){
			$("#selectsCoins").on("change",()=>{
				alert($("#selectsCoins").val());
			} );
/*			$("#selectSites").on("change",()=>{
				var site = $("#selectSites").val();
				this.selectedSite(site);
			} );*/
		},
		selectedSite: function(site){
			$.ajax({
				type:"POST",//insert
				url:"/report/selectedSite/"+site,
				contentType:"application/json;charset=utf-8",
				dataType:"json"//요창을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 문자열 (생긴게 json이라면)=>javascript
				,success:function(){
					alert("성공");
				}
				,error:function(){
					alert("실패");
				}
			}).done(function(r){
				alert("수정이 완료되었습니다.");
				alert(r);
				location.href="/board/reportForm3";
			}).fail(function(r){
				//var message = JSON.parse(r.responseText);
				//console.log((message));
				//alert('서버 오류');
			}); 
		},
		update: function(){
			let orderId= $("#orderId").val();
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
				location.href="/board/"+orderId+"/updateForm";
			}).fail(function(r){
				//var message = JSON.parse(r.responseText);
				//console.log((message));
				//alert('서버 오류');
			}); 
		}
} 
index.init();