<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="../layout/header.jsp"%>

<div class="container">
	<div class="card m-2">

			<h4 class="card-title" style="text-align: center;">계정 수정</h4>
				<input type="text"class="form-control" id="site" name="site" value = "${apiDto.site}" readonly="readonly" >
				<p>Connect Key</p>
				<input type="text" class="form-control" id ="apiKey" name = "apiKey" placeholder="Connect key"value="${apiDto.apiKey }">
				<p>Secret Key</p>
				<input type="text" class="form-control" id ="secretKey"	name = "secretKey" placeholder="Secret Key">
				</br>
				<button type="button" class="btn btn-primary"  id="btn-auth" onclick="auth()">API Key 인증</button>
				<br>
				<button type="button" class="btn btn-primary"  id="btn-insert" onclick="update()">수정하기</button>
		</div>
</div>

<script src="/static/js/apiAuth.js"></script>

<%@ include file="../layout/footer.jsp"%>
</body>
</html>



