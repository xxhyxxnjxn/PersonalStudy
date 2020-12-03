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

$(document).ready(function() {
	

	show_botOrderlist();
	
	
});
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
 
function update(){
	document.location.href = "/symbol_update";

}

function show_botOrderlist(){
	$.ajax({
		url : 'show_botOrderlist',
		type : 'get',
		dataType : 'json',
		success : function(json) {
			$($(json).get().reverse()).each(function(index,item){
				if(item.side == "bid"){
					item.side ='<td><strong class="text-danger">매수</strong></td>';
				}else if(item.side == "ask"){
					item.side ='<td><strong class="text-primary">매도</strong></td>';
				}
				
				var rev =  Number(item.revenue)*100 ;
				if(rev==0){
				$('#orderlist_data').append("<tr><td>"+item.date+"</td>"
						 +"<td>"+byc(item.price)+"</td>"
						 +"<td>"+byc(item.units)+"</td>"
						 +item.side
						 +"<td>"+byc(item.tot_price)+"</td>"
						 +"<td>"+byc(item.fee)+"</td>"
						 
						 +"<td>"+"</td>"
						);
					
				}else{
				 if(rev.toFixed(2)<0){
						revTd ='<td><strong class="text-primary">';
					}else if(ev.toFixed(2)<0){
						revTd ='<td><strong class="text-danger">';
					}
				$('#orderlist_data').append("<tr><td>"+item.date+"</td>"
						 +"<td>"+byc(item.price)+"</td>"
						 +"<td>"+byc(item.units)+"</td>"
						 +item.side
						 +"<td>"+byc(item.tot_price)+"</td>"
						 +"<td>"+byc(item.fee)+"</td>"
									 
						 +revTd+rev.toFixed(2)+" % </strong></td>"
						);
				}
				 
			 });
			
			
		},
		error : function(json) {
			
		}
	});
}

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
			<div class="container-fluid">
			<a href = "symbol_update">수정하기/삭제하기</a>
			</div>
			<!-- row -->
			<div class="container-fluid">

				<!-- /# row -->
				<div class="row">
					<!-- Polar Chart -->
					<div class="col-lg-12">
						<div class="card">
							<div class="card-body">
								<h5 class="card-title">거래내역 보여주기</h5>
								
								사이트 : ${bot_site } 코인 : ${currency }
								
								<table class="table table-hover" id="orderbook-table">
									<thead>
										<tr>
											<th class="text-left" style="position: sticky; top: 0px; background-color: #9999FF;"><strong class="text-white">주문시간</strong></th>
											<th class="text-left"	style="position: sticky; top: 0px; background-color: #9999FF;"><strong class="text-white">가격</strong></th>
											<th class="text-left"	style="position: sticky; top: 0px; background-color: #9999FF;"><strong class="text-white">수량</strong></th>
											<th class="text-left"	style="position: sticky; top: 0px; background-color: #9999FF;"><strong class="text-white">매수/매도</strong></th>
											<th class="text-left"	style="position: sticky; top: 0px; background-color: #9999FF;"><strong class="text-white">가치(총액)</strong></th>	
											<th class="text-left"	style="position: sticky; top: 0px; background-color: #9999FF;"><strong class="text-white">수수료</strong></th>
											<th class="text-left"	style="position: sticky; top: 0px; background-color: #9999FF;"><strong class="text-white">수익률</strong></th>
										</tr>
									</thead>
									<tbody id="orderlist_data" name="">
									</tbody>
								
								</table>
								<!-- <button type="button" class="btn mb-1 btn-outline-success" onclick = "update()"><b>수정하기</b></button> -->
								
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
