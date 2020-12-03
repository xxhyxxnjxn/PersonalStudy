<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" integrity="sha384-JcKb8q3iqJ61gNV9KGb8thSsNjpSL0n8PARn9HuZOnIxN0hoP+VmmDGMN5t9UJ0Z" crossorigin="anonymous">
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js" integrity="sha384-9/reFTGAW83EW2RDu2S0VKaIzap3H66lZH81PoYlFhbGU+6BZp6G7niu735Sk7lN" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js" integrity="sha384-B4gt1jrGC7Jh4AgTPSdUtOBvfO8shuf57BaghqFfPlYxofvL8/KUEfYiJOMMV+rV" crossorigin="anonymous"></script>
<link rel="stylesheet" href="/css/login.css"/>
<title>Insert title here</title>
</head>
<body>
	<main class="d-flex align-items-center min-vh-100 py-3 py-md-0">
		<div class="container">
			<div class="card login-card">
				<div class="row no-gutters">
					<div class="col-md-5">
						<img src="/img/1.gif" alt="login" class="login-card-img">
					</div>
					<div class="col-md-7">
						<div class="card-body" style="height: 700px; padding-top: 20%; padding-left: 25%;">
							<p class="login-card-description">아이디 찾기</p>
							<div class="form-group">
								아이디는 ${find.m_id } 입니다.
							</div>
							<br>
							<br>
							<br>
							<br>
							<br>
							<form action="/FindPWFrom" method="POST">
               					<input class="btn btn-block login-btn mb-4" type="submit" value="비밀번호 찾기">
              				</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</main>
</body>
</html>