/**
 * 
 */

function deleteapi(idx) {
	answer = confirm("거래소를 삭제하시겠습니까?");
	if (answer == true) {
		$.ajax({

			type: "POST",//insert
			url: "/deleteapi",
			data: {
				idx: idx
			},
			/*contentType: "application/json;charset=utf-8",*/
			dataType: "text"//요창을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 문자열 (생긴게 json이라면)=>javascript

			, success: function(r) {
				alert("삭제완료");
				window.location.href = "/getList";
			},
			error: function(r) {

			}
		});
	}
}
function detailapi(site) {
		location.href="/board/reportForm/"+site;
}


