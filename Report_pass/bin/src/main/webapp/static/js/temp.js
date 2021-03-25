/**
 * 
 */

function byc(obj){ 
   if((typeof obj).match("string")){
   var regx = new RegExp(/(-?\d+)(\d{3})/);
    var bExists = obj.indexOf(".", 0);//0번째부터 .을 찾는다.
    var strArr = obj.split('.');
    while (regx.test(strArr[0])) {//문자열에 정규식 특수문자가 포함되어 있는지 체크
        //정수 부분에만 콤마 달기 
        strArr[0] = strArr[0].replace(regx, "$1,$2");//콤마추가하기
    }
    if (bExists > -1) {
        //. 소수점 문자열이 발견되지 않을 경우 -1 반환
        obj = strArr[0] + "." + strArr[1];
    } else { //정수만 있을경우 //소수점 문자열 존재하면 양수 반환 
        obj = strArr[0];
    }
    return obj;//문자열 반환
    }else{
   obj = obj.toString();
   var regx = new RegExp(/(-?\d+)(\d{3})/);
    var bExists = obj.indexOf(".", 0);//0번째부터 .을 찾는다.
    var strArr = obj.split('.');
    while (regx.test(strArr[0])) {//문자열에 정규식 특수문자가 포함되어 있는지 체크
        //정수 부분에만 콤마 달기 
        strArr[0] = strArr[0].replace(regx, "$1,$2");//콤마추가하기
    }
    if (bExists > -1) {
        //. 소수점 문자열이 발견되지 않을 경우 -1 반환
        obj = strArr[0] + "." + strArr[1];
    } else { //정수만 있을경우 //소수점 문자열 존재하면 양수 반환 
        obj = strArr[0];
    }
    return obj;
    }
 } 
 	$("#selectsite").change(function() {
		var site = $("#selectsite").val();
		location.href=site;
	}); 

$(document).ready(function() {

	trade();

});

function trade() {
	$.ajax({
		url: '/board/reportForm/'+$('#site').val(),
		type: 'post',
		dataType: 'json',
		data: {
			site: $('#site').val()
		},
		success: function(json) {
			console.log(json);
			$('#mytable').html("");
			$.each(json, function(index, trade) {

				// 게시글 가져오기
				$.ajax({
					url: '/board/report/' + trade.orderId,
					type: 'get',
					dataType: 'text',
					success: function(json) {

						if (json == "0") {
							report = '<td><a href="/board/saveForm/' + trade.orderId + '">작성</a></td>';
							console.log(report);
						} else {
							report = '<td><a href="/board/derailForm/' + trade.orderId + '">보기</a></td>';

						}


						if (trade.price == null) price = " ";
						else price = trade.price;


						if (trade.avgPrice == null) avgPrice = " ";
						else avgPrice = trade.avgPrice;

						if (trade.totalPrice == null) totalPrice = " ";
						else totalPrice = trade.totalPrice;


						if (trade.revenue == null) revenue = " ";
						else {
						numrevenue = Number(trade.revenue)*100;
						numrevenue = numrevenue.toFixed(2);
						revenue = numrevenue.toString() + '%';
						}
						if (trade.clearRevenue == null) clearRevenue = " ";
						else{
							numclearRevenue = Number(trade.clearRevenue)*100;
				numclearRevenue= numclearRevenue.toFixed(2);
				clearRevenue= numclearRevenue.toString()+'%';
				
						} 

						if (trade.log == null) log = " ";
						else log = trade.log;

						

						units = Number(trade.units).toFixed(4);
						


						$('#myTable').append("<tr><td>" + trade.site + "</td>" +
							"<td>" + trade.currency + "</td>" +
							"<td>" + trade.transactionDate + "</td>" +
							"<td>" + trade.type + "</td>" +
							"<td>" + byc(price) + "</td>" +
							"<td>" + units + "</td>" +
							"<td>" + byc(avgPrice) + "</td>" +
							"<td>" + byc(Number(trade.fee).toFixed(0)) + "</td>" +
							"<td>" + byc(totalPrice) + "</td>" +
							"<td>" + revenue + "</td>" +
							"<td>" + clearRevenue + "</td>" +
							"<td>" + '<input type="text" id ="' + trade.orderId + '" value="' + log + '" style="width:70%">' +
							'<input type="button" onclick="updateLog(\'' + trade.orderId + '\')"  class="btn "  value= "저장" style="width:20%;font-size:13px;"/>' +
							"<td>" + report + "</td></tr>")

					},
					error: function(json) {
						console.log('실패');
					}
				});
			});
		},
		error: function(json) {
			alert('에러' + json.status + ',' + json.textStatus);
		}
	});
}


function updateLog(orderId) {
	answer = confirm("일지를 저장하시겠습니까??");
	alert($("#" + orderId).val());
	if (answer == true) {

		$.ajax({
			type: "Post",//insert
			url: "/board/reportForm/" + orderId,
			data: {
				log: $("#" + orderId).val()
			},
			dataType: "text"//요창을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 문자열 (생긴게 json이라면)=>javascript
		}).success(function(r) {
			alert("수정이 완료되었습니다.");

			trade();
		}).fail(function(r) {
			//var message = JSON.parse(r.responseText);
			//console.log((message));
			alert('서버 오류');
		});



	}
}

