<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp"%>

<head>
    <style>
        .btn-primary {
            margin-top: 20px;
            width: 100%;
            height: 44px;
        }

        .container {
            max-width: 460px;
        }
    </style>
</head>

<div id="warp">
    <div class="container">

        <form>
        <input type="hidden" id="idx"  value="${principal.user.idx }"/>
            <div class="form-group">
                <label for="username">ID</label> 
                <input type="text" value="${principal.user.memId }" class="form-control" placeholder="Enter id" id="username" readonly="readonly">
            </div>
            <div class="form-group">
                <label for="pwd">Password</label> 
                <input type="password"  class="form-control" placeholder="Enter password" id="password">
            </div>
            <div class="form-group">
                <label for="phone">Phone</label> 
                <input type="text" value="${principal.user.phone }" class="form-control" placeholder="Enter phone" id="phone">
            </div>
            <div class="form-group">
                <label for="name">Name</label> 
                <input type="text" value="${principal.user.name }"  class="form-control" placeholder="Enter name" id="name">
            </div>
            <div class="form-group">
                <label for="email">Email</label> 
                <input type="email" value="${principal.user.email }"  class="form-control" placeholder="Enter email" id="email">
            </div>
    
        </form>
        <button id="btn-update" class="btn btn-primary">회원수정완료</button>
    
    </div>
</div>

</body>

</html>

<script src="/static/js/user.js"></script>
<%@ include file="../layout/footer.jsp"%>
