<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
	rel="stylesheet" />
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/datatables/1.10.12/css/dataTables.bootstrap.min.css"
	rel="stylesheet" />


<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<style>
th {
	text-align: center;
	font-size: 13px;
	padding: .60rem;
}

.header1 {
	position: sticky;
	top: 0;
	background-color: white;
}
</style>

<script type="text/javascript">

function inNumber(){
    if(event.keyCode<48 || event.keyCode>57){
       if(event.keyCode !=46){
		       event.returnValue=false;
      		 alert("숫자만 입력가능합니다");
           } 

    }
}
	function updateprice(orderId, site) {
		var confirmPrice = $('#' + orderId).val();
		//answer = confirm("가격을 저장하시겠습니까??");
		//if (answer == true) {
			$.ajax({
				type : "PUT",//insert
				url : "/board/reportForm/" + site + "/save",
				data : {
					orderId : orderId,
					price : $('#' + orderId).val()
				},
				dataType : "json"
			}).done(function(r) {
				//alert("수정이 완료되었습니다.");
				opener.parent.location.reload();
				//   location.href = "/board/reportForm/" + $('#site').val();
			}).fail(function(r) {
				//var message = JSON.parse(r.responseText);
				//console.log((message));
				alert('서버 오류');
			});

		//}
	}



function trade(){
	//alert($('#orderby').val()+$('#type').val());
	var num;
	$.ajax({
		type : "post",//insert
		url : "/board/reportForm/" + site + "/bankstatement",
		data : {
			orderby: $('#orderby').val(),
			type: $('#type').val(),
			priceSelect: $('#priceSelect').val(),
			site : site
		},
		dataType : "json",
		success: function(json) {
			num = json.length+1;
				$.each(json, function(index, json) {
					var lit = parseFloat(json.transactionDate);
					if ($('#site').val() == "bithumb") {
						var trade_dateYear = new Date(lit / 1000).getFullYear();
						var trade_dateMonth = new Date(lit / 1000).getMonth() + 1;
						trade_dateMonth = "0" + trade_dateMonth;
						trade_dateMonth = trade_dateMonth.slice(-2)
						var trade_dateDate = new Date(lit / 1000).getDate();
						trade_dateDate = "0" + trade_dateDate;
						trade_dateDate = trade_dateDate.slice(-2)
						var trade_dateHour = new Date(lit / 1000).getHours();
						trade_dateHour = "0" + trade_dateHour;
						trade_dateHour = trade_dateHour.slice(-2)
						var trade_dateMinute = new Date(lit / 1000).getMinutes();
						trade_dateMinute = "0" + trade_dateMinute;
						trade_dateMinute = trade_dateMinute.slice(-2)
						var trade_dateSecond = new Date(lit / 1000).getSeconds();
						trade_dateSecond = "0" + trade_dateSecond;
						trade_dateSecond = trade_dateSecond.slice(-2)
						var full = trade_dateYear + ". " + trade_dateMonth + ". " + trade_dateDate + ". " + trade_dateHour + ":" + trade_dateMinute + ":" + trade_dateSecond;
					
					} else if ($('#site').val() == "coinone") {
						var trade_dateYear = new Date(lit).getFullYear();
						var trade_dateMonth = new Date(lit).getMonth() + 1;
						trade_dateMonth = "0" + trade_dateMonth;
						trade_dateMonth = trade_dateMonth.slice(-2)
						var trade_dateDate = new Date(lit).getDate();
						trade_dateDate = "0" + trade_dateDate;
						trade_dateDate = trade_dateDate.slice(-2)
						var trade_dateHour = new Date(lit).getHours();
						trade_dateHour = "0" + trade_dateHour;
						trade_dateHour = trade_dateHour.slice(-2)
						var trade_dateMinute = new Date(lit).getMinutes();
						trade_dateMinute = "0" + trade_dateMinute;
						trade_dateMinute = trade_dateMinute.slice(-2)
						var trade_dateSecond = new Date(lit).getSeconds();
						trade_dateSecond = "0" + trade_dateSecond;
						trade_dateSecond = trade_dateSecond.slice(-2)
						var full = trade_dateYear + ". " + trade_dateMonth + ". " + trade_dateDate + ". " + trade_dateHour + ":" + trade_dateMinute + ":" + trade_dateSecond;
						

					} else {
						var trade_dateYear = new Date(lit).getFullYear();
						var trade_dateMonth = new Date(lit).getMonth() + 1;
						trade_dateMonth = "0" + trade_dateMonth;
						trade_dateMonth = trade_dateMonth.slice(-2)
						var trade_dateDate = new Date(lit).getDate();
						trade_dateDate = "0" + trade_dateDate;
						trade_dateDate = trade_dateDate.slice(-2)
						var trade_dateHour = new Date(lit).getHours();
						trade_dateHour = "0" + trade_dateHour;
						trade_dateHour = trade_dateHour.slice(-2)
						var trade_dateMinute = new Date(lit).getMinutes();
						trade_dateMinute = "0" + trade_dateMinute;
						trade_dateMinute = trade_dateMinute.slice(-2)
						var trade_dateSecond = new Date(lit).getSeconds();
						trade_dateSecond = "0" + trade_dateSecond;
						trade_dateSecond = trade_dateSecond.slice(-2)
						var full = trade_dateYear + ". " + trade_dateMonth + ". " + trade_dateDate + ". " + trade_dateHour + ":" + trade_dateMinute + ":" + trade_dateSecond;
						
					}
					num--;
					  $('#myTable').append('<tr><th>'+num+'</th>'+
							   '<th>'+json.site+'</th>'+
				               '<th>'+json.currency+'</th>'+
				               '<th>'+full+'</th>'+
				               '<th>'+json.type+'</th>'+
				               '<th>'+json.units+'</th>'+
				               '<th><input  onkeypress="inNumber()" type="text" id="'+json.orderId+'" value="'+json.price+ '">'+
				               '<input type="button" onclick="updateprice(\''+json.orderId+'\',\''+json.site+'\')" class="btn " value="수정"></th></tr>');

					});
				
			},
		error: function(json) {
				alert('실패');
			}



		}); 
}

$(function() {

 site = $('#site').val()
 
 trade();

});

function candleStick() {
	var dateArray = [];
	var i = $('#bankStateMentTable >tbody tr').length;
	
	alert($('#bankStateMentTable >tbody tr').length)
	
	$.ajax({
		type : "post",//insert
		url : "/board/reportForm/bithumb/bankstatement/candleStick",
		data : {
			date : full
		},
		dataType : "json",
		success:function(json){

		},
		error: function(json) {
			alert('실패');
		}
		
	});

}


	
</script>


<div style="width: 1650px; border-collapse: collapse; margin: 0 auto;">

	<h2>입출금 내역서</h2>
	
	
<%-- 	<c:if test="${site=='bithumb'}">
   
      <button type="button" class="btn btn-primary" onclick="candleStick()" style="float:right">코인 가격 조회</button>
   </c:if> --%>
<%--    <c:if test="${site=='coinone'}">
      <p style="float: right;">입.출금 거래내역을 다운받아 csv파일 그대로 넣으세요.</p>
      <br>
      <br>
      <form action="/excel/readCoinone/bankstatement" method="POST" enctype="multipart/form-data"
         id="fileUploadFormCoinoneBank">
         <input class="btn btn-primary" id="excelSubmitCoinoneBank" type="submit"
            value="제출" style="float: right;" /> <input type="file" name="file"
            id="bithumbExcelFile" style="float: right;">
      </form>
      

   </c:if> --%>
   
   <c:if test="${site=='upbit'}">
      <p style="float: right;">입.출금 거래내역을 다운받아 파일 그대로 넣으세요.</p>
      <br>
      <br>
      <form action="/excel/readUpbit/bankstatement" method="POST" enctype="multipart/form-data"
         id="fileUploadFormUpbitBank">
         <input class="btn btn-primary" id="excelSubmitUpbitBank" type="submit"
            value="제출" style="float: right;" /> <input type="file" name="file"
            id="bithumbExcelFile" style="float: right;">
      </form>
  
   </c:if>
       <br>
      <br>
      <select id="type" style="float: right; height: 25px;">
      <option value="ALL" selected="selected">모두</option>
      <option value="입금">입금</option>
      <option value="출금">출금</option>
   </select><select id="orderby" style="float: right; height: 25px;">
      <option value="DESC" selected="selected">최근순</option>
      <option value="ASC">과거순</option>
   	</select><select id="priceSelect" style="float: right; height: 25px;">
      <option value="ALL" selected="selected">모두</option>
      <option value="ZERO">0인것만 보기</option>
   	</select>
	

<input type="hidden" id="site" value="${site}">
	<table class="table table-striped table-bordered table-hover"
		id ="bankStateMentTable" cellspacing="0" width="100%"
		style=" border-collapse: collapse; margin: 0 auto;">
		<thead class="header1">

			<tr>
				<th>번호</th>
				<th>거래소</th>
				<th>코인명</th>
				<th>일자</th>
				<th>구분</th>
				<th>체결수량</th>
				<th>코인가격</th>
			</tr>
		</thead>
		<tbody id="myTable">
		 
			<%-- <c:forEach var="statementList" items="${statementList}">
				<tr>
					<th>${statementList.site}</th>
					<th>${statementList.currency}</th>
					<th>${statementList.transactionDate}</th>
					<th>${statementList.type}</th>
					<th>${statementList.units}</th>
					<th><input type="text" id="${statementList.orderId}" value="${statementList.price} "> 
						<input type="button"
						onclick="updateprice('${statementList.orderId}','${statementList.site}')"
						class="btn " value="수정"></th>
				</tr>

			</c:forEach> --%>

		</tbody>
	</table>

	<ul class="pagination" id="page">
		<!-- <li class="page-item disabled"><a class="page-link" href="#">Previous</a></li>
		<li class="page-item"><a class="page-link" href="#">1</a></li>
		<li class="page-item"><a class="page-link" href="#">2</a></li>
		<li class="page-item"><a class="page-link" href="#">3</a></li>
		<li class="page-item"><a class="page-link" href="#">Next</a></li> -->
	</ul>


	</body>
	</html>

	<script>
		
	</script>
	<script src="/static/js/selectBox.js"></script>
	<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
	<script
		src="https://cdn.datatables.net/1.10.22/js/jquery.dataTables.min.js"></script>
	<script
		src="https://cdn.datatables.net/1.10.22/js/dataTables.bootstrap4.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/datatables/1.10.12/js/jquery.dataTables.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/datatables/1.10.12/js/dataTables.bootstrap.min.js"></script>
	<script
		src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>