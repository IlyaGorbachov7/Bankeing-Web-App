<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="by.epam.baranovsky.banking.constant.DBMetadata" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale.locale"/>


<c:set var="pending_accs" scope="page" value="${PENDING_ACCS}"/>
<c:set var="totalCount" scope="page" value="${fn.length(pending_accs)}"/>
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
    <title><fmt:message key="pending.accounts.title"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css"/>
</head>
<body>
<div class="container">
    <div class="imgColumn">
        <h2><fmt:message key="accounts.pending.requests"/></h2>
        <a href="controller?command=go_to_pending_accounts&start=${pageStart - perPage}"><fmt:message key="home.operations.prev.page"/></a>${pageStart + 1} - ${pageStart + perPage}
        <a href="controller?command=go_to_pending_accounts&start=${pageStart + perPage}"><fmt:message key="home.operations.next.page"/></a>
        <br>
        <c:forEach var="account" items="${PENDING_ACCS}" begin="${pageStart}" end="${pageStart + perPage - 1}">
            <div class="operationbox">
                <form action="controller" method="post">
                    <input type="hidden" name="command" value="update_account">
                    <input type="hidden" name="account_new_status" value="${DBMetadata.ACCOUNT_STATUS_UNLOCKED}">
                    <input type="hidden" name="account_id" value="${account.id}">
                    <table class="blankTable">
                        <tr>
                            <td><b><fmt:message key="pending.accounts.request"/></b></td>
                        </tr>
                        <tr>
                            <td>
                                <b><fmt:message key="transfer.acc.num"/></b>
                            </td>
                            <td>
                                    ${account.accountNumber}
                            </td>
                            <td>
                                <b><fmt:message key="accounts.interest"/></b>
                            </td>
                            <td>
                                <input type="number" name="new_interest_rate" step=".01" value="${account.yearlyInterestRate}" min="-25" max="25">%
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <a href="controller?command=go_to_user_info&checked_user_id=${account.users[0]}">
                                    <fmt:message key="pending.accounts.user"/>
                                </a>
                            </td>
                            <td>
                                <input id="submit-button-pending" type="submit" value="<fmt:message key="pending.accounts.approve"/>">
                            </td>
                            <td>
                                <a href="<c:url value="controller?command=delete_pending_account&account_id=${account.id}"/>">
                                    <fmt:message key="pending.accounts.deny"/>
                                </a>
                            </td>
                        </tr>
                    </table>
                </form>
            </div>
        </c:forEach>
    </div>
    <div class="userColumn">
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
