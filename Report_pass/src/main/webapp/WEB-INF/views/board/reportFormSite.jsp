<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="../layout/header.jsp"%>


<!-- <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet" /> -->
<link href="https://cdnjs.cloudflare.com/ajax/libs/datatables/1.10.12/css/dataTables.bootstrap.min.css"
    rel="stylesheet" />

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

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

    select {
        border-style: none;
        border: 0.5px solid #BEBEBE;
    }

    select:focus {
        outline: none;
    }

    input[type=date] {
        width: 138px;
    }

    input[type=date]:focus {
        outline: none;
    }

    /* 로딩*/
    #loading {
        height: 100%;
        left: 0px;
        position: fixed;
        _position: absolute;
        top: 0px;
        width: 100%;
        filter: alpha(opacity=50);
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
    }

    /* 로딩 끝 */

    .tu {
        text-align: center;
        display: inline-block;
        margin-right: 10px;
    }

    .container {
        max-width: 1200px;
        border-collapse: collapse;
        margin: 0 auto;
        padding-bottom: 300px;
    }

    .table_box {
        max-width: 1200px;
        /* overflow: scroll; */
        /* overflow-y: hidden; */
    }

    @media (max-width: 800px) {
        .table_box {
            overflow: scroll;
            overflow-y: hidden;
        }
    }

    .table-bordered {

        width: 100%;
        border-collapse: collapse;
        margin: 0 auto;
        white-space: nowrap;
    }

    .table-bordered td {
        font-size: 14px;
    }

    .pagination {
        margin-top: 20px;
    }

    .table-striped tbody tr:nth-of-type(odd) {
        background-color: #f9f9f9;
    }

    .btn-primary {
        color: #fff;
        background-color: #337ab7;
        border-color: #2e6da4;
    }

    .btn {
        font-size: 14px;

    }

    .table td {
        vertical-align: inherit;
        padding: 6px;
    }

    .table th {
        vertical-align: inherit;
        padding: 6px;
        background-color: #F5F5F5;
    }

    /* select {
    border-style: none;
    width: 140px;
    height: 30px;
    border: 1px solid #BEBEBE;
} */
    .page-link {
        cursor: pointer;
    }









    .dateSelectFlex {
        display: flex;
        justify-content: space-between;
        margin-bottom: 20px;
        align-items: center;
        font-size: 15px;
    }

    .viewUpdateFlex {
        display: flex;
        justify-content: space-between;
        margin-top: 40px;
        margin-bottom: 40px;
    }

    .bithumbFile {
        margin-bottom: 40px;
        font-size: 15px;
    }
    .table-bordered th {
        border: none;
    }
    .btn {
        vertical-align: initial;
    }



    @media (max-width: 920px) {
        .dateSelectFlex {
            flex-direction: column;
            align-items: start;
        }

        .dateBox {
            margin-bottom: 10px;
        }
    }

    @media (max-width: 400px) {
        #bithumbExcelFile {
            width: 250px;
        }
    }

</style>

<div id="loading" class="loading">
    <div style='vertical-align: middle; text-align: center; hight: 400px;'></div>
    <img id="loading_img" alt="loading" src="/static/images/viewLoading.gif" />
</div>


<div id="warp">
    <div class="container">

        <div style="margin-top: 20px;">
            <h2>
                <span>체결내역 보고서</span>
            </h2>
             <p>원하는 코인, 거래소및 기간을 선택하세요.
             <span style="margin-left: 1050px;">
                <c:if test="${site=='coinone'}">
                           	코인원
                    </c:if>
                    <c:if test="${site=='upbit'}">
                            업비트
                    </c:if>
                     <c:if test="${site=='bithumb'}">
                             빗썸
                    </c:if>
               </span>
                <button type="button" class="btn btn-primary" onclick="refresh('${site}')"
                    style="float:right">초기화</button>
             
           </div>
       





        <div style="margin-top: 20px;margin-left: 300px;">
            <p id='roadingStr'></p>
        </div>

        <div class="viewUpdateFlex">
            <div>
                <div style="text-align: right;">
                    <!-- 코인원에만 -->
                    <%--             <c:if test="${site=='coinone'}">
                        <br>
                        <div class="tu">
                            <span>코인 : </span>
                            <input type='text' id="coinone_currency" />
                        </div>
                    </c:if> --%>

                    <button type="button" class="btn btn-primary" onclick="updateLately('${site}')">업데이트</button>
                </div>

                <c:set var="site" value="${site}"></c:set>
            </div>

            <div>
                <c:choose>
                    <c:when test="${empty  statementList}">
                        <button type="button" class="btn btn-primary" onclick="rerevenue('${site}')">수익률보기</button>
                        <%-- <button type="button" class="btn btn-primary" onclick="allrevenue('${site}')">전체 재계산</button> --%>
                    </c:when>
                    <c:otherwise>

                    </c:otherwise>
                </c:choose>
            </div>
        </div>




        <%-- <button type="button" class="btn btn-primary" onclick="insertPrice('${site}')">입출금 가격 입력</button> --%>

        <!-- 빗썸에만 -->
        <div class="bithumbFile">
            <c:if test="${site=='bithumb'}">

                <p style="float: right;">누락된 이전 데이터가 있으면 해당 거래소에서 다운받아 파일그대로 넣으세요.</p>

                <br>
                <br>

                <form action="/excel/read" method="POST" enctype="multipart/form-data" id="fileUploadForm">
                    <input class="btn btn-primary" id="excelSubmit" type="submit" value="제출" style="float: right;" />
                    <input type="file" name="file" id="bithumbExcelFile" style="float: right;">
                </form>

                <br>
                <br>

                <a href="#" onclick='window.open("https://www.bithumb.com/member_operation/login?reurl=%2Ftrade_history%2Fhistory","window팝업","width=1200,height=800,menubar=no,status=no,toolbar=no")'
                    style="float: right;">빗썸 내역 다운받기</a>

                <br>

                <a href="#"
                    onclick='window.open("/images/detail.jpg","window팝업","width=590,height=840,menubar=no,status=no,toolbar=no")'
                    style="float: right;">빗썸 내역 다운받는 법
                </a>

                <br>
            </c:if>
        </div>


        <!-- 코인원에만 -->
        <%--         <c:if test="${site=='coinone'}">
            <p style="float: right;">누락된 이전 데이터가 있으면 해당 거래소에서 다운받아 csv파일그대로 넣으세요.</p>
            <br>
            <br>
            <form action="/excel/readCoinone" method="POST" enctype="multipart/form-data" id="fileUploadFormCoinone">
                <input class="btn btn-primary" id="excelSubmitCoinone" type="submit" value="제출" style="float: right;" />
                <input type="file" name="file" id="bithumbExcelFile" style="float: right;">
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

        </c:if> --%>












        <div class="dateSelectFlex">
            <div class="dateBox">
                <input type="hidden" id="site" value="${site}"> <input type="hidden" id="orderId" value="${orderId}">
                <label>기간:</label>
                <input type="date" id="start"> <label>~</label> <input type="date" id="end">
                <input type="button" class="btn btn-primary" id="date" onclick="date()" value="기간 검색" style="height: 28px; padding: 0px 10px;">
            </div>

            <div>
                <select id="ListCount" style="float: right; height: 25px;">
                    <option value="10">10 개</option>
                    <option value="20">20 개</option>
                    <option value="30">30 개</option>
                    <option value="50" selected="selected">50 개</option>
                    <option value="100">100 개</option>

                </select>
                <select id="type" style="float: right; height: 25px;">
                    <option value="ALL" selected="selected">모두</option>
                    <option value="매수">매수</option>
                    <option value="매도">매도</option>
                    <option value="입금">입금</option>
                    <option value="출금">출금</option>
                </select>

                <select id="orderby" style="float: right; height: 25px;">
                    <option value="DESC" selected="selected">최근순</option>
                    <option value="ASC">과거순</option>
                </select>

                <select style="float: right; height: 25px;" id="currency">
                    <option value="ALL" selected="selected">모든코인</option>
                    <c:forEach var="selectsCoins" items="${selectsCoins}">
                        <option value="${selectsCoins.currency}">${selectsCoins.currency}</option>
                    </c:forEach>
                </select>

                <select style="float: right; height: 25px;" id="selectsite">
                    <c:forEach var="selectSites" items="${selectsSites}">
                        <c:if test="${selectSites.site!=site &&selectSites.site=='bithumb'}">
                            <option value="${selectSites.site}">빗썸 ( ${selectSites.site} )</option>
                        </c:if>
                        <c:if test="${selectSites.site!=site && selectSites.site=='upbit'}">
                            <option value="${selectSites.site}">업비트 ( ${selectSites.site} )</option>
                        </c:if>
                        <c:if test="${selectSites.site!=site &&selectSites.site=='coinone'}">
                            <option value="${selectSites.site}">코인원 (
                                ${selectSites.site} )</option>
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
            </div>
        </div>








        <div class="table_box">
            <table class="table table-striped table-bordered table-hover" cellspacing="0">
                <thead class="header1">
                    <tr>
                        <td></td>
                        <!-- <td></td> -->
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td style="text-align : right">수익금 :</td>
                        <td style="border-right: none; text-align : right" id="myTablesum"></td>
                        <td></td>
                        <td></td>

                    </tr>
                    <tr>
                        <th style="width: 80px;">번호</th>
                        <th style="width: 80px;">거래소</th>
                        <th style="width: 80px;">코인명</th>
                        <th style="width: 200px;">일자</th>
                        <th style="width: 80px;">구분</th>
                        <!-- <th>코인가격</th> -->
                        <!-- <th>체결수량</th> -->
                        <th style="width: 100px;">체결수량</th>
                        <th style="width: 120px;">평균매매가</th>
                        <th style="width: 80px;">수수료</th>
                        <!-- <th>거래금액</th> -->
                        <th style="width: 100px;">거래금액</th>
                        <th style="width: 80px;">수익 금액</th>
                        <!-- <th>개별 수익 금액</th> -->
                        <th style="width: 100px;">수익률</th>
                        <th style="width: 100px;">일지</th>
                    </tr>
                </thead>
                <tbody id="myTable">

                </tbody>
            </table>
        </div>

        <ul class="pagination" id="page">

        </ul>

    </div>
</div>


</body>

</html>

<script src="/static/js/fresh.js"></script>
<script src="/static/js/reportForm.js"></script>
<script src="/static/js/selectBox.js"></script>
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script src="https://cdn.datatables.net/1.10.22/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/1.10.22/js/dataTables.bootstrap4.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/datatables/1.10.12/js/jquery.dataTables.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/datatables/1.10.12/js/dataTables.bootstrap.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<%@ include file="../layout/footer.jsp"%>