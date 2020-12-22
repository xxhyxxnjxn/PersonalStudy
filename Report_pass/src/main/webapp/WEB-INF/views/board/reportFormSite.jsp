<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="../layout/header.jsp"%>
<style>
th {
	text-align: center;
	font-size: 13px;
	padding: .60rem;
}
</style>
<div style="width: 1900px; border-collapse: collapse; margin: 0 auto;">
	<input type="hidden" id="site" value="${site}"> <input type="hidden" id="orderId" value="${orderId}"> <br>
	<h2>체결내역 보고서</h2>
	<p>원하는 코인, 거래소및 기간을 선택하세요.</p>
	<br> <br> <input class="form-control" id="myInput" type="text" placeholder="검색" style="float: right; height: 25px;; width: 100px;"> <select id="orderby"
		style="float: right; height: 25px;">
		<option value="DESC">최근순</option>
		<option value="ASC" selected="selected">과거순</option>
	</select>
	<p style="float: right;">
		Date: <input style="height: 25px;" type="date" id="now_dateFrom">~<input style="height: 25px;" type="date" id="now_dateTo">
	</p>
	<select style="float: right; height: 25px;" id="coinSelected">
		<option>코인선택</option>
		<c:forEach var="selectsCoins" items="${selectsCoins}">
			<option>${selectsCoins.currency}</option>
		</c:forEach>
	</select>

	<!--     <select style="float:right; height:25px;" id = "selectsite">
           	<option value="bithumb"> 빗썸 ( bithumb ) </option>
      	<option value="upbit"> 업비트 ( upbit) </option>
    	<option value="coinone"> 코인원 (coinone) </option>

-->

	<select style="float: right; height: 25px;" id="selectsite">
		<c:forEach var="selectSites" items="${selectsSites}">

			<c:if test="${selectSites.site!=site &&selectSites.site=='bithumb'}">
				<option value="${selectSites.site}">빗썸 ( ${selectSites.site} )</option>
			</c:if>
			<c:if test="${selectSites.site!=site && selectSites.site=='upbit'}">
				<option value="${selectSites.site}">업비트 ( ${selectSites.site} )</option>
			</c:if>
			<c:if test="${selectSites.site!=site &&selectSites.site=='coinone'}">
				<option value="${selectSites.site}">코인원 ( ${selectSites.site} )</option>
			</c:if>
			<c:if test="${selectSites.site==site && selectSites.site=='bithumb'}">
				<option value="${selectSites.site}" selected="selected">빗썸( ${selectSites.site} )</option>
			</c:if>
			<c:if test="${selectSites.site==site && selectSites.site=='upbit'}">
				<option value="${selectSites.site}" selected="selected">업비트( ${selectSites.site} )</option>
			</c:if>
			<c:if test="${selectSites.site==site && selectSites.site=='coinone'}">
				<option value="${selectSites.site}" selected="selected">코인원( ${selectSites.site} )</option>
			</c:if>

		</c:forEach>
	</select>

	<table class="table table-hover" style="width: 1900px; border-collapse: collapse; margin: 0 auto;">
		<thead>
			<tr>
				<td>거래소</td>
				<td>코인명</td>
				<td>일자</td>
				<td>구분</td>
				<td>코인가격</td>
				<td>체결수량</td>
				<td>평균매수가</td>
				<td>수수료</td>
				<td>거래금액</td>
				<td>매도수익률</td>
				<td>청산수익률</td>
				<td colspan="2">일지</td>
				<td>상세작성</td>
			</tr>
		</thead>
		<tbody id="myTable">

		</tbody>
	</table>
	</body>
	</html>

	<script>
		
	</script>
	<script src="/js/fresh.js"></script>
	<script src="/js/reportForm.js"></script>
	<script src="/js/selectBox.js"></script>
	<%@ include file="../layout/footer.jsp"%>

