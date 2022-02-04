<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="by.epam.baranovsky.banking.constant.DBMetadata" %>
<%@ page import="by.epam.baranovsky.banking.constant.Message" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale.locale"/>

<c:set var="pending_bills" scope="page" value="${BILL_REQUESTS}"/>
<c:set var="totalCount" scope="page" value="${fn.length(pending_bills)}"/>
<c:set var="perPage" scope="page"  value="${5}"/>
<c:set var="pageStart" value="${param.start}"/>

<c:if test="${empty pageStart or pageStart < 0}">
    <c:set var="pageStart" value="0"/>
</c:if>
<c:if test="${totalCount < pageStart}">
    <c:set var="pageStart" value="${pageStart - perPage}"/>
</c:if>

<html>
<head>
    <title><fmt:message key="bill.requests.title"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css"/>
</head>
<body>
<div class="container">
    <div class="imgColumn">
        <h2><fmt:message key="bill.requests.title"/></h2>
        <a href="controller?command=go_to_bill_requests&start=${pageStart - perPage}"><fmt:message key="home.operations.prev.page"/></a>${pageStart + 1} - ${pageStart + perPage}
        <a href="controller?command=go_to_bill_requests&start=${pageStart + perPage}"><fmt:message key="home.operations.next.page"/></a>
        <c:forEach var="bill" items="${pending_bills}" begin="${pageStart}" end="${pageStart + perPage - 1}">
            <div class="operationbox">
                <table class="blankTable">
                    <tr>
                        <td><b><fmt:message key="operations.value"/></b></td>
                        <td><b><fmt:message key="loans.issue"/></b></td>
                        <td><b><fmt:message key="loans.due"/></b></td>
                    </tr>
                    <tr>
                        <td>${bill.value}$</td>
                        <td>${bill.issueDate}</td>
                        <td>${bill.dueDate}</td>
                    </tr>
                    <tr>
                        <td style="padding-top: 1em"><b><fmt:message key="bills.for"/>:</b></td>
                        <td colspan="3">
                            <a href="controller?command=go_to_user_info&checked_user_id=${bill.userId}">
                                    ${bill.userFullName}
                            </a>
                        </td>
                    </tr>
                    <tr>
                        <td><b><fmt:message key="bills.from"/>:</b></td>
                        <td colspan="3">
                            <a href="controller?command=go_to_user_info&checked_user_id=${bill.bearerId}">
                                    ${bill.bearerFullName}
                            </a>
                        </td>
                    </tr>
                    <c:if test="${not empty bill.penaltyId and bill.penaltyId != 0}">
                        <tr>
                            <td><b><fmt:message key="bills.penalty"/></b></td>
                            <td>
                                <c:choose>
                                    <c:when test="${bill.penaltyTypeId eq DBMetadata.PENALTY_TYPE_FEE}">
                                        <fmt:message key="penalty.type.fee"/>
                                    </c:when>
                                    <c:when test="${bill.penaltyTypeId eq DBMetadata.PENALTY_TYPE_LAWSUIT}">
                                        <fmt:message key="penalty.type.lawsuit"/>
                                    </c:when>
                                    <c:when test="${bill.penaltyTypeId eq DBMetadata.PENALTY_TYPE_ACCS_LOCK}">
                                        <fmt:message key="penalty.type.acc.lock"/>
                                    </c:when>
                                    <c:when test="${bill.penaltyTypeId eq DBMetadata.PENALTY_TYPE_CARDS_LOCK}">
                                        <fmt:message key="penalty.type.card.lock"/>
                                    </c:when>
                                    <c:when test="${bill.penaltyTypeId eq DBMetadata.PENALTY_TYPE_ACCS_SUSP}">
                                        <fmt:message key="penalty.type.acc.suspension"/>
                                    </c:when>
                                </c:choose>
                            </td>
                            <c:if test="${not empty bill.penaltyValue and bill.penaltyValue != 0}">
                                <td><b><fmt:message key="operations.value"/>: </b></td>
                                <td>${bill.penaltyValue}$</td>
                            </c:if>
                        </tr>
                    </c:if>
                    <c:if test="${not empty bill.notice}">
                        <tr>
                            <td><b><fmt:message key="penalty.notice"/> </b></td>
                            <td colspan="3">${bill.notice}</td>
                        </tr>
                    </c:if>
                    <tr>
                        <td></td>
                        <td>
                            <a href="controller?command=approve_bill&bill_id=${bill.id}">
                                <fmt:message key="bill.requests.approve"/>
                            </a>
                        </td>
                        <td>
                            <a href="controller?command=delete_bill_request&bill_id=${bill.id}">
                                <fmt:message key="bill.requests.deny"/>
                            </a>
                        </td>
                    </tr>
                </table>
            </div>
        </c:forEach>

    </div>
    <div class="userColumn">
        <div class="whitebox">
            <a href="controller?command=go_to_pending_accounts">
                <fmt:message key="accounts.pending.requests"/>
            </a>
        </div>
        <div class="whitebox">
            <a href="controller?command=go_to_all_users">
                <fmt:message key="all.users.title"/>
            </a>
        </div>
        <div class="whitebox">
            <a href="controller?command=go_to_main_page">
                <fmt:message key="edit.user.goto.main"/>
            </a>
        </div>
        <c:if test="${not empty ERROR_MSG}">
            <div class="whitebox">
                <i class="errorMsg">
                    <fmt:message key="logination.error"/>:
                    <c:choose>
                        <c:when test="${ERROR_MSG eq Message.BILL_CANNOT_APPROVE}">
                            <fmt:message key="bill.requests.err.cannot.approve"/>
                        </c:when>
                        <c:when test="${ERROR_MSG eq Message.BILL_CANNOT_DELETE}">
                            <fmt:message key="bill.requests.err.cannot.delete"/>
                        </c:when>
                        <c:otherwise>
                            <fmt:message key="error.unknown"/>
                        </c:otherwise>
                    </c:choose>
                </i>
            </div>
        </c:if>
        <br>
        <a href="controller?command=logout"><fmt:message key="home.logout.button"/></a>
        <br>
        <div>
            <a href="controller?command=change_locale&locale=en">EN  </a>
            <a href="controller?command=change_locale&locale=ru">  RU</a>
        </div>
    </div>
</div>
</body>
</html>
