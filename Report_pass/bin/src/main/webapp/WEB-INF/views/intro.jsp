<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="layout/header.jsp"%>
<html>
<head>

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

</head>
<body>

	<div class="container">
		<button type="button" class="btn btn-primary" style="float: center;"
			id="btn-apiCreate">계정 새로만들기</button>

		<c:forEach var="row" items="${ list }">
			<form name="form1" method="post" action="/apikey" onsubmit="return confirm('수정하시겠습니까?')">
				<button type="button" class="btn" style="float: right;"
					id="btnDelete" onclick="deleteapi(${row.idx})">삭제</button>
				<button type="submit" class="btn" style="float: right;" >수정</button>

				<div class="card m-2">
					<div class="card-body">
						<h4 class="card-title">${row.idx}</h4>
						<h4 class="card-title">${row.memId}</h4>
						<h4 class="card-title">${row.site}</h4>
						<input type="hidden" name="idx" id="idx" value="${row.idx}">
						<input type="hidden" name="memId" id="memId" value="${row.memId}">
						<input type="hidden" name="site" id="site" value="${row.site}">
						<input type="hidden" name="apiKey" id="apiKey"
							value="${row.apiKey}"> <input type="hidden"
							name="secretKey" id="secretKey" value="${row.secretKey}">
						<c:choose>
							<c:when test="${row.site eq 'bithumb'}">
								<h4 class="card-title" id="detailavg">${bithumbbal}</h4>
							</c:when>
							<c:when test="${row.site eq 'upbit'}">
								<h4 class="card-title" id="detailavg">${upbitbal}</h4>
							</c:when>
							<c:when test="${row.site eq 'coinone'}">
								<h4 class="card-title" id="detailavg">${coinonebal}</h4>
							</c:when>
						</c:choose>
						
						<button type="button" class="btn btn-primary"
							id="btnDetail" onclick="detailapi(${row.idx})">상세보기</button>
					</div>

				</div>
				<br>
			</form>
		</c:forEach>
	</div>
	<script src="/js/user.js"></script>
	<script src="/js/edit.js"></script>
	<%@ include file="layout/footer.jsp"%>
</body>
</html>


