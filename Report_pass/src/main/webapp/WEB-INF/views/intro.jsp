<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="layout/header.jsp"%>
<html>

<head>
    <!-- <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"> -->
    <link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-bs4.min.css" rel="stylesheet">

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-bs4.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
    <script src="//code.jquery.com/ui/1.8.18/jquery-ui.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

    <style>
        .container {
            max-width: 960px;
        }

        .m-2 {
            margin: 0px !important;
        }

        .new_sccount {
            margin-bottom: 20px;
        }

        .table td,
        .table th {
            vertical-align: inherit;
            border-top: none;
        }

        .table th {
            border-bottom: 1px solid #dee2e6;
            ;
        }

        .table {
            margin-bottom: 0px;
            display: flex;
            justify-content: space-between;
            text-align: center;
        }

        .table .itemTotal {
            width: 50%;
        }

        .tableFlex {
            display: flex;
            width: 55%;
        }

        .detailBtn {
            width: 84px;
            font-size: 14px;
        }

        p {
            margin: 0px;
        }

        .buttonBox {
            align-self: center;
            width: 45%;
            text-align: right;
        }

        .buttonBox button {
            margin-right: 10px;
        }

        .itemTotal .item-1 {
            text-align: center;
            margin-bottom: 16px;
        }

        .itemTotal .item-3 {
            text-align: center;
            margin-bottom: 16px;
        }

        @media (max-width: 740px) {

            .buttonBox {
                margin-top: 0px;
                width: auto;
                display: flex;
                flex-direction: column;
                text-align: right;
                margin-left: 10px;
            }

            .buttonBox button {
                margin-bottom: 6px;
                margin-right: 0px;
            }

            .tableFlex {
                display: flex;
                width: 100%;
                flex-direction: column;
                align-self: center;
            }

            .table .itemTotal {
                width: 94%;
                display: flex;
                justify-content: space-between;
            }

            .card-body {
                padding: 14px;
            }

            .itemTotal .item-1 {
                margin-bottom: 16px;
            }
            .itemTotal .item-3 {
                margin-bottom: 0px;
            }

        }
    </style>

</head>

<body>

    <div id="warp">
        <div class="container">
            <button type="button" class="btn btn-primary new_sccount" style="float: center;" id="btn-apiCreate">계정
                새로만들기</button>

            <c:forEach var="row" items="${ list }">
                <form name="form1" method="post" action="/apikey" onsubmit="return confirm('수정하시겠습니까?')">


                    <div class="card m-2">
                        <div class="card-body">

                            <div class="table">
                                <div class="tableFlex">
                                    <div class="itemTotal">
                                        <p class="item-1">거래소</p>
                                        <p class="item-2">${row.site}</p>
                                    </div>

                                    <div class="itemTotal">
                                        <p class="item-3">거래가능 원화</p>
                                        <p class="item-4" style="text-align: center;">
                                            <c:choose>
                                                <c:when test="${row.site eq 'bithumb'}">
                                                    ${bithumbbal}
                                                </c:when>
                                                <c:when test="${row.site eq 'upbit'}">
                                                    ${upbitbal}
                                                </c:when>
                                                <c:when test="${row.site eq 'coinone'}">
                                                    ${coinonebal}
                                                </c:when>
                                            </c:choose>
                                        </p>
                                    </div>
                                </div>

                                <div class="buttonBox">
                                    <button type="button" class="btn btn-primary detailBtn" id="btnDetail"
                                        onclick="detailapi('${row.site}')">상세보기</button>
                                    <button type="submit" class="btn btn-primary detailBtn">수정</button>
                                    <button type="button" class="btn btn-primary detailBtn" id="btnDelete"
                                        onclick="deleteapi(${row.idx})"
                                        style="margin-bottom: 0px; margin-right: 0px;">삭제</button>
                                </div>
                            </div>




                            <input type="hidden" name="idx" id="idx" value="${row.idx}">
                            <input type="hidden" name="memId" id="memId" value="${row.memId}">
                            <input type="hidden" name="site" id="site" value="${row.site}">
                            <input type="hidden" name="apiKey" id="apiKey" value="${row.apiKey}">
                            <input type="hidden" name="secretKey" id="secretKey" value="${row.secretKey}">


                        </div>

                    </div>
                </form>
            </c:forEach>
        </div>
    </div>

    <script src="/static/js/user.js"></script>
    <script src="/static/js/edit.js"></script>

    <%@ include file="layout/footer.jsp"%>
</body>

</html>
