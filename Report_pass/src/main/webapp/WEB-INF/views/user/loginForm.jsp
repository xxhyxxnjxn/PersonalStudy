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
            padding: 0px;
        }

    </style>
</head>

<div id="warp">
    <div class="container">

        <form action="/auth/loginProc" method="post">
            <div class="form-group">
                <label for="memId">ID</label> <input type="text" name="username" class="form-control"
                    placeholder="Enter Id" id="memId">
            </div>
            <div class="form-group">
                <label for="pwd">Password</label> <input type="password" name="password" class="form-control"
                    placeholder="Enter password" id="password">
            </div>

            <!-- <div class="form-group form-check">
                <label class="form-check-label"> <input class="form-check-input" type="checkbox"> Remember me
                </label>
            </div> -->

            <button id="btn-login" class="btn btn-primary">로그인</button>
        </form>

    </div>
</div>

</body>

</html>

<%@ include file="../layout/footer.jsp"%>