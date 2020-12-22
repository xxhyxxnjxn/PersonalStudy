<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp"%>
<link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-bs4.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-bs4.min.js"></script>

<div class="container">
	<form>
		<input type="hidden" id="idx" value="${board.idx }"/>
		<div class="form-group">
			<input value="${board.title }" type="text" class="form-control" placeholder="Enter title" id="title">
		</div>
		<div class="form-group">
			<textarea class="form-control summernote" rows="5" id="content">${board.content }</textarea>
		</div>
		<input type="hidden" id="idx" value="${idx}"/>
	</form>
		<button id="btn-board-save" class="btn btn-primary" style="text-algin:center;">글수정 완료</button>
</div>

 <script>
      $('.summernote').summernote({
        tabsize: 2,
        height: 500
      });
    </script>
</body>
</html>
<script src="/js/board.js"></script>
<%@ include file="../layout/footer.jsp"%>