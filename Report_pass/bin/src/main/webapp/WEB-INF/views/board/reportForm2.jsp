<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="../layout/header.jsp"%>
<style>
	th {
		text-align:center;
		font-size:13px;
		padding:	.60rem;
	}
	td{
	font-size:13.5px;
	}
</style>
<script>
</script>
<div class="container">
  <h2>체결내역 보고서</h2>
  <p>원하는 코인, 거래소및 기간을 선택하세요.</p>            
     <input class="form-control" id="myInput" type="text" placeholder="검색" style="float:right; height:25px;;width:100px;">
    <select style="float:right; height:25px;">
    	<option>최근순</option>
    	<option>과거순</option>
    </select>
    <p style="float:right;">Date: <input style=" height:25px;" type="text" id="datepickerFrom">~<input style=" height:25px;" type="text" id="datepickerTo"></p>
    <select style="float:right; height:25px;" id="selectsCoins">
    	<option>코인선택</option>
        <c:forEach var="selectsCoins" items="${selectsCoins}">
    	<option> ${selectsCoins.currency} </option>
		</c:forEach>    
    </select>
    
    <select style="float:right; height:25px;" id="selectSites">
       	<option value="bithumb"> 빗썸 ( bithumb ) </option>
      	<option value="upbit"> 업비트 ( upbit) </option>
    	<option value="coinone"> 코인원 (coinone) </option>
    </select>

  </div>
  <table class="table table-hover" style="width:1150;">
    <thead>
      <tr>
        <th>거래소</th>
        <th>코인명</th>
        <th>일자</th>
        <th>구분</th>
        <th>코인가격</th>
        <th>체결수량</th>
        <th>평균매수가</th>
        <th>수수료</th>
        <th>거래금액</th>
		<th>누적 수익 금액</th>
        <th>개별 수익 금액</th>
        <th>수익률</th>
        <th>일지</th>
        <th>상세작성</th>
      </tr>
    </thead>
    <tbody id="myTable">
    <c:forEach var="trades" items="${trades}">
    		        <%-- <c:forEach var="boards" items="${boards}">
					<c:set value = "${boards.orderId}" var="boardIdx"></c:set> --%>
					<c:set value = "${trades.orderId}" var="tradeIdx"></c:set>
      <tr>
        <td>${trades.site}</td>
        <td>${trades.currency}</td>
        <td>${trades.transactionDate}</td>
<%-- 		<c:set value = "${trades.type}" var="type"></c:set>
		<c:if test="${type=='bid' }">
        <td>매수</td>
		</c:if>
		<c:if test="${type=='ask' }">
        <td>매도</td>
		</c:if> --%>
		<td>${trades.type}</td>
        <td style="text-align:right;">${trades.price}</td>
        <td style="text-align:right;">${trades.units}</td>
        <td style="text-align:right;">${trades.avgPrice}</td>
        <td style="text-align:right;">${trades.fee}</td>
        <td style="text-align:right;">${trades.totalPrice}</td>
        <td style="text-align:right;">${trades.incomeCal}</td> <!-- 누적 수익 금액 -->
        <td style="text-align:right;">${trades.income}</td> <!-- 개별 수익 금액 -->
        <td style="text-align:right;">${trades.revenue}</td>
        <td style="text-align:right;">${trades.clearRevenue}</td>
        <td><input type="text" id ="logId${trades.orderId}" value="${trades.log}" placeholder="${trades.log}" style="width:70%"><input type="button" onclick="updateLog('${trades.orderId}")"id="btn-save-log"  class="btn "  value= "저장" style="width:20%;font-size:13px;"/></td>
		<c:set value = "${boardsInit}" var="boardsInit"></c:set>

		        <c:choose>       
					 <c:when test="${boardsInit=='boardsInit'}">
					 	
					  		<td><a href="/board/saveForm/${trades.orderId}&${trades.price}">상세작성!</a></td>
					  		
					 </c:when>
					 
					 
					 
					 
				<%-- 	  <c:otherwise>
					  
					  
					  
		
							   <c:choose>       
							      <c:when test="${empty boardIdx}">
							        	<td><a href="/board/saveForm/${trades.orderId}&${trades.price}">상세작성</a></td>
							        	
							        	
							        	
							      </c:when>
							      <c:otherwise>
							      
								   <c:choose>       
								      <c:when  test="${boardIdx==tradeIdx}">
							      
							      		<td><a href="/board/${trades.orderId}">상세보기</a></td>
							      		
									</c:when>
									</c:choose>
									
							      </c:otherwise>
							      
							      
							      
							   </c:choose>
						
						
						
						
						
					 </c:otherwise> --%>
				</c:choose>
			
		
	  		
	  		
	  		
	  		
	  		
	  		
		   <input type="hidden" id="orderId" name="orderId" value="${trades.orderId}"/>
      </tr>   
      </c:forEach>
<%-- 	</c:forEach> --%>
    </tbody>
  </table>
</div>
</body>
</html>
<script >
</script>
<script src="/js/reportForm2.js" ></script>
<script src="/js/selectBox2.js" ></script>
<%@ include file="../layout/footer.jsp"%>


