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

<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>

<script type="text/javascript">


function scriptNo_update(scriptNo){
	var result = confirm( scriptNo+'스크립트를 수정하시겠습니까?');
		
		if(result) { //yes 
			document.location.href = "/script_change?scriptNo="+scriptNo;
		} else {
			//no 
		}
}

function scriptNo_delete(scriptNo){
	var result = confirm(scriptNo+'재정거래를 삭제합니다.');
	if(result) { //yes 
		$.ajax({
			url : 'script_delete',
			type : 'get',
			dataType : 'json',
			data : {scriptNo : scriptNo},
			success : function(json) {	
				$('#scriptlist').html('');
				
				$.each(json,function(i,item){
					$('#scriptlist').append('<tr><td id ="'+item.scriptNo+'">'+item.scriptNo+'</td><td>'+item.side+'</td><td>'+item.side_num+'</td><td>'+item.log+'</td>'
							+'<td><a href = "#" onclick = "scriptNo_update(\''+item.scriptNo+'\')" class="fa fa-pencil color-muted m-r-5"></a> \t<a href="#" onclick = "scriptNo_delete(\''+item.scriptNo+'\')" class="ti-trash"></a></td></tr>');
				});
				
			},
			error : function(json) {
				alert('에러' + json.status + ',' + json.textStatus);
			}
		});
		
	} else {
		//no 
	}

	
	
}; 




$(document).ready(function() {

		$.ajax({
			url : 'scriptlist',
			type : 'get',
			dataType : 'json',
			success : function(json) {
				
				$.each(json,function(i,item){
					$('#scriptlist').append('<tr><td id ="'+item.scriptNo+'">'+item.scriptNo+'</td><td>'+item.side+'</td><td>'+item.side_num+'</td><td>'+item.log+'</td>'
							+'<td><a href = "#" onclick = "scriptNo_update(\''+item.scriptNo+'\')" class="fa fa-pencil color-muted m-r-5"></a> \t<a href="#" onclick = "scriptNo_delete(\''+item.scriptNo+'\')" class="ti-trash"></a></td></tr>');
				});
				
			},
			error : function(json) {
			}
		});



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
			<div class="container-fluid">
				<a href="script_add">추가하기</a>
			</div>
			<!-- row -->
			<div class="container-fluid">

				<!-- /# row -->
				<div class="row">
					<!-- Polar Chart -->
					<div class="col-lg-12">
						<div class="card">
							<div class="card-body">
								<h5 class="card-title">스크립트 목록</h5>
								<!-- 재정거래 <label class="switch">  <input type="checkbox" id = "bot_ck"> 
									<span	class="slider round"></span>
								</label> -->
								<br>



								<table class="table table-hover" id="script">
									<thead>
										<tr>
											<th
												style="position: sticky; top: 0px; background-color: #9999FF;"><strong
												class="text-white">ScriptNo</strong></th>
											<th
												style="position: sticky; top: 0px; background-color: #9999FF;"><strong
												class="text-white">SIDE</strong></th>
											<th
												style="position: sticky; top: 0px; background-color: #9999FF;"><strong
												class="text-white">SIDE NUM</strong></th>
											<th
												style="position: sticky; top: 0px; background-color: #9999FF;"><strong
												class="text-white">log</strong></th>
											<th
												style="position: sticky; top: 0px; background-color: #9999FF;"><strong
												class="text-white">수정 / 삭제</strong></th>
										</tr>
									</thead>
									<tbody id="scriptlist" name="">
									</tbody>

								</table>
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
