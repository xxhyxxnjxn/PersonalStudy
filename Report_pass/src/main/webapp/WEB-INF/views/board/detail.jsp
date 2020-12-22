<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp"%>
<link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-bs4.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-bs4.min.js"></script>

<div class="container">
	<br/><br/>
		<div>
			<label for="title">Title</label>
			<h3> ${board.title}	</h3>
		</div>
		<div>
			<label for="content">Content:</label>
			<div>${board.content }</div>
		</div>
		</hr>
	<br/><br/>
	<button class="btn btn-secondary"  onclick="history.back()">돌아가기</button>
	<c:if test="${board.user.memId == principal.user.memId}">	
		<a href="/board/${board.orderId}/updateForm" class="btn btn-warning" id="btn-update"  >수정</a>
		<button id="btn-delete" class="btn btn-danger" >삭제</button>
	</c:if>
	</br></br>
	<div>
		글 번호 : <span id="orderId">${board.orderId}</span>
		작성자 : <span>${board.user.memId}</span>
	</div>
	</br></br>
</div>

</body>
</html>
<script src="/js/report.js"></script>
<%@ include file="../layout/footer.jsp"%>
