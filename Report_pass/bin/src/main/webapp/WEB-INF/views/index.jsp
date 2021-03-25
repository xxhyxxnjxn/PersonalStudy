<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="layout/header.jsp"%>

<div class="container">
<form action="/auth/loginProc" method="post">
		<div class="form-group">
			<label for="memId">Id</label> 
			<input type="text" name="username" class="form-control" placeholder="Enter Id" id="memId">
		</div>
		<div class="form-group">
			<label for="pwd">Password:</label> 
			<input type="password" name="password" class="form-control" placeholder="Enter password" id="password">
		</div>
	 	<div class="form-group form-check">
			<label class="form-check-label"> 
			<input name="remember" class="form-check-input" type="checkbox"> Remember me
			</label>
		</div> 
		<button id="btn-login" class="btn btn-primary">로그인</button>
	</form>
</div>
<script src="/static/js/user.js"></script>
<%@ include file="layout/footer.jsp"%>
</body>
</html>

