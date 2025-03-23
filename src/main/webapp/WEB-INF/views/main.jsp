<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<html>
<head>
    <title>Spring MVC 메인 페이지</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-5">
        <h1 class="text-center">메인 페이지 깃허브에 올렷습니다</h1>

        <!-- 현재 로그인 상태 확인 -->
        <c:choose>
            <c:when test="${not empty sessionScope.user}">
                <p class="text-center">환영합니다, <strong>${sessionScope.user.name}</strong>님!</p>

                <!-- 로그아웃 & 관리자 페이지 버튼 -->
                <div class="text-center">
					<!-- POST 방식으로 로그아웃 요청 -->
					                    <form action="${pageContext.request.contextPath}/user/logout" method="post">
					                        <button type="submit" class="btn btn-danger">로그아웃</button>
					                    </form>
                    
                    <c:if test="${sessionScope.user.role == 'ADMIN'}">
                        <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-primary">관리자 페이지</a>
                    </c:if>
                </div>
            </c:when>
            
            <c:otherwise>
                <p class="text-center">로그인이 필요합니다.</p>
                <div class="text-center">
                    <!-- 로그인 페이지로 이동 -->
                    <a href="${pageContext.request.contextPath}/user/login" class="btn btn-success">로그인</a>
                    <a href="${pageContext.request.contextPath}/user/signup" class="btn btn-info">회원가입</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
	
	<%@ include file="/WEB-INF/views/common/footer.jsp" %>
</body>
</html>
