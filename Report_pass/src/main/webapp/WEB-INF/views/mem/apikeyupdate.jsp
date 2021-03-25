<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="../layout/header.jsp"%>

<style>
    .container {
        max-width: 460px;
    }

    /* 로딩*/
    p {
        margin: 0px;
    }

    .notification {
        vertical-align: middle;
        text-align: center;
        height: 400px;
    }

    .m-2 {
        margin: 0px !important;
    }

    .card {
        border: none;
    }

    .btn-primary {
        margin-bottom: 20px;
        height: 40px;
        width: 100%;
        height: 40px;
    }

    .card-title {
        text-align: center;
        margin-bottom: 40px;
    }

    .form-control {
        margin-bottom: 30px;
    }
</style>

<div id="warp">
    <div class="container">
        <div class="card m-2">

            <h4 class="card-title" style="text-align: center;">계정 수정</h4>
            <input type="text" class="form-control" id="site" name="site" value="${apiDto.site}" readonly="readonly">
            <p>Connect Key</p>
            <input type="text" class="form-control" id="apiKey" name="apiKey" placeholder="Connect key"
                value="${apiDto.apiKey }">
            <p>Secret Key</p>
            <input type="text" class="form-control" id="secretKey" name="secretKey" placeholder="Secret Key">
            <button type="button" class="btn btn-primary" id="btn-auth" onclick="auth()">API Key 인증</button>
            <button type="button" class="btn btn-primary" id="btn-insert" onclick="update()">수정하기</button>
        </div>
    </div>
</div>

<script src="/static/js/apiAuth.js"></script>

<%@ include file="../layout/footer.jsp"%>
</body>

</html>
