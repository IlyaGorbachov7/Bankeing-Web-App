<%
    int code = pageContext.getErrorData().getStatusCode();
%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale.locale"/>

<html>
<head>
    <title><fmt:message key="error.title"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/error.css"/>

</head>
<body>
    <div class="container">
        <div class="errorContainer">
            <h2><fmt:message key="error.error"/>
                <% if(code != 0){out.println(code);}%>
            </h2>
            <br>
            <img src="${pageContext.request.contextPath}/img/errorArt.png"/>
            <br>
            <c:if test="${not empty ERROR_MSG}">
                <fmt:message key="error.unknown"/>
            </c:if>
            <br>
            <a href="controller?command=go_to_main_page"><fmt:message key="error.back"/></a>

        </div>
    </div>
</body>
</html>
