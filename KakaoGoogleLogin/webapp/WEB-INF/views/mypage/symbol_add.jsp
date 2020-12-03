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

	$.ajax({
		url : 'site_tbl',
		type : 'get',
		dataType : 'json',
		success : function(json) {

			$.each(json,function(index,item){

		    	$('#site').append("<option value='"+item.site+"'>"+item.site+"</option>");
		    	$("#reg_submit").attr("disabled", true);
		    	
			});
			
			
		},
		error : function(json) {
			alert('에러' + json.status + ',' + json.textStatus);
		}
	});

	$('#apiKey').blur(function(){
		
		$.ajax({
			url      : 'check_api_tbl',
			type     : 'GET',
			dataType : 'json',
			data: {apiKey : $('#apiKey').val() } ,
			success  : function(data){
				if(data==1){
					alert("이미 존재하는 apiKey 입니다.");
					$("#secretKey").attr("disabled", true);
				}else{
					$("#secretKey").attr("disabled", false);
				}
			},
			error    : function(xhr){
				alert('err' + xhr.status);
			}
		});
	});


});

	function api_exist() {

		$.ajax({
			url : 'check_api',
			type : 'get',
			dataType : 'json',
			data : {
				site : $('#site').val(),
				currency : $('#coin').val(),
				apiKey : $('#apiKey').val(),
				secretKey : $('#secretKey').val()
			},
			success : function(json) {

				if (json.error != undefined) {
					alert("업비트에 등록되지 않은 api키 입니다.");
				} else if (json.result == "error") {
					alert("코인원에 등록되지 않은 api키 입니다.")
				} else {
					alert("확인되었습니다.");
					
					$("#reg_submit").attr("disabled", false);

				}

			},
			error : function(xhr) {
				alert("빗썸에 등록되지 않은 api키 입니다.")
			}
		});

	};

	function add() {
		//alert($('#site').val() +$('#coin').val() + $('#apiKey').val()+$('#secretKey').val());
		if($("#set_per").val() == ""){
			alert("빈칸을 입력하세요.");
		}else{
			
		$.ajax({
			url : 'insert_api_tbl',
			type : 'get',
			dataType : 'data',
			data : {
				site : $('#site').val(),
				apiKey : $('#apiKey').val(),
				secretKey : $('#secretKey').val(),
				currency : $('#coin').val(),
				set_per : $('#set_per').val() / 100
			},
			success : function(json) {

			},
			error : function(xrh) {
				alert("입력")
				location.href = "botCreate";
			}
		});
		
		}

	};
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
						<li class="breadcrumb-item active"><a href="javascript:void(0)">Home</a></li>
					</ol>
				</div>
			</div>
			<!-- row -->
			<div class="container-fluid">

				<!-- /# row -->
				<div class="row">
					<!-- Polar Chart -->
					<div class="col-lg-12">
						<div class="card">
							<div class="card-body">
								<div>
									<h5 class="card-title">거래소 선택</h5>

									<select class="form-control" id="site">
										<option>선택해주세요</option>

									</select>
								</div>
								<br>
								<div>
									<h5 class="card-title">코인 선택</h5>

									<select class="form-control" id="coin">
										<option>선택해주세요</option>
										<option>BTC</option>

									</select>
								</div>
								<br>
								<div>
									<h5 class="card-title">API KEY 입력</h5>
									API KEY : <input type="text" name="apiKey" id="apiKey"
										class="form-control" placeholder="apiKey" required="required">
									<br> SECRET KEY : <input type="text" name="secretKey"
										id="secretKey" class="form-control" placeholder="secretKey"
										required="required">
								</div>
								<br>
								<button type="button" class="btn mb-1 btn-outline-success"
									onclick="api_exist()">
									<b>API 체크하기</b>
								</button>
								<br>
								<div><br>
									<h5 class="card-title">자산 설정</h5>
									<input type="text" name="set_per" id="set_per"
										class="" placeholder="설정할 자산의 퍼센트를 입력해주세요" required="required" style="width:300px">%
								</div>
								<br>
								<br>
								<button type="button" class="btn mb-1 btn-outline-success"
									onclick="add()" id="reg_submit">
									<b>추가하기</b>
								</button>

							</div>
						</div>
					</div>
				</div>
			</div>

		</div>
	</div>
	<!-- #/ container -->

	
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
