<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ include file="../layout/header.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org">
<body>
<div class="container">

   <form>
      <div class="form-group">
         <label for="username">Id ( 4~12자의 영문 대/소문자와 숫자로만 입력)</label> 
         <input type="text" class="form-control" placeholder="Enter id" id="username"><input type="button" value="중복확인"  id="btn-confirm-id" class="btn btn-primary">
      </div>
      <div class="form-group">
         <label for="pwd">Password (  8~15자의 영문 대문자와 소문자, 숫자가 함께 입력되어야 합니다. )</label> 
         <input type="password" class="form-control" placeholder="Enter password" id="password">
      </div>
      <div class="form-group">
         <label for="pwd">Password 재확인 </label> 
         <input type="password" class="form-control" placeholder="Enter password confirm" id="passwordRe">
      </div>
      <div class="form-group">
         <label for="name">User Name</label> 
         <input type="text" class="form-control" placeholder="Enter name" id="name">
      </div>
      <div class="form-group">
         <label for="email">Email </label> 
         <input type="email" class="form-control" placeholder="Enter email" id="email">
      </div>
      <div class="form-group">
         <label for="phone">Phone</label> 
         <input type="text" class="form-control" placeholder="Enter phone" id="phone">
      </div>

   </form>
   <button id="btn-save" class="btn btn-primary">가입하기</button>

</div>

</body>
<script src="/static/js/user.js"></script>
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
  <link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
  <link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-bs4.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-bs4.min.js"></script>
  <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
  <script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
  <script src="//ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<script src="//code.jquery.com/ui/1.8.18/jquery-ui.min.js"></script>
</html>

<%@ include file="../layout/footer.jsp"%>