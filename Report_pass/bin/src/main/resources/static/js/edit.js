/**
 * 
 */

function deleteapi(idx) {
	answer1 = confirm("거래소를 삭제하시겠습니까?");
	if(answer1==true){
	answer2 = confirm("계정에 저장된 거래내역도 삭제하시겠습니까?");
	 if(answer2==false){
			$.ajax({
	
				type: "POST",//insert
				url: "/deleteapi",
				data: {
					idx: idx
				},
				/*contentType: "application/json;charset=utf-8",*/
				dataType: "text"//요창을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 문자열 (생긴게 json이라면)=>javascript
	
				, success: function(r) {
					alert("계정 삭제 완료");
					window.location.href = "/getList";
				},
				error: function(r) {
	
				}
			});
			
		}	else if (answer2 == true) {
									$.ajax({
													type: "POST",//insert
													url: "/deleteList",
													data: {
														idx: idx
													},
													/*contentType: "application/json;charset=utf-8",*/
													dataType: "text"//요창을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 문자열 (생긴게 json이라면)=>javascript
										
													, success: function(r) {
															
														alert("계정 & 거래내역 삭제 완료");
														window.location.href = "/getList";
														
														
													},
													error: function(r) {
															
													}
												});
				
			}
		}
		
		
		} 
			
function detailapi(site) {
					$.ajax({
										type: "POST",//insert
										url: "/confirm/apiState",
										data: {
											site : site
										},
										/*contentType: "application/json;charset=utf-8",*/
										dataType: "text"//요창을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 문자열 (생긴게 json이라면)=>javascript
										, success: function(re) {
											var result = Number(re);
/*											if(re==0){
												alert( "값을 저장 중입니다.");
											}else{*/
												location.href="/board/reportForm/"+site;
											//}
										},
										error: function(r) {
										}
									});
									
									
		
}


