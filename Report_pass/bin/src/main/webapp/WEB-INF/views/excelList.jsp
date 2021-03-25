<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org">
<head>
  <meta charset="UTF-8">
  <title>Title</title>
  
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
</head>
<body>
  <table class="table table-striped">
    <thead>
    <tr>
      <th scope="col">거래소</th>
      <th scope="col">주문아이디</th>
      <th scope="col">거래일시</th>
      <th scope="col">수수료</th>
      <th scope="col">자산</th>
      <th scope="col">구분</th>
      <th scope="col">가격</th>
      <th scope="col">수량</th>
      <th scope="col">거래금액</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="datas" items="${datas}">
    <tr>
      <td>${datas.site}</td>
      <td>${datas.orderId}</td>
      <td>${datas.transactionDate}</td>
      <td>${datas.fee}</td>
      <td>${datas.currency}</td>
      <td>${datas.type}</td>
      <td>${datas.price}</td>
      <td>${datas.units}</td>
      <td>${datas.totalPrice}</td>
    </tr>
    </c:forEach>
    </tbody>
  </table>
</body>
</html>