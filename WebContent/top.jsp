<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>簡易Twitter</title>
<div class="header">
	<c:if test="${ empty loginUser }">
		<a href="login">ログイン</a>
		<a href="signup">登録する</a>
	</c:if>
	<c:if test="${ not empty loginUser }">
	<%--テキストや画像をリンクさせる→リンク範囲をa要素のタグで囲って、リンク先のURLをhref属性の値として指定 --%>
		<a href="./">ホーム</a>
		<a href="setting">設定</a>
		<a href="logout">ログアウト</a>
	</c:if>
</div>
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
<link href="./css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
    <div class="filter">
        <form action="./" method="get">
            <input type="date" name="startDate" value="${param.startDate}">
            <input type="date" name="endDate" value="${param.endDate}">
            <input type="submit" value="絞込">
        </form>
    </div>
	<c:if test="${ not empty errorMessages }">
		<div class="errorMessages">
			<ul>
				<c:forEach items="${errorMessages}" var="errorMessage">
					<li><c:out value="${errorMessage}" /></li>
				</c:forEach>
			</ul>
		</div>
		<%--変数を削除→sessionの情報（エラーメッセージ）を都度削除している --%>
		<c:remove var="errorMessages" scope="session" />
	</c:if>

	<div class="form-area">
		<!--  isShowMessageForm…ログイン後、つぶやきのフォームを展開する -->
		<c:if test="${ isShowMessageForm }">
			<form action="message" method="post">
				いま、どうしてる？<br />
				<textarea name="text" cols="100" rows="5" class="tweet-box"></textarea>
				<br /> <input type="submit" value="つぶやく">（140文字まで）
			</form>
		</c:if>
	</div>
	<div class="messages" style="margin-bottom: 10px;">
	<%--forEachは繰り返し文 Servletから受け取ったList<UserMessage>型のmessagesを、messageという形に格納 --%>
		<c:forEach items="${messages}" var="message">
			<div class="message">
				<div class="account-name">
				<%--文章の一部をグループ化するためのインラインの範囲を示す --%>
					<span class="account">
					<%--messageのuserIdを表示する --%>
					<a href="./?user_id=<c:out value="${message.userId}"/> "> <c:out
								value="${message.account}" />
					</a>
					</span> <span class="name"><c:out value="${message.name}" /></span>
				</div>
				<div class="text" style="white-space: pre-wrap;"><c:out value="${message.text}" /></div>
				<div class="date" style="margin-bottom: 6px;">
					<fmt:formatDate value="${message.createdDate}"
						pattern="yyyy/MM/dd HH:mm:ss" />
				</div>
				<%--<c:if>タグのtest属性に記述した条件が一致した場合に、<c:if>から</c:if>までの内容を実行--%>
				<c:if test="${loginUser.id == message.userId}">
					<form action="deleteMessage" method="post" style="display:inline-block; margin-right:5px;">
						<input name="messageId" value="${message.id}" type="hidden"/>
						<input type="submit" value="削除">
					</form>
					<form action="edit" method="get" style="display:inline-block;">
						<input name="messageId" value="${message.id}" type="hidden"/>
						<input type="submit" value="編集">
					</form>
				</c:if>
			</div>
			<%--返信したメッセージを表示 --%>
			 <div class="comments" style="margin-bottom: 6px;">
				<c:forEach items="${comments}" var="comment">
					<c:if test="${comment.messageId == message.id}">
						<div class="comment">
							<span class="account">
							<a href="./?user_id=<c:out value="${message.userId}"/> "> <c:out
								value="${comment.account}" />
							</a>
							<%--c:out（変数を出力）→comment.nameを出力--%>
							</span><span class="name"><c:out value="${comment.name}" /></span>
							<div class="text" style="white-space: pre-wrap;"><c:out value="${comment.text}" /></div>
								<div class="date">
								<%--<fmt:formatDate>タグは、日付のフォーマットを行うタグ --%>
									<fmt:formatDate value="${comment.createdDate}" pattern="yyyy/MM/dd HH:mm:ss" />
								</div>
						</div>
					</c:if>
				</c:forEach>
			</div>
			<!-- 返信フォーム -->
			<c:if test="${isShowMessageForm}">
				<form action="comment" method="post">
					<textarea name="comment" cols="100" rows="5" class="tweet-box"></textarea><br />
					<input type="hidden" name="messageId" value="${message.id}" />
					<input type="submit" value="返信">（140文字まで）
				</form>
			</c:if>
		</c:forEach>
	</div>
	<div class="copyright">Copyright(c)TaguchiKeita</div>
</body>
</html>
