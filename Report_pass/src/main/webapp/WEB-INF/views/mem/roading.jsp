<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="../layout/header.jsp"%>
<script>

 /* $(document).ready(function(){

			$.ajax({
				url: '/roading',
				type: 'post',
				dataType: 'text',
				data: {
					site: $('#site').val(),
					apiKey: $('#apiKey').val(),
					secretKey: $('#secretKey').val()
				},
				success: function(result) {

				 	//alert("계정이 생성되었습니다.");
					location.replace("/getList"); 
				}
			});
	
})  */
</script>
<div class="container">
	<div class="card m-2">

			<h4 class="card-title" style="text-align: center;">로딩중</h4>
	</div>
</div>

<script src="/js/apiAuth.js"></script>

<%@ include file="../layout/footer.jsp"%>
</body>
</html>



