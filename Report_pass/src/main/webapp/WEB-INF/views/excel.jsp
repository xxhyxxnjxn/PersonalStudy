<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org">
  <head>
    <meta charset="UTF-8">
    <title>엑셀 업로드</title>
  </head>

  <body>
    <form action="/excel/read" method="POST" enctype="multipart/form-data">
      <input type="file" name="file">
      <input type="submit" value="제출" />
    </form>
  </body>

</html>