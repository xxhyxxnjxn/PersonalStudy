<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
	integrity="sha384-JcKb8q3iqJ61gNV9KGb8thSsNjpSL0n8PARn9HuZOnIxN0hoP+VmmDGMN5t9UJ0Z"
	crossorigin="anonymous">
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"
	integrity="sha384-9/reFTGAW83EW2RDu2S0VKaIzap3H66lZH81PoYlFhbGU+6BZp6G7niu735Sk7lN"
	crossorigin="anonymous"></script>
<script
	src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"
	integrity="sha384-B4gt1jrGC7Jh4AgTPSdUtOBvfO8shuf57BaghqFfPlYxofvL8/KUEfYiJOMMV+rV"
	crossorigin="anonymous"></script>
	
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
<script>
	$(function(){
		$('#m_id').blur(function(){
			var checkId = $('#m_id').val();
			var ID = checkId.length;
			
			$.ajax({
				url      : '/Join/CheckId?m_id=' + checkId,
				type     : 'GET',
				
				dataType : 'json',
				success  : function(data){
					
					if ( data == '1'){
						$("#id_check").text("사용중인 아이디입니다.");
						$("#id_check").css("color", "red");
						$("#m_id").focus();
						$("#reg_submit").attr("disabled", true);
					} else if(data == '0' && ID > 5){
						$("#id_check").text("사용 가능한 아이디입니다.");
						$("#id_check").css("color", "blue");
						$("#reg_submit").attr("disabled", false);
					}
					else if(data == '0' && ID < 6){
						$("#id_check").text("아이디는 6자 이상으로 입력 가능 합니다");
						$("#m_id").focus();
						$("#reg_submit").attr("disabled", true);
					}
					
				},
				error    : function(xhr){
					alert('err' + xhr.status);
				}
			});
		});
		
		$('#m_pw').blur(function(){
			var checkPw = $('#m_pw').val();
			var PW = checkPw.length;
			if(PW < 4 || PW > 12){
	               $("#pw_check").text("비밀번호는 4자 ~ 11자 로 입력 가능 합니다");
	               $("#pw_check").css("color", "red");
	               $("#m_pw").focus();
	               $("#reg_submit").attr("disabled", true);
	               return false;
	        }
			else{
				$("#pw_check").text("비밀번호 사용 가능");
				$("#pw_check").css("color", "blue");
				$("#reg_submit").attr("disabled", false);
			}
		});

		$('#m_pw_chk').blur(function(){
			var pw = $('#m_pw').val();
			var pw_chk = $('#m_pw_chk').val();
			if(pw != pw_chk){
				$("#pw_check2").text("비밀번호가 맞지않습니다.");
	            $("#pw_check2").css("color", "red");
	            $("#m_pw_chk").focus();
	            $("#reg_submit").attr("disabled", true);
			}else{
				$("#pw_check2").text("일치합니다.");
				$("#pw_check2").css("color", "blue");
				$("#reg_submit").attr("disabled", false);
			}
			
		});
		
	});
</script>

<title>Insert title here</title>
<style>
	.test {
		width: 100%;
		margin: 0;
		padding: 0;
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		/* align-items: center; */
	}
</style>

</head>
<body>
	<div class="login-form-bg h-500">
        <div class="container h-500">
            <div class="row justify-content-center h-400">
                <div class="col-xl-6">
                    <div class="form-input-content">
                        <div class="card login-form mb-0">
                            <div class="card-body pt-5">
                            
							<a class="text-center" href="index.html"> <h4>JOIN US</h4></a>
							<form class="mt-5 mb-5 login-input" action="/Join/Join" method="POST">
								<div class="form-group test">
									<input style="width: 100%" type="text" name="m_id"
										id="m_id" class="form-control" placeholder="ID"
										required="required"  />
									<!-- 	
									 <form:errors path="m_id" cssClass="error" />
								  	 -->
								  	<!--
									<input type="button" value="중복확인" id="btnCheck">
								-->
								
								</div>
								<div class="form-group">
								<div class="check_font" id="id_check"></div>
								</div>
								
								<div class="form-group">
									<input type="password" name="m_pw" id="m_pw"class="form-control"
										placeholder="비밀번호" required="required">
								</div>
								<div class="check_font" id="pw_check"></div>
								
								<div class="form-group">
								<input type="password" name="m_pw_chk" id="m_pw_chk"class="form-control"
										placeholder="비밀번호 확인" required="required">
								</div>
								<div class="check_font" id="pw_check2"></div>
								
								<div class="form-group">
									<input type="text" name="email" class="form-control"
										placeholder="이메일" required="required" />
								</div>
								<input class="btn login-form__btn submit w-100" id="reg_submit" type="submit" value="가입하기">
							</form>
							
							
                           
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    

	<!-- <form action="/Join/Join" method="POST">
      <table>
         <tr>
            <td>아이디</td>
            <td><input type="text" name="m_id" /></td>
         </tr>
         <tr>
            <td>이름</td>
            <td><input type="text" name="m_name" /></td>
         </tr>
         <tr>
            <td>비밀번호</td>
            <td><input type="text" name="m_pw" /></td>
         </tr>
         <tr>
            <td>전화번호</td>
            <td><input type="text" name="tel" /></td>
         </tr>
         <tr>
            <td>이메일</td>
            <td><input type="text" name="email" /></td>
         </tr>
         <tr>
            <td colspan="2">
               <input type="submit" value="가입하기" />
            </td>
         </tr>
      </table>
      <input type="hidden" name="lvl" value="1" />
   </form> -->
</body>
</html>