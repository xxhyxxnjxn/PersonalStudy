<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp"%>

<div class="container">

	<form>
		<div class="form-group">
			<label for="username">Id</label> 
			<input type="text" class="form-control" placeholder="Enter id" id="username">
		</div>
		<div class="form-group">
			<label for="pwd">Password</label> 
			<input type="password" class="form-control" placeholder="Enter password" id="password">
		</div>
		<div class="form-group">
			<label for="phone">Phone</label> 
			<input type="text" class="form-control" placeholder="Enter phone" id="phone">
		</div>
		<div class="form-group">
			<label for="name">name</label> 
			<input type="text" class="form-control" placeholder="Enter name" id="name">
		</div>
		<div class="form-group">
			<label for="email">Email</label> 
			<input type="email" class="form-control" placeholder="Enter email" id="email">
		</div>

	</form>
	<button id="btn-save" class="btn btn-primary">가입하기</button>

</div>

</body>
</html>

<script src="/js/user.js"></script>
<%@ include file="../layout/footer.jsp"%>
