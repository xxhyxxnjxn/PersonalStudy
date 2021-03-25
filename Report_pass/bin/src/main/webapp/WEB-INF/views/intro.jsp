<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="layout/header.jsp"%>
<html>
<head>
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
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script>

</script>
</head>
<body>

	<div class="container">
		<button type="button" class="btn btn-primary" style="float: center;"
			id="btn-apiCreate">계정 새로만들기</button>




		<c:forEach var="row" items="${ list }">
			<form name="form1" method="post" action="/apikey"
				onsubmit="return confirm('수정하시겠습니까?')">


				<div class="card m-2">
					<div class="card-body" >

						<table class="table" style="text-align: center; ">
							<thead>
								<tr>
									<th style="width: 500px;">거래소</th>
									<th style="text-align: center;">거래가능 원화</th>
								</tr>
							</thead>

							<tbody>
								<tr>
									<td>${row.site}</td>
									<td style="text-align: center;"><c:choose>
											<c:when test="${row.site eq 'bithumb'}">
								${bithumbbal}
							</c:when>
											<c:when test="${row.site eq 'upbit'}">
								${upbitbal}
							</c:when>
											<c:when test="${row.site eq 'coinone'}">
								${coinonebal}
							</c:when>
										</c:choose></td>
								</tr>
							</tbody>
						</table>


						<input type="hidden" name="idx" id="idx" value="${row.idx}">
						<input type="hidden" name="memId" id="memId" value="${row.memId}">
						<input type="hidden" name="site" id="site" value="${row.site}">
						<input type="hidden" name="apiKey" id="apiKey"
							value="${row.apiKey}"> <input type="hidden"
							name="secretKey" id="secretKey" value="${row.secretKey}">

						<button type="button" class="btn btn-primary" id="btnDetail"
							onclick="detailapi('${row.site}')" style="margin-left: ;">상세보기</button>
						<button type="button" class="btn" style="float: right;"
							id="btnDelete" onclick="deleteapi(${row.idx})">삭제</button>
						<button type="submit" class="btn" style="float: right;">수정</button>
					</div>

				</div>
				<br>
			</form>
		</c:forEach>
	</div>
	<script src="/static/js/user.js"></script>
	<script src="/static/js/edit.js"></script>
	
	<%@ include file="layout/footer.jsp"%>
</body>
</html>


