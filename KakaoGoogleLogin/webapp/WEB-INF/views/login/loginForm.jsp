<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<!-- 
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
 -->
<!--  -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <title>Quixlab - Bootstrap Admin Dashboard Template by Themefisher.com</title>
    <!-- Favicon icon -->
    <link rel="icon" type="/image/png" sizes="16x16" href="/images/favicon.png">
    <link href="/css/style.css" rel="stylesheet">
        <!--**********************************
        Scripts
    ***********************************-->
    <script src="/plugins/common/common.min.js"></script>
    <script src="/js/custom.min.js"></script>
    <script src="/js/settings.js"></script>
    <script src="/js/gleek.js"></script>
    <script src="/js/styleSwitcher.js"></script>

<!--  -->

<title>Insert title here</title>

</head>
<body class="h-100">
    
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

    



    <div class="login-form-bg h-100">
        <div class="container h-100">
            <div class="row justify-content-center h-100">
                <div class="col-xl-6">
                    <div class="form-input-content">
                        <div class="card login-form mb-0">
                            <div class="card-body pt-5">
                            
                                <a class="text-center" href="index.html"> <h4>Login</h4></a>
        
                                <form class="mt-5 mb-5 login-input" action="/loginProcess" method="post">
                                    <div class="form-group">
                                        <input type="text" class="form-control" placeholder="아이디 입력" name = "m_id" id = "m_id">
                                        
                                    </div>
                                    <div class="form-group">
                                        <input type="password" class="form-control" placeholder="암호" name ="m_pw" id = "m_pw">
                                    </div>
                                    <input type="submit" class="btn login-form__btn submit w-100" value="로그인">
                                    <!-- <button class="btn login-form__btn submit w-100">Sign In</button> -->
                                </form>
                                <p class="mt-5 login-form__footer">Dont have account? <a href="/JoinForm" class="text-primary">Sign Up</a> now</p>
                            	<p class="mt-5 login-form__footer">kakao? <a href="https://kauth.kakao.com/oauth/authorize?client_id=7adab7a284d33aebf8f758a613b2ee76&redirect_uri=http://localhost:8090/kakaologin&response_type=code" class="text-primary">카카오로 로그인</a> now</p>
                           		
                           		<div id="googleLoginBtn" style="cursor: pointer"> <p class="mt-5 login-form__footer">google? <a href="#" class="text-primary">구글로 로그인</a> now</p></div>
                           
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    

    


</body>


<script>
 	const onClickGoogleLogin = (e) => {
    	//구글 인증 서버로 인증코드 발급 요청
 		window.location.replace("https://accounts.google.com/o/oauth2/v2/auth?client_id=224677599374-p7ep3bont28baeu6l829pg50g74r96o1.apps.googleusercontent.com&redirect_uri=http://localhost:8090/googleAuth&response_type=code&scope=email%20profile%20openid&access_type=offline")
 	}
	
	const googleLoginBtn = document.getElementById("googleLoginBtn");
	googleLoginBtn.addEventListener("click", onClickGoogleLogin);
    
</script>
</html>