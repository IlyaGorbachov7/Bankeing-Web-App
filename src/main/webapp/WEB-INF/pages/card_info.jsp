<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="by.epam.baranovsky.banking.constant.DBMetadata" %>
<%@ page import="by.epam.baranovsky.banking.constant.Message" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale.locale"/>

<html>
<head>
    <title><fmt:message key="card.info.title" /></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/info.css"/>
</head>
<body>
<div class="container">
    <div class="formContainer">
        <h3><fmt:message key="card.info.title"/>:</h3>
        <br>
        <div class="whitebox">
            <table class="blankTable">
                <tr>
                    <td>
                        <b><fmt:message key="card.number"/></b>
                    </td>
                    <td>
                        <b><fmt:message key="card.cvc"/></b>
                    </td>
                    <td>
                        <b><fmt:message key="card.pin"/></b>
                    </td>
                    <td>
                        <b><fmt:message key="card.expiration"/></b>
                    </td>
                </tr>
                <tr>
                    <td>
                        ${CARD_DATA.number}
                    </td>
                    <td>
                        ${CARD_DATA.cvc}
                    </td>
                    <td>
                        ${CARD_DATA.pin}
                    </td>
                    <td>
                        ${CARD_DATA.expirationDate}
                    </td>
                </tr>
                <tr>
                    <td>
                        <b><fmt:message key="card.type"/></b>
                    </td>
                    <td>
                        <b><fmt:message key="card.registration"/></b>
                    </td>
                    <c:choose>
                        <c:when test="${CARD_DATA.cardTypeId == DBMetadata.CARD_TYPE_CREDIT}">
                            <td>
                                <b><fmt:message key="card.balance"/></b>
                            </td>
                            <td>
                                <b><fmt:message key="card.interest"/></b>
                            </td>
                        </c:when>
                        <c:when test="${CARD_DATA.cardTypeId == DBMetadata.CARD_TYPE_OVERDRAFT}">
                            <td>
                                <b><fmt:message key="card.overdraft.limit"/></b>
                            </td>
                            <td>
                                <b><fmt:message key="card.interest"/></b>
                            </td>
                        </c:when>
                    </c:choose>
                </tr>
                <tr>
                    <td>
                        <c:choose>
                            <c:when test="${CARD_DATA.cardTypeId == DBMetadata.CARD_TYPE_DEBIT}">
                                <fmt:message key="card.type.debit"/>
                            </c:when>
                            <c:when test="${CARD_DATA.cardTypeId == DBMetadata.CARD_TYPE_OVERDRAFT}">
                                <fmt:message key="card.type.overdraft"/>
                            </c:when>
                            <c:when test="${CARD_DATA.cardTypeId == DBMetadata.CARD_TYPE_CREDIT}">
                                <fmt:message key="card.type.credit"/>
                            </c:when>
                        </c:choose>
                    </td>
                    <td>
                        ${CARD_DATA.registrationDate}
                    </td>
                    <c:choose>
                        <c:when test="${CARD_DATA.cardTypeId == DBMetadata.CARD_TYPE_CREDIT}">
                            <td>${CARD_DATA.balance}$</td>
                            <td>${CARD_DATA.overdraftInterestRate}%</td>
                        </c:when>
                        <c:when test="${CARD_DATA.cardTypeId == DBMetadata.CARD_TYPE_OVERDRAFT}">
                            <td>${CARD_DATA.overdraftMax}$</td>
                            <td>${CARD_DATA.overdraftInterestRate}%</td>
                        </c:when>
                    </c:choose>
                </tr>
                <c:if test="${CARD_DATA.cardTypeId == DBMetadata.CARD_TYPE_OVERDRAFT}">
                    <tr>
                        <td>
                            <b><fmt:message key="card.info.overdrafted"/>:</b>
                        </td>
                        <td>${CARD_OVERDRAFTED}$</td>
                    </tr>
                </c:if>
                <tr>
                    <td><b><fmt:message key="card.user"/>:</b></td>
                    <td colspan="3">
                        ${CARD_USER.firstName} ${CARD_USER.lastName}
                        <c:if test="${not empty CARD_USER.patronymic}">${CARD_USER.patronymic}</c:if>
                    </td>
                </tr>
                <tr>
                    <td><b><fmt:message key="card.acc"/>:</b></td>
                    <td colspan="3">
                        ${CARD_ACCOUNT.accountNumber}
                    </td>
                </tr>
                <tr>
                    <td colspan="4">
                        <c:if test="${not empty ERROR_MSG}">
                            <i class="errorMsg">
                                <fmt:message key="logination.error"/>:
                                <c:choose>
                                    <c:when test="${ERROR_MSG eq Message.CANT_ACCESS_CARD_INFO}">
                                        <fmt:message key="card.info.no.rights"/>
                                    </c:when>
                                    <c:otherwise>
                                        <fmt:message key="error.unknown"/>
                                    </c:otherwise>
                                </c:choose>
                            </i>

                        </c:if>
                    </td>
                </tr>
            </table>

        </div>
        <br>
        <c:choose>
            <c:when test="${not empty PREV_PAGE}">
                <a href="${PREV_PAGE}">
                    <fmt:message key="card.info.back"/>
                </a>
            </c:when>
            <c:otherwise>
                <a href="controller?command=go_to_cards_page">
                    <fmt:message key="card.info.back"/>
                </a>
            </c:otherwise>
        </c:choose>


    </div>

</div>
</body>
</html>
