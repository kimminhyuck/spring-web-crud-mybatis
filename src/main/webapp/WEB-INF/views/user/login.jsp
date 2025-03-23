<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<html>
<head>
    <title>로그인</title>
</head>
<body>
    <h2>로그인</h2>
    <c:if test="${not empty error}">
        <p style="color: red;">${error}</p>
    </c:if>
    <form action="${pageContext.request.contextPath}/user/login" method="post">
        <label for="username">아이디:</label>
        <input type="text" name="username" required /><br/><br/>
        
        <label for="password">비밀번호:</label>
        <input type="password" name="password" required /><br/><br/>
        
        <button type="submit">로그인</button>
    </form>
    <p>회원이 아니신가요? <a href="${pageContext.request.contextPath}/signup">회원가입</a></p>
</body>
</html>
