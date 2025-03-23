<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<html>
<head>
    <title>회원가입</title>
</head>
<body>
    <h2>회원가입</h2>
    <c:if test="${not empty error}">
        <p style="color: red;">${error}</p>
    </c:if>
   <form action="${pageContext.request.contextPath}/user/signup" method="post">
        <label for="username">아이디:</label>
        <input type="text" name="username" required /><br/><br/>
        
        <label for="name">이름:</label>
        <input type="text" name="name" required /><br/><br/>
        
        <label for="email">이메일:</label>
        <input type="email" name="email" required /><br/><br/>
        
        <label for="password">비밀번호:</label>
        <input type="password" name="password" required /><br/><br/>
        
        <label for="address">주소:</label>
        <input type="text" name="address" /><br/><br/>
        
        <%-- Role은 직접 입력받지 않고 USER로 고정 --%>
        <input type="hidden" name="role" value="USER"/>
        
        <button type="submit">회원가입</button>
    </form>
</body>
</html>
