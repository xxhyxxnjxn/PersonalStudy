<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="../layout/header.jsp"%>
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
 /* 로딩*/
/* #loading {
   height: 100%;
   left: 0px;
   position: fixed;
   _position: absolute;
   top: 0px;
   width: 100%;
   filter: alpha(opacity = 50);
   -moz-opacity: 0.5;
   opacity: 0.5;
}

.loading {
   background-color: white;
   z-index: 199;
}

#loading_img {
   position: absolute;
   top: 50%;
   left: 50%;
   height: 35px;
   margin-top: -75px; //
   이미지크기 margin-left: -75px; //
   이미지크기 z-index: 200;
} */
.tu{
 	 height: 0px;
	
	 margin-left: 1200px;
	 text-align: center;
}
</style>

<!-- <div id="loading" class="loading">
   <div style='vertical-align: middle; text-align: center; hight: 400px;'></div>
   <img id="loading_img" alt="loading"
      src="/static/images/viewLoading.gif" />
</div> -->


<div style="width: 1650px; border-collapse: collapse; margin: 0 auto;">
   <h2>체결내역 보고서<button type="button" class="btn btn-primary" onclick="refresh('${site}')" style="float:right">초기화</button></h2>
   <p >원하는 코인, 거래소및 기간을 선택하세요.
   
   
   <c:if test="${site=='coinone'}">
   <br>
	<div class = "tu">
	
     코인 : <input type='text' id="coinone_currency"/>
     
     </div>
   </c:if>

   <button type="button" class="btn btn-primary" onclick="updateLately('${site}')" style="float:right">업데이트
   </button>
<br>
<br>
<br>

   <c:set var="site" value="${site}"></c:set>
   <button type="button" class="btn btn-primary"
      onclick="insertPrice('${site}')" style="margin-left:;">입출금 가격 입력</button>
   <c:if test="${site=='bithumb'}">
   
      <p style="float: right;">누락된 이전 데이터가 있으면 해당 거래소에서 다운받아 파일그대로 넣으세요.</p>
      <br>
      <br>
      <form action="/excel/read" method="POST" enctype="multipart/form-data"
         id="fileUploadForm">
         <input class="btn btn-primary" id="excelSubmit" type="submit"
            value="제출" style="float: right;" /> <input type="file" name="file"  id="bithumbExcelFile" style="float: right;">
      </form>
      <br>
      <a href="#"
         onclick='window.open("https://www.bithumb.com/member_operation/login?reurl=%2Ftrade_history%2Fhistory","window팝업","width=1200,height=800,menubar=no,status=no,toolbar=no")'
         style="float: right;">빗썸 내역 다운받기</a>
      <br>
      <a href="#"
         onclick='window.open("/images/detail.jpg","window팝업","width=590,height=840,menubar=no,status=no,toolbar=no")'
         style="float: right;">빗썸 내역 다운받는 법 </a>
      <br>
   </c:if>
    <c:if test="${site=='coinone'}">
      <p style="float: right;">누락된 이전 데이터가 있으면 해당 거래소에서 다운받아 csv파일그대로 넣으세요.</p>
      <br>
      <br>
      <form action="/excel/readCoinone" method="POST" enctype="multipart/form-data"
         id="fileUploadFormCoinone">
         <input class="btn btn-primary" id="excelSubmitCoinone" type="submit"
            value="제출" style="float: right;" /> <input type="file" name="file"
            id="bithumbExcelFile" style="float: right;">
      </form>
      <br>
      <br>
      <a href="#"
         onclick='window.open("https://coinone.co.kr/balance/history/trade","window팝업","width=1200,height=800,menubar=no,status=no,toolbar=no")'
         style="float: right;">코인원 내역 다운받기 (거래내역 )</a>
      <br>
      <a href="#"
         onclick='window.open("/images/detail.jpg","window팝업","width=590,height=840,menubar=no,status=no,toolbar=no")'
         style="float: right;">코인원 내역 다운받는 법 </a>
      <br>

   </c:if>





   <c:choose>
      <c:when test="${empty  statementList}">
         <button type="button" class="btn btn-primary"
            onclick="rerevenue('${site}')" style="margin-left:;">수익률보기</button>
         <button type="button" class="btn btn-primary"
            onclick="allrevenue('${site}')" style="margin-left:;">전체
            재계산</button>

      </c:when>
      <c:otherwise>

      </c:otherwise>
   </c:choose>

   <br> <input type="hidden" id="site" value="${site}"> <input
      type="hidden" id="orderId" value="${orderId}"> <label>기간:</label>
   <input type="date" id="start"> <label>~</label> <input
      type="date" id="end"> <input type="button"
      class="btn btn-primary" id="date" onclick="date()" value="기간 검색">

   <select id="type" style="float: right; height: 25px;">
      <option value="ALL" selected="selected">모두</option>
      <option value="매수">매수</option>
      <option value="매도">매도</option>
      <option value="입금">입금</option>
      <option value="출금">출금</option>
   </select> <select id="orderby" style="float: right; height: 25px;">
      <option value="DESC" selected="selected">최근순</option>
      <option value="ASC">과거순</option>
   </select> <select style="float: right; height: 25px;" id="currency">
      <option value="ALL" selected="selected">모든코인</option>
      <c:forEach var="selectsCoins" items="${selectsCoins}">
         <option value="${selectsCoins.currency}">${selectsCoins.currency}
         </option>

      </c:forEach>
   </select> <select style="float: right; height: 25px;" id="selectsite">
      <c:forEach var="selectSites" items="${selectsSites}">

         <c:if test="${selectSites.site!=site &&selectSites.site=='bithumb'}">
            <option value="${selectSites.site}">빗썸 (
               ${selectSites.site} )</option>
         </c:if>
         <c:if test="${selectSites.site!=site && selectSites.site=='upbit'}">
            <option value="${selectSites.site}">업비트 (
               ${selectSites.site} )</option>
         </c:if>
         <c:if test="${selectSites.site!=site &&selectSites.site=='coinone'}">
            <option value="${selectSites.site}">코인원 (
               ${selectSites.site} )</option>
         </c:if>
         <c:if test="${selectSites.site==site && selectSites.site=='bithumb'}">
            <option value="${selectSites.site}" selected="selected">빗썸(
               ${selectSites.site} )</option>
         </c:if>
         <c:if test="${selectSites.site==site && selectSites.site=='upbit'}">
            <option value="${selectSites.site}" selected="selected">업비트(
               ${selectSites.site} )</option>
         </c:if>
         <c:if test="${selectSites.site==site && selectSites.site=='coinone'}">
            <option value="${selectSites.site}" selected="selected">코인원(
               ${selectSites.site} )</option>
         </c:if>
      </c:forEach>
   </select>
   <table class="table table-striped table-bordered table-hover"
      cellspacing="0" width="100%"
      style="width: 1650px; border-collapse: collapse; margin: 0 auto;">
      <thead class="header1">
			<tr>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td style="text-align : right">수익금 :</td>
			<td style="border : 0px; text-align : right" id="myTablesum"></td>
			</tr>
			<tr>
				<th>번호</th>
				<th>거래소</th>
				<th>코인명</th>
				<th>일자</th>
				<th>구분</th>
				<th>코인가격</th>
				<th>체결수량</th>
				<th>누적 체결수량</th>
				<th>평균매수가</th>
				<th>수수료</th>
				<th>거래금액</th>
				<th>누적 거래금액</th>
				<th>누적 수익 금액</th>
				<th>개별 수익 금액</th>
				<th>수익률</th>
				<th>일지</th>
				<th>상세작성</th>
			</tr>
		</thead>
      <tbody id="myTable">

      </tbody>
   </table>

   <ul class="pagination" id="page">

   </ul>


   </body>
   </html>

   <script>
      
   </script>
   <script src="/static/js/fresh.js"></script>
   <script src="/static/js/reportForm.js"></script>
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

   <%@ include file="../layout/footer.jsp"%>