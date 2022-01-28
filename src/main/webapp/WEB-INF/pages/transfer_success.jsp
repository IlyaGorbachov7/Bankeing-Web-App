<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale.locale"/>

<html>
<head>
    <title><fmt:message key="transfer.success.title" />!</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css"/>
</head>
<body>
<div class="formContainer">
    <h3><fmt:message key="transfer.success.header"/></h3>
    <div class="whitebox">
        <a href="controller?command=go_to_transfer_page">
            <fmt:message key="transfer.title"/>
        </a>
        <br>
        <a href="controller?command=go_to_main_page">
            <fmt:message key="edit.user.goto.main"/>
        </a>
    </div>
</div>
</body>
</html>
