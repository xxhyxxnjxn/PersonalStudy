/**
 * 
 *//**
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
	var page = 0;
	trade2(page);
});

$("#type").change(function() {
	var type = $("#type").val();
	$('#myTable').html("");
	var page = 0;
	trade2(page);
});

$("#currency").change(function() {
	var currency = $("#currency").val();
	$('#myTable').html("");
	var page = 0;
	trade2(page);
});

$("#ListCount").change(function() {
	var listCount = $("#ListCount").val();
	$('#myTable').html("");
	var page = 0;
	trade2(page);
});

function date() {

	if ($('#start').val() == "") {
		alert("시작일을 설정해주세요");
	} else if ($('#end').val() == "") {
		alert("종료일을 설정해주세요");
	} else {
		var page = 0;
		trade2(page);
	}

}

var timer = 0;
var timer2 =0;
$(document).ready(function() {
	//	$('#loading').hide();
	//stateTimer();
	$('#loading').show();
	timerStart();

	//$('#rerevenue').attr('disabled', true);
});

/*


$(document).ajaxStart(function() {
	$('#loading').show();

});
$(document).ajaxStop(function() {
	$('#loading').hide();
	//location.replace("/getList");
});


*/
function timerStart() {
	timer = setInterval(function() {
		stateTimer();
	}, 1000);
	timer2 = setInterval(function() {
		revenueTime();
	}, 1000);
}
var timerRoadingNum = 0;
function stateTimer() {
	$.ajax({
		url: '/board/apiRoadingState/' + $('#site').val(),
		type: 'post',
		async: false,
		dataType: 'json',
		global: false,
		success: function(json) {
			if (json.state == '1') {


				$.ajax({
					url: '/board/BankStateMentState/' + $('#site').val(),
					type: 'post',
					async: false,
					dataType: 'json',
					global: false,
					success: function(json) {
						if (json.state == '1') {
							$.ajax({
								url: '/board/CandleStickState/' + $('#site').val(),
								type: 'post',
								async: false,
								dataType: 'json',
								global: false,
								success: function(json) {
									if (json.state == '1') {
										$('#loading').hide();
										if (timerRoadingNum > 0) {
											timerRoadingNum = 0;
											alert('업데이트 되었습니다.');
											location.reload();
										}
										document.getElementById('roadingStr').innerHTML = "";

										clearInterval(timer);
										findDate();
										var page = 0;
										trade2(page);
									} else {
										$('#loading').hide();
										timerRoadingNum++;
										document.getElementById('roadingStr').innerHTML = "입출금 시세를 찾는 중입니다.";
									}
								},
								error: function(json) {
									alert('에러' + json.status + ',' + json.textStatus);
								}
							});

						} else {
							$('#loading').hide();
							timerRoadingNum++;
							document.getElementById('roadingStr').innerHTML = "입출금 내역을 저장중입니다. (" + json.num + "/" + json.totalNum + ")";
						}
					},
					error: function(json) {
						alert('에러' + json.status + ',' + json.textStatus);
					}
				});




			} else {
				$('#loading').hide();
				timerRoadingNum++;
				if ($('#site').val() == 'upbit') {
					document.getElementById('roadingStr').innerHTML = "거래내역을 조회 중입니다.";
				} else {
					document.getElementById('roadingStr').innerHTML = "거래내역을 조회 중입니다. (" + json.num + "/" + json.totalNum + ")";
				}

			}
		},
		error: function(json) {
			alert('에러' + json.status + ',' + json.textStatus);
		}
	});
}

function error() {
	timerRoadingNum = 0;
	$.ajax({
		url: '/board/apiRoadingStateError/' + $('#site').val(),
		type: 'post',
		async: false,
		dataType: 'json',
		global: false,
		success: function(json) {
			$.ajax({
				url: '/board/BankStateMentStateError/' + $('#site').val(),
				type: 'post',
				async: false,
				dataType: 'json',
				global: false,
				success: function(json) {
					$.ajax({
						url: '/board/CandleStickStateError/' + $('#site').val(),
						type: 'post',
						async: false,
						dataType: 'json',
						global: false,
						success: function(json) {
							alert('다시 시도해주세요.');
							location.reload();
						},
						error: function(json) {
							alert('에러' + json.status + ',' + json.textStatus);
						}
					});
				},
				error: function(json) {
					alert('에러' + json.status + ',' + json.textStatus);
				}
			});
		},
		error: function(json) {
			alert('에러' + json.status + ',' + json.textStatus);
		}
	});




}

function findDate() {
	$.ajax({
		url: '/board/reportForm/' + $('#site').val() + "/dateCount",
		type: 'post',
		async: false,
		dataType: 'json',
		global: false,
		success: function(json) {
			//alert(json.size)

			var max_lit = parseFloat(json[0]);
			var min_lit = parseFloat(json[1]);
			if ($('#site').val() == "bithumb") {

				var max_dateYear = new Date(max_lit / 1000).getFullYear();

				var max_dateMonth = new Date(max_lit / 1000).getMonth() + 1;
				max_dateMonth = "0" + max_dateMonth;
				max_dateMonth = max_dateMonth.slice(-2)

				var max_dateDate = new Date(max_lit / 1000).getDate();
				max_dateDate = "0" + max_dateDate;
				max_dateDate = max_dateDate.slice(-2)

				var min_dateYear = new Date(min_lit / 1000).getFullYear();

				var min_dateMonth = new Date(min_lit / 1000).getMonth() + 1;
				min_dateMonth = "0" + min_dateMonth;
				min_dateMonth = min_dateMonth.slice(-2)

				var min_dateDate = new Date(min_lit / 1000).getDate();
				min_dateDate = "0" + min_dateDate;
				min_dateDate = min_dateDate.slice(-2)

				var max_full = max_dateYear + "-" + max_dateMonth + "-" + max_dateDate;
				var min_full = min_dateYear + "-" + min_dateMonth + "-" + min_dateDate;

				document.getElementById('start').value = min_full;
				document.getElementById('end').value = max_full;


			} else if ($('#site').val() == "coinone") {

				var max_dateYear = new Date(max_lit).getFullYear();

				var max_dateMonth = new Date(max_lit).getMonth() + 1;
				max_dateMonth = "0" + max_dateMonth;
				max_dateMonth = max_dateMonth.slice(-2)

				var max_dateDate = new Date(max_lit).getDate();
				max_dateDate = "0" + max_dateDate;
				max_dateDate = max_dateDate.slice(-2)

				var min_dateYear = new Date(min_lit).getFullYear();

				var min_dateMonth = new Date(min_lit).getMonth() + 1;
				min_dateMonth = "0" + min_dateMonth;
				min_dateMonth = min_dateMonth.slice(-2)

				var min_dateDate = new Date(min_lit).getDate();
				min_dateDate = "0" + min_dateDate;
				min_dateDate = min_dateDate.slice(-2)

				var max_full = max_dateYear + "-" + max_dateMonth + "-" + max_dateDate;
				var min_full = min_dateYear + "-" + min_dateMonth + "-" + min_dateDate;

				document.getElementById('start').value = min_full;
				document.getElementById('end').value = max_full;

			} else {

				var max_dateYear = new Date(max_lit).getFullYear();

				var max_dateMonth = new Date(max_lit).getMonth() + 1;
				max_dateMonth = "0" + max_dateMonth;
				max_dateMonth = max_dateMonth.slice(-2)

				var max_dateDate = new Date(max_lit).getDate();
				max_dateDate = "0" + max_dateDate;
				max_dateDate = max_dateDate.slice(-2)

				var min_dateYear = new Date(min_lit).getFullYear();

				var min_dateMonth = new Date(min_lit).getMonth() + 1;
				min_dateMonth = "0" + min_dateMonth;
				min_dateMonth = min_dateMonth.slice(-2)

				var min_dateDate = new Date(min_lit).getDate();
				min_dateDate = "0" + min_dateDate;
				min_dateDate = min_dateDate.slice(-2)

				var max_full = max_dateYear + "-" + max_dateMonth + "-" + max_dateDate;
				var min_full = min_dateYear + "-" + min_dateMonth + "-" + min_dateDate;

				document.getElementById('start').value = min_full;
				document.getElementById('end').value = max_full;
			}
		},
		error: function(json) {
			alert('에러' + json.status + ',' + json.textStatus);
		}
	});
}

var totincome = 0;
function trade2(page) {
	//alert(page);
	var num;
	var hiddenType;
	var before_hiddenType;

	$.ajax({
		url: '/board/reportForm/' + $('#site').val() + "/rownum",
		type: 'post',
		async: false,
		dataType: 'json',
		data: {
			orderby: $('#orderby').val(),
			currency: $('#currency').val(),
			start: $('#start').val(),
			end: $('#end').val(),
			type: $('#type').val()
		},
		global: false,
		success: function(json) {
			//alert(json)
			num = json - (50 * page);

			$.ajax({
				url: '/board/reportForm/' + $('#site').val() + "?page=" + page + "&size=" + $('#ListCount').val(),
				type: 'post',
				async: false,
				dataType: 'json',
				data: {
					orderby: $('#orderby').val(),
					currency: $('#currency').val(),
					start: $('#start').val(),
					end: $('#end').val(),
					type: $('#type').val()
				},
				global: false,
				success: function(json) {

					//	alert(json.size);
					/*			if (page >= 1) {
									num = 50 * page;
								} else if (page == 0) {
									num = 0;
								}*/

					$('#myTable').html("");
					$.each(json.content, function(index, trade) {
						// 게시글 가져오기
						$.ajax({
							url: '/board/report/' + trade.orderId,
							type: 'get',
							async: false,
							dataType: 'text',
							global: false,
							success: function(json) {


								if (json == "0") {
									report = '<a href="#" onclick=window.open("/board/saveForm/' + trade.orderId + '/' + trade.site + '","window팝업","width=773,height=800,menubar=no,status=no,toolbar=no")>작성</a>';
									console.log(report);
								} else {
									report = '<a href="#"  onclick=window.open("/board/' + trade.orderId + '/' + trade.site + '","window팝업","width=950,height=773,menubar=no,status=no,toolbar=no") >보기</a>';
								}

								if (trade.askAccUnits == null) askAccUnits = 0.0;
								else askAccUnits = trade.askAccUnits;

								if (trade.bidAccUnits == null) bidAccUnits = 0.0;
								else bidAccUnits = trade.bidAccUnits;

								if (trade.price == null) price = " ";
								else price = trade.price;

								if (trade.bidAvgPrice == null) bidAvgPrice = " ";
								else bidAvgPrice = trade.bidAvgPrice;

								if (trade.askAvgPrice == null) askAvgPrice = " ";
								else askAvgPrice = trade.askAvgPrice;

								if (trade.totalPrice == null) totalPrice = " ";
								else totalPrice = trade.totalPrice;

								if (trade.bidTotalPriceCal == null) bidTotalPriceCal = " ";
								else bidTotalPriceCal = trade.bidTotalPriceCal;

								if (trade.askTotalPriceCal == null) askTotalPriceCal = " ";
								else askTotalPriceCal = trade.askTotalPriceCal;


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

								if (trade.incomeCal == null) incomeCal = " ";
								else {
									incomeCal = Math.floor(trade.incomeCal);
								}
								if (trade.income == null) income = " ";
								else {
									income = Math.floor(trade.income);
								}


								if (trade.log == null) log = " ";
								else log = trade.log;

								if (trade.type.toString() == "입금") {
									trade.units = trade.units.replace("+ ", "");
								} else if (trade.type.toString() == "출금") {
									trade.units = trade.units.replace("- ", "");
								}



								var lit = parseFloat(trade.transactionDate);
								if ($('#site').val() == "bithumb") {
									units = Number(trade.units).toFixed(4);

									var trade_dateYear = new Date(lit / 1000).getFullYear();
									var trade_dateMonth = new Date(lit / 1000).getMonth() + 1;
									trade_dateMonth = "0" + trade_dateMonth;
									trade_dateMonth = trade_dateMonth.slice(-2)
									var trade_dateDate = new Date(lit / 1000).getDate();
									trade_dateDate = "0" + trade_dateDate;
									trade_dateDate = trade_dateDate.slice(-2)
									var trade_dateHour = new Date(lit / 1000).getHours();
									trade_dateHour = "0" + trade_dateHour;
									trade_dateHour = trade_dateHour.slice(-2)
									var trade_dateMinute = new Date(lit / 1000).getMinutes();
									trade_dateMinute = "0" + trade_dateMinute;
									trade_dateMinute = trade_dateMinute.slice(-2)
									var trade_dateSecond = new Date(lit / 1000).getSeconds();
									trade_dateSecond = "0" + trade_dateSecond;
									trade_dateSecond = trade_dateSecond.slice(-2)
									var full = trade_dateYear + ". " + trade_dateMonth + ". " + trade_dateDate + ". " + trade_dateHour + ":" + trade_dateMinute + ":" + trade_dateSecond;
									var feeReal = trade.fee.toString().replace(",", "")

								} else if ($('#site').val() == "coinone") {
									units = Number(trade.units).toFixed(4);

									var trade_dateYear = new Date(lit).getFullYear();
									var trade_dateMonth = new Date(lit).getMonth() + 1;
									trade_dateMonth = "0" + trade_dateMonth;
									trade_dateMonth = trade_dateMonth.slice(-2)
									var trade_dateDate = new Date(lit).getDate();
									trade_dateDate = "0" + trade_dateDate;
									trade_dateDate = trade_dateDate.slice(-2)
									var trade_dateHour = new Date(lit).getHours();
									trade_dateHour = "0" + trade_dateHour;
									trade_dateHour = trade_dateHour.slice(-2)
									var trade_dateMinute = new Date(lit).getMinutes();
									trade_dateMinute = "0" + trade_dateMinute;
									trade_dateMinute = trade_dateMinute.slice(-2)
									var trade_dateSecond = new Date(lit).getSeconds();
									trade_dateSecond = "0" + trade_dateSecond;
									trade_dateSecond = trade_dateSecond.slice(-2)
									var full = trade_dateYear + ". " + trade_dateMonth + ". " + trade_dateDate + ". " + trade_dateHour + ":" + trade_dateMinute + ":" + trade_dateSecond;
									var feeReal = trade.fee.replace(/[^0-9\.]/g, '');


								} else {
									units = Number(trade.units).toFixed(8);

									var trade_dateYear = new Date(lit).getFullYear();
									var trade_dateMonth = new Date(lit).getMonth() + 1;
									trade_dateMonth = "0" + trade_dateMonth;
									trade_dateMonth = trade_dateMonth.slice(-2)
									var trade_dateDate = new Date(lit).getDate();
									trade_dateDate = "0" + trade_dateDate;
									trade_dateDate = trade_dateDate.slice(-2)
									var trade_dateHour = new Date(lit).getHours();
									trade_dateHour = "0" + trade_dateHour;
									trade_dateHour = trade_dateHour.slice(-2)
									var trade_dateMinute = new Date(lit).getMinutes();
									trade_dateMinute = "0" + trade_dateMinute;
									trade_dateMinute = trade_dateMinute.slice(-2)
									var trade_dateSecond = new Date(lit).getSeconds();
									trade_dateSecond = "0" + trade_dateSecond;
									trade_dateSecond = trade_dateSecond.slice(-2)
									var full = trade_dateYear + ". " + trade_dateMonth + ". " + trade_dateDate + ". " + trade_dateHour + ":" + trade_dateMinute + ":" + trade_dateSecond;
									var feeReal = trade.fee

								}


								if (trade.incomeCal > 0) {
									var incomeCalColor = "text-danger";
								} else if (trade.incomeCal < 0) {
									var incomeCalColor = "text-primary";
								}
								if (trade.income > 0) {
									var incomeColor = "text-danger";
								} else if (trade.income < 0) {
									var incomeColor = "text-primary";
								}


								if (trade.type.toString() == "매수") {
									var typeColor = "text-danger";

								} else if (trade.type.toString() == "매도") {
									var typeColor = "text-primary";
								}
								if (trade.revenue > 0) {
									var tradeColor = "text-danger";
								} else if (trade.revenue < 0) {
									var tradeColor = "text-primary";
								}
								if (trade.type.toString() == "매수" || trade.type.toString() == "입금") {
									avgPrice = bidAvgPrice;
									accUnits = bidAccUnits;
									totalPriceCal = bidTotalPriceCal;

								} else if (trade.type.toString() == "매도" || trade.type.toString() == "출금") {
									avgPrice = askAvgPrice;
									accUnits = askAccUnits;
									totalPriceCal = askTotalPriceCal;
								}


								var html = "<tr class='tr_visible'><td style='text-align: center;'>" + num + "</td>" +
									"<td style='text-align: center;'>" + trade.site + "</td>" +
									"<td style='text-align: center;'>" + trade.currency + "</td>" +
									"<td id='dateId' style='text-align: center;'>" + full + "</td>" +
									"<td class=" + typeColor + " style='text-align: center;'>" + trade.type + "</td>" +
									/*"<td style='text-align: right;'>" + byc(parseFloat(price)) + "</td>" +*/
									/*"<td style='text-align: right;'>" + byc(units) + "</td>" +*/
									"<td style='text-align: right;'>" + byc((parseFloat(trade.units * 1000) / 1000).toFixed(4)) + "</td>" +
									"<td style='text-align: right;'>" + byc(Number(avgPrice).toFixed(0)) + "</td>" +
									"<td style='text-align: right;'>" + byc(Number(feeReal).toFixed(2)) + "</td>" +
									/*"<td style='text-align: right;'>" + byc(Number(totalPrice).toFixed(0)) + "</td>" +*/
									"<td style='text-align: right;'>" + byc(Number(totalPriceCal).toFixed(0)) + "</td>" +
									"<td class=" + incomeCalColor + " style='text-align: right;'>" + byc(incomeCal) + "</td>" +
									/*"<td class=" + incomeColor + " style='text-align: right;'>" + byc(income) + "</td>" +*/
									"<td class=" + tradeColor + " style='text-align: right;'>" + revenue + "</td>" +
									/*"<td>" + '<input type="text" id ="logId' + trade.orderId + '" value="' + log + '" style="width:75%"  placeholder="' + log + '">' +*/
									'<input type="button" onclick="updateLog(\'' + trade.orderId + '\')"  class="btn "  value= "저장" style="font-size:13px;padding: .3rem .7rem;width:40px">' +
									"</td><td style='text-align: center;'>" +  + "</td></tr>";

								$('#myTable').append(html)

								num--;
								if (income == null) {
									imcome = 0;
								}
								totincome = Number(totincome) + Number(income);

							},
							error: function(json) {
								console.log('실패');
							}
						});



					});


					pageNext = page + 1;

					pageQuotient = parseInt(page / 10);
					pageRemainder = page % 10;
					pageLast = json.totalPages - 1;
					pageLastNum = parseInt(json.totalPages / 10);
					$('#page').html("");

					if (pageQuotient == 0) {
						$('#page').append('<li class="page-item"><a class="page-link" onclick="trade2(0)">first</a></li>');

						$('#page').append('<li class="page-item disabled"><a class="page-link"">Previous</a></li>');
					} else {
						$('#page').append('<li class="page-item"><a class="page-link" onclick="trade2(0)">first</a></li>');

						pagePrevious = (pageQuotient * 10) - 10;
						$('#page').append('<li class="page-item"><a class="page-link" onclick="trade2(' + pagePrevious + ')">Previous</a></li>');

					}

					for (i = 0; i < pageRemainder; i++) {



						pageNum = (pageQuotient * 10) + i;
						pageNumNext = pageNum + 1;
						$('#page').append('<li class="page-item"><a class="page-link" onclick="trade2(' + pageNum + ')">' + pageNumNext + '</a></li>');

					}
					//17
					$('#page').append('<li class="page-item"><a class="page-link"style = "color : red; onclick="trade2(' + page + ')">' + pageNext + '</a></li>');


					for (i = 1; i < 10 - pageRemainder; i++) {

						pageNum = page + i;
						pageNumNext = pageNum + 1;
						//18 >= 17
						if (pageNum > pageLast) break;
						$('#page').append('<li class="page-item"><a class="page-link" onclick="trade2(' + pageNum + ')">' + pageNumNext + '</a></li>');


					}


					if (pageLastNum == pageQuotient) {
						//$('#page').append('<li class="page-item disabled"><a class="page-link"">Next</a></li>');
						$('#page').append('<li class="page-item"><a class="page-link" onclick="trade2(' + pageLast + ')">last</a></li>');

					} else {


						pageNext = (pageQuotient * 10) + 10;


						$('#page').append('<li class="page-item"><a class="page-link" onclick="trade2(' + pageNext + ')">Next</a></li>');
						$('#page').append('<li class="page-item"><a class="page-link" onclick="trade2(' + pageLast + ')">last</a></li>');

					}
					$('#myTablesum').html("");
					$('#myTablesum').append(byc(totincome));
					totincome = 0;

				},
				error: function(json) {
					alert('에러' + json.status + ',' + json.textStatus);
				}
			});

		},
		error: function(json) {
			alert('에러' + json.status + ',' + json.textStatus);
		}
	});
}

/*$(document).on('click', '.tr_visible a', function() {

	//const test = document.getElementById('.plus');
		var value = $(this).hasClass('minus');
	
		alert(value);
		$(".minus").show();

	if ($(this).hasClass('minus')) {
		$(".minus").hide();
		$(".plus").show();
	} else {
		$(".plus").hide();
		$(".minus").show();
	}
	$(this).parent().parent().nextUntil(".tr_visible").toggle();


});*/


function insertPrice(site) {

	$.ajax({
		url: '/board/reportForm/state',
		type: 'post',
		data: {
			site: site
		},
		dataType: 'json',
		//global: false,
		success: function(j) {
			if (j.state == false) {
				window.open("/board/reportForm/" + site + "/bankstatement", "window팝업", "width=1700,height=800,menubar=no,status=no,toolbar=no")
			} else {
				alert("기다려주세용");
			}

		},
		error: function(j) {

		}
	});





}

function allrevenue(site) {
	$.ajax({
		url: '/board/reportForm/state',
		type: 'post',
		data: {
			site: site
		},
		dataType: 'json',
		//global: false,
		success: function(j) {
			console.log(j.state);
			if (j.state == false) {
				$.ajax({
					url: '/board/showAllRevenue',
					type: 'post',
					data: {
						site: site
					},
					dataType: 'text',
					//global: false,
					success: function(j) {

						alert('수익률 계산이 완료되었습니다.');
						location.reload();


					},
					error: function(j) {

						alert('실패');
						location.reload();

					}
				});

			} else {
				alert('데이터 불러오는중');
			}

		},
		error: function(j) {

			alert('실패');

		}
	});



}
var timerRevenueNum = 0;
function rerevenue(site) {

	$.ajax({
		url: '/board/reportForm/state',
		type: 'post',
		data: {
			site: site
		},
		dataType: 'json',
		//global: false,
		success: function(j) {

			if (j.state == false) {
				timerRevenueNum=1;
				timer2 = setInterval(function() {
					revenueTime();
				}, 1000);
				$.ajax({
					url: '/board/showNewRevenue',
					type: 'post',
					data: {
						site: site
					},
					dataType: 'text',
					//global: false,
					success: function(j) {

						 //alert('수익률 계산이 완료되었습니다.');
						//location.reload();

					},
					error: function(j) {

						$.ajax({
							url: '/board/RevenueStateError/' + $('#site').val(),
							type: 'post',
							async: false,
							dataType: 'json',
							global: false,
							success: function(json) {
								alert('다시 시도해주세요.');
								location.reload();
							},
							error: function(json) {
								alert('에러' + json.status + ',' + json.textStatus);
							}
						});



					}
				});

			} else {
				alert('데이터 불러오는중');
			}

		},
		error: function(j) {

			alert('실패');

		}
	});

}
function revenueTime() {
	$.ajax({
		url: '/board/RevenueState/' + $('#site').val(),
		type: 'post',
		async: false,
		dataType: 'json',
		global: false,
		success: function(json) {
			
			if (json.state == '0') {
				document.getElementById('roadingStr').innerHTML = "";
				
				$('#loading').hide();
				clearInterval(timer2);
				if (timerRevenueNum > 0) {
					timerRevenueNum=0;
					alert('수익률 계산이 완료되었습니다.');
					location.reload();
				}
				
			} else {
				$('#loading').hide();
				timerRevenueNum++;
				document.getElementById('roadingStr').innerHTML = "수익률 계산 중입니다. (" + json.num + "/" + json.totalNum + ")";
			}
		},
		error: function(json) {
			alert('에러' + json.status + ',' + json.textStatus);
		}
	});
}