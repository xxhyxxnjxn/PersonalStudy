<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-bs4.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-bs4.min.js"></script>
  <link href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css" rel="stylesheet">
  <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
  <script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
  <link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.js"></script>
<link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-bs4.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-bs4.min.js"></script>

<div class="container">
   <br/><br/>
      <div>
         <label for="title">Title</label>
         <h3> ${board.title}   </h3>
      </div>
      <div>
         <label for="content">Content:</label>
         <div>${board.content }</div>
      </div>
      </hr>
   <br/><br/>
   <%-- <button class="btn btn-secondary"  id="btn-back">돌아가기</button><input type="hidden" value="${site}" id="site"/> --%>
   <c:if test="${board.user.memId == principal.user.memId}">   
      <a href="#"  onclick='window.open("/board/${board.orderId}/updateForm/${site}","window팝업","width=950,height=1000,menubar=no,status=no,toolbar=no")''  class="btn btn-warning" id="btn-update"  >수정</a>
      <button id="btn-delete" class="btn btn-danger" >삭제</button>
   </c:if>
   </br></br>
   <div>
    <input type="hidden"  id="orderId" value="${board.orderId}">
      작성자 : <span>${board.user.memId}</span>
   </div>
   </br></br>
</div>

</body>
</html>
<script src="/static/js/report.js"></script>