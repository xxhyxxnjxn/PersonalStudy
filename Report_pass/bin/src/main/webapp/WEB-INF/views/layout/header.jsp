<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<sec:authorize access="isAuthenticated()"> 
  <sec:authentication property="principal" var="principal"/>
	<script>
		
	</script>
</sec:authorize>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>GmcLabs Report Project</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
  
</head>
<body>

<nav class="navbar navbar-expand-md bg-dark navbar-dark">
  <a class="navbar-brand" href="/">Gmc Labs</a>
  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#collapsibleNavbar">
    <span class="navbar-toggler-icon"></span>
  </button>
  <div class="collapse navbar-collapse" id="collapsibleNavbar">

  <c:choose>
  	<c:when test="${empty principal }">
  		<ul class="navbar-nav">
		      <li class="nav-item">
		        <a class="nav-link" href="/auth/loginForm">Login</a>
		      </li>
		      <li class="nav-item">
		        <a class="nav-link" href="/auth/joinForm">Join</a>
		      </li>
		    </ul>
  	</c:when>
  	<c:otherwise>
  		 <ul class="navbar-nav">
		      <li class="nav-item">
		        <a class="nav-link" href="/board/reportForm">트레이딩 보고서</a>
		      </li>
		      <li class="nav-item">
		        <a class="nav-link" href="/getList">계정정보</a>
		      </li>
		      <li class="nav-item">
		        <a class="nav-link" href="/user/updateForm">회원정보</a>
		      </li>
		      <li class="nav-item">
		        <a class="nav-link" href="/logout">로그아웃</a>
		      </li>
		    </ul>
  	</c:otherwise>
  </c:choose>
  </div>  
</nav>
<br>

<div class="container">
 <!DOCTYPE html>
<html lang="en">
<head>
  <title>Bootstrap Example</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">

</head>
<body>