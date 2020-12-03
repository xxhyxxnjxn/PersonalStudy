<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<link rel="preconnect" href="https://fonts.gstatic.com">
<link href="https://fonts.googleapis.com/css2?family=Black+Han+Sans&family=Nanum+Pen+Script&family=Noto+Serif+KR&family=Special+Elite&family=Sunflower:wght@300&display=swap" rel="stylesheet">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1">
<title>메인 페이지</title>

    <!-- Favicon icon -->
<link rel="icon" type="image/png" sizes="16x16" href="/images/favicon.png">
<!-- Custom Stylesheet -->
<link href="/css/style.css" rel="stylesheet">

</head>

<%-- <body>
	<%@ include file="../include/nav.jsp"%>
	${login.m_name} <br>
	${login.m_id} <br>
	${login.m_pw} <br>
	${login.tel} <br>
	${login.email} <br>
	${login.tel} <br>
	<footer>
		<div class="foot" style="color: white; text-align:center;">
			<p>${login.m_name}</p>
		</div>
	</footer>
</body>
</html> --%>

<body>

    <!--*******************
        Preloader start
    ********************-->
    <div id="preloader">
        <div class="loader">
            <svg class="circular" viewBox="25 25 50 50">
                <circle class="path" cx="50" cy="50" r="20" fill="none" stroke-width="3" stroke-miterlimit="10" />
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
	
        <jsp:include page="inc/header.jsp"/>
        <jsp:include page="inc/sidebar.jsp"/>
        <!--**********************************
            Content body start
        ***********************************-->
        <div class="content-body">
            <div class="row page-titles mx-0">
                <div class="col p-md-0">
                    <ol class="breadcrumb">
                        <li class="breadcrumb-item"><a href="javascript:void(0)">불리고</a></li>
                        <li class="breadcrumb-item active"><a href="javascript:void(0)">Home</a></li>
                    </ol>
                </div>
            </div>
            
            
            <!-- row -->
            <div class="container-fluid">
               
               <h5  style="font-family: 'Sunflower', sans-serif;">여러분이 잠든사이, 자산을 불려드립니다.</h5>
               <h1  style="font-family: 'Sunflower', sans-serif;">불리고</h1>
               
                <!-- /# row -->
			<h3 class="card-title">비트코인 시세</h3>
           <div class="row">
           <div class="col-lg-6">
                    <!-- Bar Chart -->
                    <div class="tradingview-widget-container">
					  <div id="tradingview_023fc"></div>
					  <div class="tradingview-widget-copyright">TradingView 제공 <a href="https://kr.tradingview.com/symbols/NASDAQ-AAPL/" rel="noopener" target="_blank"><span class="blue-text">AAPL 차트</span></a></div>
					  <script type="text/javascript" src="https://s3.tradingview.com/tv.js"></script>
					  <script type="text/javascript">
					  new TradingView.widget(
					  {
					  "width": 1300,
					  "height": 600,
					  "symbol": "BTCKRW",
					  "interval": "D",
					  "timezone": "Etc/UTC",
					  "theme": "light",
					  "style": "1",
					  "locale": "kr",
					  "toolbar_bg": "#f1f3f6",
					  "enable_publishing": false,
					  "allow_symbol_change": true,
					  "container_id": "tradingview_023fc"
						});
					  </script>
					</div>
                 </div>
                    <div class="col-lg-6">
                        <div class="card">
                        </div>
                    </div>
            </div>
           <div class="row">

                    <div class="col-lg-0">
                        <div class="card">
                            <div class="card-body">
                                <h4 class="card-title">현재 봇 생성 연계된 거래소</h4>
                                <table>
                                	<tr><td></td><td><img class="" src="/images/avatar/bithumb.png" alt=""></td></tr>
                                	<tr><td></td><td><img class="" src="/images/avatar/coinone.PNG" alt=""></td></tr>
                                	<tr><td></td><td><img class="" src="/images/avatar/upbit.png" alt=""></td></tr>
                                </table>
                            </div>
                            <div class="card-body">
                                <h1 class="card-title">고객센터</h1>
                                <h4 class="card-title">언제나 상담 원하시면 연락주세요</h4>
                                <h4 class="card-title">저희가 고객님들을 위해 24시간 대기중입니다.</h4>
                                <img class="" src="/images/avatar/customer_.png">
                                <h4>전화 번호</h4>
                                <h4>010-2529-5017</h4>
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
                <p>Copyright &copy; Designed & Developed by <a href="https://themeforest.net/user/quixlab">GMC Labs Developers</a> 2020</p>
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
    <script src="/js/plugins-init/chartjs-init.js"></script>

</body>

</html>
