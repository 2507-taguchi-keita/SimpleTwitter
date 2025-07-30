<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>つぶやきの編集</title>
<link href="./css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div class="header">
		<c:if test="${ not empty loginUser }">
			<a href="./">ホーム</a>
			<a href="setting">設定</a>
			<a href="logout">ログアウト</a>
		</c:if>
		<c:if test="${ not empty loginUser }">
			<div class="profile">
				<div class="name">
					<h2>
						<c:out value="${loginUser.name}" />
					</h2>
				</div>
				<div class="account">
					@
					<c:out value="${loginUser.account}" />
				</div>
				<div class="description">
					<c:out value="${loginUser.description}" />
				</div>
			</div>
		</c:if>
	</div>
	<c:if test="${ not empty errorMessages }">
	<div class="errorMessages">
		<ul>
			<c:forEach items="${errorMessages}" var="errorMessage">
				<li><c:out value="${errorMessage}" /></li>
			</c:forEach>
		</ul>
	</div>
	</c:if>
<%--インデントがずれている　タブとインデントがまざっている --%>
	<div class="form-area">
		<form action="edit" method="post">
			つぶやきの編集<br />
			<textarea name="text" cols="100" rows="5" class="tweet-box">${message.text}</textarea> <!-- 修正 -->
 			<input name="messageId" value="${message.id}" type="hidden"/> <!-- 修正 -->
			<br />
			<input type="submit" value="更新">（140文字まで）
			<br />
			<a href="./">戻る</a>
		</form>
	</div>
	<div class="copyright">Copyright(c)TaguchiKeita</div>
</body>
</html>
