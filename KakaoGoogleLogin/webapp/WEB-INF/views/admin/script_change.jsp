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
			<!-- row -->
			<div class="container-fluid">

				<!-- /# row -->
				<div class="row">
					<!-- Polar Chart -->
					<div class="col-lg-12">
						<div class="card">
							<div class="card-body">
								<form action="script_update" method="post">
									<div>
										<h5 class="card-title">스크립터 넘버</h5>
										<input type="text" id="set_scriptNo" name ="set_scriptNo" class="form-control"
											placeholder="ScriptNo" required="required"
											style="width: 450px" value="${script.scriptNo}"> <br>
										
										<h5 class="card-title">사이드</h5>
										<input type="text" id="side" name ="side" class="form-control"
											placeholder="Side" required="required"
											style="width: 450px" value="${script.side}">
										
										<h5 class="card-title">사이드 넘버</h5>
										<input type="text" id="side_num" name ="side_num" class="form-control"
											placeholder="Side_Num" required="required"
											style="width: 450px" value="${script.side_num}">
										<input type = "text" name="scriptNo" value="${script.scriptNo}" hidden>
										
									</div>
									<br> <input type="submit"
										class="btn mb-1 btn-outline-success" value="변경하기" />
								</form>
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
