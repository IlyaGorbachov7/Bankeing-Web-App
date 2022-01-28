<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="by.epam.baranovsky.banking.constant.DBMetadata" %>
<%@ page import="by.epam.baranovsky.banking.constant.Message" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale.locale"/>

<html>
<head>
    <title><fmt:message key="transfer.failure.title"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css"/>
</head>
<body>
<div class="formContainer">
    <h3><fmt:message key="transfer.failure.header"/></h3>
    <div class="whitebox">
        <c:choose>
            <c:when test="${ERROR_MSG eq Message.NOT_YOUR_ACCOUNT}">
                <fmt:message key="accounts.error.alien"/>
            </c:when>
            <c:when test="${ERROR_MSG eq Message.OPERATION_TRANSFER_TO_SELF}">
                <fmt:message key="transfer.error.to.self"/>
            </c:when>
            <c:when test="${ERROR_MSG eq Message.NO_MONEY}">
                <fmt:message key="transfer.error.no.money"/>
            </c:when>
            <c:when test="${ERROR_MSG eq Message.CARD_NOT_YOURS}">
                <fmt:message key="card.not.yours"/>
            </c:when>
            <c:when test="${ERROR_MSG eq Message.OPERATION_ILLEGAL}">
                <fmt:message key="transfer.err.illegal"/>
            </c:when>
            <c:when test="${ERROR_MSG eq Message.OPERATION_INVALID_VALUE}">
                <fmt:message key="transfer.err.invalid.value"/>
            </c:when>
            <c:when test="${ERROR_MSG eq Message.OPERATION_NOT_ENOUGH_DATA}">
                <fmt:message key="transfer.err.no.data"/>
            </c:when>
            <c:when test="${ERROR_MSG eq Message.CREDIT_CARD}">
                <fmt:message key="transfer.error.credit"/>
            </c:when>
            <c:when test="${ERROR_MSG eq Message.ACC_SUSPENDED}">
                <fmt:message key="transfer.error.acc.suspended"/>
            </c:when>
            <c:when test="${ERROR_MSG eq Message.ACC_LOCKED}">
                <fmt:message key="transfer.error.acc.locked"/>
            </c:when>
            <c:when test="${ERROR_MSG eq Message.CARD_LOCKED}">
                <fmt:message key="transfer.error.card.locked"/>
            </c:when>
            <c:when test="${ERROR_MSG eq Message.PENALTY_BILL_INTERSECTION}">
                <fmt:message key="transfer.err.penalty.bill.intersect"/>
            </c:when>
            <c:otherwise>
                <fmt:message key="error.unknown"/>
            </c:otherwise>
        </c:choose>
    </div>
    <br>
    <a href="controller?command=go_to_transfer_page">
        <fmt:message key="transfer.title"/>
    </a>
    <br>
    <a href="controller?command=go_to_main_page">
        <fmt:message key="edit.user.goto.main"/>
    </a>
</div>
</body>
</html>
