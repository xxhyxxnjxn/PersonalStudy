<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1">
<title>메인 페이지</title>
<!-- Favicon icon -->
<link rel="icon" type="image/png" sizes="16x16"
	href="/images/favicon.png">
<!-- Custom Stylesheet -->
<link href="/css/style.css" rel="stylesheet">

<style type="text/css">
.switch {
	position: relative;
	display: inline-block;
	width: 60px;
	height: 34px;
	vertical-align: middle;
}

/* Hide default HTML checkbox */
.switch input {
	display: none;
}

/* The slider */
.slider {
	position: absolute;
	cursor: pointer;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	background-color: #ccc;
	-webkit-transition: .4s;
	transition: .4s;
}

.slider:before {
	position: absolute;
	content: "";
	height: 26px;
	width: 26px;
	left: 4px;
	bottom: 4px;
	background-color: white;
	-webkit-transition: .4s;
	transition: .4s;
}

input:checked+.slider {
	background-color: #2196F3;
}

input:focus+.slider {
	box-shadow: 0 0 1px #2196F3;
}

input:checked+.slider:before {
	-webkit-transform: translateX(26px);
	-ms-transform: translateX(26px);
	transform: translateX(26px);
}

/* Rounded sliders */
.slider.round {
	border-radius: 34px;
}

.slider.round:before {
	border-radius: 50%;
}

p {
	margin: 0px;
	display: inline-block;
	font-size: 15px;
	font-weight: bold;
}
</style>
<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>

<script type="text/javascript">

var tdSite_Val;
var tdCurrency_Val;

var timer;

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

function symbol_state(symbol){

	$.ajax({
		url : 'arbi_symbol_ck',
		type : 'get',
		dataType : 'json',
		async:false,
		data : {symbol : symbol, symbol_ck : $('#'+symbol).prop( 'checked')},
		success : function(json) {
			
			if($( '#'+symbol).prop( 'checked') == true){
				alert(symbol+' 재정거래를 실행합니다');
			}else{
				alert(symbol+' 재정거래를 중지합니다');
			}

		},
		error : function(json) {
			//alert('에러' + json.status + ',' + json.textStatus);
		}
	});
}; 

function state(bot_name){
	
	if($('#'+bot_name).prop('checked') == true){
		alert($('#'+bot_name).prop('checked'));
		
		$.ajax({
			url : 'update_bot_state',
			type : 'get',
			dataType : 'json',
			async:false,
			data : {site : tdSite_Val, currency : tdCurrency_Val, bot_ck : "true", bot_name : bot_name},
			success : function(json) {
				
			},
			error : function(json) {
				//location.reload();
				//alert('에러' + json.status + ',' + json.textStatus);
			}
		});
		
	}else if($('#'+bot_name).prop('checked') == false){
		alert($('#'+bot_name).prop('checked'));
		
		$.ajax({
			url : 'update_bot_state',
			type : 'get',
			dataType : 'json',
			async:false,
			data : {site : tdSite_Val, currency : tdCurrency_Val, bot_ck : "false", bot_name : bot_name},
			success : function(json) {
			},
			error : function(json) {
				//location.reload();
				//alert('에러' + json.status + ',' + json.textStatus);
			}
		});
	}
} 

function update_bot_state(x){

	tdSite_Val = document.getElementsByClassName("tdSite_Val")[x.rowIndex-1].innerHTML;
	tdCurrency_Val = document.getElementsByClassName("tdCurrency_Val")[x.rowIndex-1].innerHTML;
	
} 

$(document).ready(function() {
	
	show_bot();
	timer = setInterval(show_bot,1000);

});

function show_bot(){

	$.ajax({
		url : 'show_bot',
		type : 'get',
		dataType : 'json',
		async:false,
		success : function(json) {
			$('#bot_switch').html("");
			
			$('#arbi_mem_ck').html("");
			
			$.each(json,function(index,item){
				  
				 var bitumb = "bithumb";
				 var upbit = "upbit";
				 var coinone = "coinone";

				 var total_krw = '<td></td>';
				 var total_krw_cal = '<td></td>';
				 var total_units = '<td></td>';
				 
				if(item.site=="bithumb"){
					if(item.bot_ck=="1"){
						item.bot_ck = '<label class="switch">  <input type="checkbox" id = "'+item.bot_name+'" checked="true" onclick="state(\''+item.bot_name+'\')"> <span class="slider round"></span>';
					}
					else{
						item.bot_ck = '<label class="switch">  <input type="checkbox" id = "'+item.bot_name+'" onclick="state(\''+item.bot_name+'\')"> <span class="slider round"></span>';
					}
				}

				if(item.site=="upbit"){
					if(item.bot_ck=="1"){
						item.bot_ck = '<label class="switch">  <input type="checkbox" id = "'+item.bot_name+'" checked="true" onclick="state(\''+item.bot_name+'\')"> <span	class="slider round"></span>';
					}
					else{
						item.bot_ck = '<label class="switch">  <input type="checkbox" id = "'+item.bot_name+'" onclick="state(\''+item.bot_name+'\')"> <span class="slider round"></span>';
					}

				}

				if(item.site=="coinone"){
					if(item.bot_ck=="1"){
						item.bot_ck = '<label class="switch">  <input type="checkbox" id = "'+item.bot_name+'" checked="true" onclick="state(\''+item.bot_name+'\')"> <span	class="slider round"></span>';
					}
					else{
						item.bot_ck = '<label class="switch">  <input type="checkbox" id = "'+item.bot_name+'" onclick="state(\''+item.bot_name+'\')"> <span class="slider round"></span>';
					}

				}

				$.ajax({
					url : 'bot_units',
					type : 'get',
					dataType : 'json',
					async:false,
					data : {site : item.site, bot_name : item.bot_name, currency : item.currency},
					success : function(json) {
						console.log(json.units)
						total_units ='<td>'+json.units+'</td>';
					},
					error : function(json) {
						//alert('에러' + json.status + ',' + json.textStatus);
					}
				});
				
				$.ajax({
					url : 'site_balance',
					type : 'get',
					dataType : 'json',
					async:false,
					data : {site : item.site, bot_name : item.bot_name, currency : item.currency},
					success : function(json) {

						if(item.site=="bithumb"){
							var krw = String(json.data.available_krw);
						}else if(item.site=="upbit"){
							var krw = String(json.bid_account.balance);
						}else if(item.site=="coinone"){
							var krw = String(json.krw.avail);
						}
						
						var strArray = krw.split("."); // 소수점 앞 정수만 출력

						total_krw ='<td>'+byc(Number(strArray[0]).toFixed(0))+'</td>';
						total_krw_cal ='<td>'+byc(Number(strArray[0]*item.set_per).toFixed(0))+'</td>';
					},
					error : function(json) {
						//alert('에러' + json.status + ',' + json.textStatus);
					}
				});
				

				
				$('#bot_switch').append("<tr onclick='update_bot_state(this)'><td>"+item.bot_ck+"</td></tr>");
				
				$('#arbi_mem_ck').append("<tr id='bot_data' height=69><td class='tdSite_Val'>"+item.site+"</td>"
						 +"<td class='tdCurrency_Val'>"+item.currency+"</td>"
						 +"<td>"+item.position+"</td>"
						 +total_units
						 +total_krw
						 +"<td>"+item.set_per*100+"% </td>"
						 +total_krw_cal
						 +"</tr>"
						);
				 
			 });
			
			
		},
		error : function(json) {
			alert('에러' + json.status + ',' + json.textStatus);
		}
	});
}

$(document).on('click','#bot_data',function(){
	var tr=$(this);
 	var td=tr.children();

 	var bot_site = td.eq(0).text();
 	var currency = td.eq(1).text();

 	document.location.href = "/symbol_transaction?bot_site="+bot_site+"&currency="+currency;
 	
});

</script>

</head>
<body>

	<!--*******************
        Preloader start
    ********************-->
	<div id="preloader">
		<div class="loader">
			<svg class="circular" viewBox="25 25 50 50">
                <circle class="path" cx="50" cy="50" r="20" fill="none"
					stroke-width="3" stroke-miterlimit="10" />
            </svg>
		</div>
	</div>
	<!--*******************
        Preloader end
    ********************-->


	<!--**********************************
        Main wrapper start
    ***********************************-->
	<div id="main-wrapper">
		<!--헤더, 사이드바 -->

		<jsp:include page="../inc/header.jsp" />

		<jsp:include page="../inc/sidebar.jsp" />


		<!--**********************************
            Content body start
        ***********************************-->
		<div class="content-body">

			<div class="row page-titles mx-0">
				<div class="col p-md-0">
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="javascript:void(0)">Dashboard</a></li>
						<li class="breadcrumb-item active"><a
							href="javascript:void(0)">Home</a></li>
					</ol>
				</div>
			</div>
			<div class="container-fluid" style="float:right;">
			<a href = "symbol_add"  style="float:right; font-size:20px;">추가하기<img src="/images/avatar/add.png"></a>
			</div>
			<!-- row -->
			<div class="container-fluid">

				<!-- /# row -->
				<div class="row">
					<!-- Polar Chart -->
					<div class="col-lg-12">
						<div class="card">
							<div class="card-body">
								<h5 class="card-title">생성한 봇</h5>
								<!-- 재정거래 <label class="switch">  <input type="checkbox" id = "bot_ck"> 
									<span	class="slider round"></span>
								</label> -->
								<br>
								
								<div style="width:20%;float:left;">
								<table class="table table-hover" id="onoff-table">
									<thead>
										<tr>
											<th class="text-left" style="position: sticky; top: 0px; background-color: #9999FF;"><strong class="text-white">ON/OFF</strong></th>
										</tr>
									</thead>
									<tbody id="bot_switch" name="">
									</tbody>
								
								</table>
								</div>
								
								<div style="width:80%;float:right;">
								<table class="table table-hover" id="orderbook-table">
									<thead>
										<tr>
											<!-- <th class="text-center" style="position: sticky; top: 0px; background-color: #9999FF;"><strong class="text-white">on/off</strong></th> -->
											<th class="text-left"	style="position: sticky; top: 0px; background-color: #9999FF;"><strong class="text-white">거래소</strong></th>
											<th class="text-left"	style="position: sticky; top: 0px; background-color: #9999FF;"><strong class="text-white">코인</strong></th>
											<th class="text-left"	style="position: sticky; top: 0px; background-color: #9999FF;"><strong class="text-white">position</strong></th>
											<th class="text-left"	style="position: sticky; top: 0px; background-color: #9999FF;"><strong class="text-white">주문 수량</strong></th>
											<th class="text-left"	style="position: sticky; top: 0px; background-color: #9999FF;"><strong class="text-white">내 총 자산</strong></th>
											<th class="text-left"	style="position: sticky; top: 0px; background-color: #9999FF;"><strong class="text-white">설정값</strong></th>	
											<th class="text-left"	style="position: sticky; top: 0px; background-color: #9999FF;"><strong class="text-white">내 총 자산 * 설정값</strong></th>
											
										</tr>
									</thead>
									<tbody id="arbi_mem_ck" name="">
									</tbody>
								
								</table>
								</div>
								<!-- <a href = "symbol_add">재정거래 코인 추가하기</a>  -->
								<!-- 코인 추가 / <a onclick = "update()">수정</a> 하기 -->
		
							</div>
						</div>
					</div>
				</div>
			</div>


		</div>
	</div>
	<!-- #/ container -->
	</div>
	
	<!--**********************************
            Content body end
        ***********************************-->


	<!--**********************************
            Footer start
        ***********************************-->
	<div class="footer">
		<div class="copyright">
			<p>
				Copyright &copy; Designed & Developed by <a
					href="https://themeforest.net/user/quixlab">Quixlab</a> 2018
			</p>
		</div>
	</div>
	<!--**********************************
            Footer end
        ***********************************-->
	</div>
	<!--**********************************
        Main wrapper end
    ***********************************-->

	<!--**********************************
        Scripts
    ***********************************-->
	<script src="/plugins/common/common.min.js"></script>
	<script src="/js/custom.min.js"></script>
	<script src="/js/settings.js"></script>
	<script src="/js/gleek.js"></script>
	<script src="/js/styleSwitcher.js"></script>

	<script src="/plugins/chart.js/Chart.bundle.min.js"></script>


</body>

</html>
