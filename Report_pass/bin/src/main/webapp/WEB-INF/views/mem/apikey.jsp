<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="../layout/header.jsp"%>

<div class="container">
	<div class="card m-2">

			<h4 class="card-title" style="text-align: center;">계정 생성</h4>
						
						<select class="form-control valid" id="site" name="site"
                                 aria-required="true" aria-describedby="val-skill-error">
                                 <option value="">거래소 선택</option>
                                 <option value="upbit">업비트 (UPBIT)</option>
                                 <option value="bithumb">빗썸 (BitHumb)</option>
                                 <option value="coinone">코인원 (coinone)</option>
                              </select>
					
				<p>Connect Key</p>
				
				<input type="text" class="form-control" id ="apiKey" name = "apiKey" placeholder="Connect key"/>
				<p>Secret Key</p>
				<input type="text" class="form-control" id ="secretKey"	name = "secretKey" placeholder="Secret Key"/>
				<br>
				<button type="button" class="btn btn-primary"  id="btn-auth" onclick="auth()">API Key 인증</button>
				<br>
				<button type="button" class="btn btn-primary"  id="btn-insert" onclick="insert()">생성하기</button>
		</div>
</div>

<script src="/js/apiAuth.js"></script>

<%@ include file="../layout/footer.jsp"%>
</body>
</html>



