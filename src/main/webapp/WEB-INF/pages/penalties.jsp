<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="by.epam.baranovsky.banking.constant.DBMetadata" %>
<%@ page import="by.epam.baranovsky.banking.constant.Message" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale.locale"/>

<c:set var="perPage" scope="page"  value="${5}"/>
<c:set var="penalties" scope="page" value="${USER_PENALTIES}"/>
<c:set var="penaltyCount" scope="page" value="${fn.length(penalties)}"/>
<c:set var="penaltiesStart" value="${param.penalties_start}"/>
<c:if test="${empty penaltiesStart or penaltiesStart < 0}">
    <c:set var="penaltiesStart" value="0"/>
</c:if>
<c:if test="${penaltyCount < penaltiesStart}">
    <c:set var="penaltiesStart" value="${penaltiesStart - perPage}"/>
</c:if>

<html>
<head>
    <title><fmt:message key="penalties.title"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css"/>
</head>
<body>
<div class="container">
    <div class="imgColumn">
        <h2><fmt:message key="penalties.title"/> </h2>
        <a href="controller?command=command=go_to_penalties_page&penalties_start=${penaltiesStart - perPage}"><fmt:message key="home.operations.prev.page"/></a>${penaltiesStart + 1} - ${penaltiesStart + perPage}
        <a href="controller?command=go_to_penalties_page&penalties_start=${penaltiesStart + perPage}"><fmt:message key="home.operations.next.page"/></a>
        <c:forEach var="penalty" items="${penalties}" begin="${penaltiesStart}" end="${penaltiesStart + perPage - 1}">
            <div class="operationbox">
                <table class="blankTable">
                    <tr>
                        <td><b><fmt:message key="accounts.status"/></b></td>
                        <td><b><fmt:message key="card.type"/></b></td>
                        <c:if test="${not empty penalty.value and penalty.value != 0}">
                            <td><b><fmt:message key="operations.value"/></b></td>
                        </c:if>
                    </tr>
                    <tr>
                        <td>
                            <c:choose>
                                <c:when test="${penalty.statusId eq DBMetadata.PENALTY_STATUS_CLOSED}">
                                    <fmt:message key="loans.status.closed"/>
                                </c:when>
                                <c:when test="${penalty.statusId eq DBMetadata.PENALTY_STATUS_PENDING}">
                                    <fmt:message key="loans.status.pending"/>
                                </c:when>
                                <c:when test="${penalty.statusId eq DBMetadata.PENALTY_STATUS_INFLICTED}">
                                    <fmt:message key="penalty.status.inflicted"/>
                                </c:when>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${penalty.typeId eq DBMetadata.PENALTY_TYPE_FEE}">
                                    <fmt:message key="penalty.type.fee"/>
                                </c:when>
                                <c:when test="${penalty.typeId eq DBMetadata.PENALTY_TYPE_LAWSUIT}">
                                    <fmt:message key="penalty.type.lawsuit"/>
                                </c:when>
                                <c:when test="${penalty.typeId eq DBMetadata.PENALTY_TYPE_ACCS_LOCK}">
                                    <fmt:message key="penalty.type.acc.lock"/>
                                </c:when>
                                <c:when test="${penalty.typeId eq DBMetadata.PENALTY_TYPE_CARDS_LOCK}">
                                    <fmt:message key="penalty.type.card.lock"/>
                                </c:when>
                                <c:when test="${penalty.typeId eq DBMetadata.PENALTY_TYPE_ACCS_SUSP}">
                                    <fmt:message key="penalty.type.acc.suspension"/>
                                </c:when>
                            </c:choose>
                        </td>
                        <td>
                            <c:if test="${not empty penalty.value and penalty.value != 0}">
                                ${penalty.value}$
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td><b><fmt:message key="penalty.notice"/> </b></td>
                        <td colspan="3">
                                ${penalty.notice}
                        </td>
                    </tr>
                    <c:if test="${penalty.typeId eq DBMetadata.PENALTY_TYPE_FEE}">
                        <tr>
                            <td colspan="4">
                                <a href="controller?command=go_to_transfer_page&penalty_id=${penalty.id}">
                                    <fmt:message key="penalties.pay"/>
                                </a>
                            </td>
                        </tr>
                    </c:if>
                </table>
            </div>
        </c:forEach>
    </div>
    <div class="userColumn">
        <div class="whitebox">
            <a href="controller?command=go_to_accounts_page">
                <fmt:message key="accounts.title"/>
            </a>
        </div>
        <div class="whitebox">
            <a href="controller?command=go_to_cards_page">
                <fmt:message key="cards.title"/>
            </a>
        </div>
        <div class="whitebox">
            <a href="controller?command=go_to_bills_page">
                <fmt:message key="bills.title"/>
            </a>
        </div>
        <div class="whitebox">
            <a href="controller?command=go_to_loans_page">
                <fmt:message key="loans.title"/>
            </a>
        </div>
        <div class="whitebox">
            <a href="controller?command=go_to_transfer_page"><fmt:message key="transfer.title"/></a>
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
                        <c:when test="${ERROR_MSG eq Message.PENALTY_BILL_INTERSECTION}">
                            <fmt:message key="transfer.err.penalty.bill.intersect"/>
                        </c:when>
                        <c:otherwise>
                            <fmt:message key="error.unknown"/>
                        </c:otherwise>
                    </c:choose>
                </i>
            </div>
        </c:if>
        <a href="controller?command=logout"><fmt:message key="home.logout.button"/></a>
        <br>
        <br>
        <div>
            <a href="controller?command=change_locale&locale=en">EN  </a>
            <a href="controller?command=change_locale&locale=ru">  RU</a>
        </div>
    </div>
</div>

</body>
</html>
