<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="../layout/header.jsp"%>

<style>
    .container {
        max-width: 460px;
        }
    /* 로딩*/
    p {
        margin-bottom: 8px;
    }
    #loading {
        height: 100%;
        left: 0px;
        position: fixed;
        _position: absolute;
        top: 0px;
        width: 100%;
        filter: alpha(opacity=50);
        -moz-opacity: 0.5;
        opacity: 0.5;
    }
    .loading {
        background-color: white;
        z-index: 199;
    }
    .notification {
        vertical-align: middle;
        text-align: center;
        height: 400px;
    }

    #loading_img {
        position: absolute;
        top: 50%;
        left: 50%;
        height: 35px;
        margin-top: -75px; //	이미지크기
        margin-left: -75px; //	이미지크기
        z-index: 200;
    }

    .m-2 {
        margin: 0px!important;
    }
    .card {
        border: none;
    }
    .btn-primary {
        margin-bottom: 20px;
        height: 44px;
    }

    .card-title {
        text-align: center;
        margin-bottom: 40px;
    }

    .form-control {
        margin-bottom: 16px;
    }
    
</style>

<div id="loading" class="loading">
    <div class="notification">최초 거래내역 저장 중 입니다. <br> 잠시만 기다려주세요.</div>
    <img id="loading_img" alt="loading" src="/static/images/viewLoading.gif" />
</div>

<div id="warp">
    <div class="container">
        <div class="card m-2">
    
            <h4 class="card-title">계정 생성</h4>
    
            <select class="form-control valid" id="site" name="site" aria-required="true" aria-describedby="val-skill-error">
                <option value="">거래소 선택</option>
                <option value="upbit">업비트 (UPBIT)</option>
                <option value="bithumb">빗썸 (BitHumb)</option>
                <option value="coinone">코인원 (coinone)</option>
            </select>
    
            <div>
                <div id="upbit_comment"></div>
            </div>
            <p>Connect Key</p>
    
            <input type="text" class="form-control" id="apiKey" name="apiKey" placeholder="Connect key" />
            <p>Secret Key</p>
            <input type="text" class="form-control" id="secretKey" name="secretKey" placeholder="Secret Key" />

            <button type="button" class="btn btn-primary" id="btn-auth" onclick="auth()" style="margin-top: 20px;">API Key 인증</button>
            <button type="button" class="btn btn-primary" id="btn-insert" onclick="insert()">생성하기</button>
        </div>
    </div>
</div>

<script src="/static/js/apiAuth.js"></script>

<%@ include file="../layout/footer.jsp"%>
</body>

</html>
