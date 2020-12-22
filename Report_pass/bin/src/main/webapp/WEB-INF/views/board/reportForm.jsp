<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="../layout/header.jsp"%>
<script>
</script>
<div class="container">
  <h2>체결내역 보고서</h2>
  <p>원하는 코인, 거래소및 기간을 선택하세요.</p>            
  <table class="table table-hover" style="width:1150;">
    <thead>
      <tr>
        <th style="text-align:center;">거래소</th>
        <th style="text-align:center;">일자</th>
        <th style="text-align:center;">구분</th>
        <th style="text-align:center;">코인가격</th>
        <th style="text-align:center;">체결수량</th>
        <th style="text-align:center;">평균매수가</th>
        <th style="text-align:center;">수수료</th>
        <th style="text-align:center;">거래금액</th>
        <th style="text-align:center;">일지</th>
        <th style="text-align:center;">첨부파일</th>
        <th style="text-align:center;">비고</th>
      </tr>
    </thead>
    <tbody>
    <c:forEach var="trades" items="${trades}">
      <tr>
        <td>${trades.site}</td>
        <td>${trades.transactionDate}</td>
		<c:set value = "${trades.type}" var="type"></c:set>
		<c:if test="${type=='bid' }">
        <td>매수</td>
		</c:if>
		<c:if test="${type=='ask' }">
        <td>매도</td>
		</c:if>
        <td style="text-align:right;">${trades.price}</td>
        <td style="text-align:right;">${trades.units}</td>
        <td style="text-align:right;">${trades.avgPrice}</td>
        <td style="text-align:right;">${trades.fee}</td>
        <td style="text-align:right;">${trades.totalPrice}</td>
        <td><input type="text" value="${trades.log}" placeholder="${trades.log}" style="width:80%"><input type="button" id="btn-save-log"  class="btn "  value= "저장" style="width:20%"/></td>
        <td>${trades.file}</td>
		<c:set value = "${boardsInit}" var="boardsInit"></c:set>
        <c:choose>       
			 <c:when test="${boardsInit=='boardsInit'}">
			  		<td><a href="/board/saveForm/${trades.idx}&${trades.price}">상세작성</a></td>
			 </c:when>
			  <c:otherwise>
		        <c:forEach var="boards" items="${boards}">
					<c:set value = "${boards.idx}" var="boardIdx"></c:set>
					<c:set value = "${trades.idx}" var="tradeIdx"></c:set>
					   <c:choose>       
					      <c:when test="${empty boardIdx}">
					        	<td><a href="/board/saveForm/${trades.idx}&${trades.price}">상세보기</a></td>
					      </c:when>
					      <c:otherwise>
							<c:if test="${boardIdx==tradeIdx}">
					      		<td><a href="/board/${trades.idx}">상세보기</a></td>
							</c:if>
					      </c:otherwise>
					   </c:choose>
				   </c:forEach>
			 </c:otherwise>
		</c:choose>
		   <td></td>
      </tr>
	</c:forEach>
    </tbody>
  </table>
</div>
<%@ include file="../layout/footer.jsp"%>
</body>
</html>


