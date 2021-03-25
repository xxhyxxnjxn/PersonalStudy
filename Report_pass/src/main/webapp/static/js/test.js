/**
 * 
 */
function byc(obj) {
	if ((typeof obj).match("string")) {
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
	} else {
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
	location.href = site;
});


$("#orderby").change(function() {
	var orderby = $("#orderby").val();
	$('#myTable').html("");
	trade();
});



$(document).ready(function() {

	trade();

});

function trade() {
	$.ajax({
		url: '/board/reportForm/' + $('#site').val(),
		type: 'post',
		async: false,
		dataType: 'json',
		data: {
			orderby : $('#orderby').val()
		},
		success: function(json) {
			console.log(json);
			$('#myTable').html("");
			$.each(json, function(index, trade) {

				console.log(trade.transactionDate);
				// 게시글 가져오기
				$.ajax({
					url: '/board/report/' + trade.orderId,
					type: 'get',
					async: false,
					dataType: 'text',
					success: function(json) {

						if (json == "0") {
							report = '<td><a href="/board/saveForm/' + trade.orderId +'/'+trade.site+'">작성</a></td>';
							console.log(report);
						} else {
							report = '<td><a href="/board/' + trade.orderId+'/'+trade.site+'">보기</a></td>';
						}

						if (trade.price == null) price = " ";
						else price = trade.price;

						if (trade.avgPrice == null) avgPrice = " ";
						else avgPrice = trade.avgPrice;

						if (trade.totalPrice == null) totalPrice = " ";
						else totalPrice = trade.totalPrice;


						if (trade.revenue == null) revenue = " ";
						else {
							numrevenue = Number(trade.revenue) * 100;
							numrevenue = numrevenue.toFixed(2);
							revenue = numrevenue.toString() + '%';
						}
						if (trade.clearRevenue == null) clearRevenue = " ";
						else {
							numclearRevenue = Number(trade.clearRevenue) * 100;
							numclearRevenue = numclearRevenue.toFixed(2);
							clearRevenue = numclearRevenue.toString() + '%';

						}

						if (trade.log == null) log = " ";
						else log = trade.log;

						units = Number(trade.units).toFixed(4);

						var lit = parseFloat(trade.transactionDate);
						if($('#site').val() == "bithumb"){
						var trade_dateYear = new Date(lit/1000).getFullYear();
						var trade_dateMonth = new Date(lit/1000).getMonth()+1;
							trade_dateMonth = "0"+trade_dateMonth;
							trade_dateMonth = trade_dateMonth.slice(-2)
						var trade_dateDate = new Date(lit/1000).getDate();
							trade_dateDate = "0"+trade_dateDate;
							trade_dateDate = trade_dateDate.slice(-2)
						var trade_dateHour = new Date(lit/1000).getHours();
							trade_dateHour = "0"+trade_dateHour;
							trade_dateHour = trade_dateHour.slice(-2)
						var trade_dateMinute = new Date(lit/1000).getMinutes();
							trade_dateMinute = "0"+trade_dateMinute;
							trade_dateMinute = trade_dateMinute.slice(-2)
						var trade_dateSecond = new Date(lit/1000).getSeconds();
							trade_dateSecond = "0"+trade_dateSecond;
							trade_dateSecond = trade_dateSecond.slice(-2)
						var full = trade_dateYear+". "+trade_dateMonth+". "+trade_dateDate+". " +trade_dateHour+":"+trade_dateMinute+":"+trade_dateSecond;
						
					
						}else if($('#site').val() == "coinone"){
						var trade_dateYear = new Date(lit*1000).getFullYear();
						var trade_dateMonth = new Date(lit*1000).getMonth()+1;
							trade_dateMonth = "0"+trade_dateMonth;
							trade_dateMonth = trade_dateMonth.slice(-2)
						var trade_dateDate = new Date(lit*1000).getDate();
							trade_dateDate = "0"+trade_dateDate;
							trade_dateDate = trade_dateDate.slice(-2)
						var trade_dateHour = new Date(lit*1000).getHours();
							trade_dateHour = "0"+trade_dateHour;
							trade_dateHour = trade_dateHour.slice(-2)
						var trade_dateMinute = new Date(lit*1000).getMinutes();
							trade_dateMinute = "0"+trade_dateMinute;
							trade_dateMinute = trade_dateMinute.slice(-2)
						var trade_dateSecond = new Date(lit*1000).getSeconds();
							trade_dateSecond = "0"+trade_dateSecond;
							trade_dateSecond = trade_dateSecond.slice(-2)
						var full = trade_dateYear+". "+trade_dateMonth+". "+trade_dateDate+". " +trade_dateHour+":"+trade_dateMinute+":"+trade_dateSecond;
						
					
						
						}else{
						var trade_dateYear = new Date(lit).getFullYear();
						var trade_dateMonth = new Date(lit).getMonth()+1;
							trade_dateMonth = "0"+trade_dateMonth;
							trade_dateMonth = trade_dateMonth.slice(-2)
						var trade_dateDate = new Date(lit).getDate();
							trade_dateDate = "0"+trade_dateDate;
							trade_dateDate = trade_dateDate.slice(-2)
						var trade_dateHour = new Date(lit).getHours();
							trade_dateHour = "0"+trade_dateHour;
							trade_dateHour = trade_dateHour.slice(-2)
						var trade_dateMinute = new Date(lit).getMinutes();
							trade_dateMinute = "0"+trade_dateMinute;
							trade_dateMinute = trade_dateMinute.slice(-2)
						var trade_dateSecond = new Date(lit).getSeconds();
							trade_dateSecond = "0"+trade_dateSecond;
							trade_dateSecond = trade_dateSecond.slice(-2)
						var full = trade_dateYear+". "+trade_dateMonth+". "+trade_dateDate+". " +trade_dateHour+":"+trade_dateMinute+":"+trade_dateSecond;
						
					
						}
						
						
						if(trade.clearRevenue>0){
						var clearRevColor = "text-danger";
						}	else if(trade.clearRevenue<0) {
						var clearRevColor = "text-primary";
						}
						if(trade.type.toString()=="매수"){
						var typeColor = "text-danger";
						}	else if(trade.type.toString()=="매도") {
						var typeColor = "text-primary";
						}
						if(trade.revenue >0 ){
						var tradeColor = "text-danger";
						}	else if(trade.revenue <0) {
						var tradeColor = "text-primary";
						}
						

						$('#myTable').append("<tr><td>" + trade.site + "</td>" +
							"<td>" + trade.currency + "</td>" +
							"<td id='dateId'>" + full + "</td>" +
							"<td class="+typeColor+">" +  trade.type + "</td>" +
							"<td>" + byc(price) + "</td>" +
							"<td>" + units + "</td>" +
							"<td>" + byc(avgPrice) + "</td>" +
							"<td>" + byc(Number(trade.fee).toFixed(0)) + "</td>" +
							"<td>" + byc(totalPrice) + "</td>" +
							"<td class="+tradeColor+">" + revenue + "</td>" +
							"<td class="+clearRevColor+">" + clearRevenue + "</td>" +
							"<td style='colspan=2'>" + '<input type="text" id ="logId' + trade.orderId + '" value="' + log + '" style="width:75%" colspan="2" placeholder="'+log+'">' +
							'<input type="button" onclick="updateLog(\'' + trade.orderId + '\')"  class="btn "  value= "저장" style="font-size:13px;padding: .3rem .7rem;width:40px"/>' +
							"<td>" + report + "</td></tr>"+
						'')

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
