<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="by.epam.baranovsky.banking.constant.DBMetadata" %>
<%@ page import="by.epam.baranovsky.banking.constant.Message" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale.locale"/>

<html>
<head>
    <title><fmt:message key="accounts.title" /></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css"/>
</head>
<body>
<div class="container">
    <div class="imgColumn">
        <h1 class="operationbox"><fmt:message key="accounts.title"/></h1>
        <h2><fmt:message key="accounts.unlocked"/></h2>
        <c:forEach var="workingAccount" items="${UNLOCKED_ACCS}">
            <div class="operationbox">
                <table class="blankTable">
                   <tr>
                       <td>
                           <b><fmt:message key="accounts.number"/></b>
                       </td>
                       <td>
                           <b><fmt:message key="accounts.balance"/></b>
                       </td>
                       <td>
                           <b><fmt:message key="accounts.interest"/></b>
                       </td>
                       <td>
                           <a href="controller?command=lock_or_suspend_account&account_id=${workingAccount.id}&account_new_status=${DBMetadata.ACCOUNT_STATUS_SUSPENDED}">
                               <i><fmt:message key="accounts.suspend.account"/></i>
                           </a>
                       </td>
                   </tr>
                    <tr>
                        <td>${workingAccount.accountNumber}</td>
                        <td>${workingAccount.balance}$</td>
                        <td>${workingAccount.yearlyInterestRate}%</td>
                        <td>
                            <a href="controller?command=lock_or_suspend_account&account_id=${workingAccount.id}&account_new_status=${DBMetadata.ACCOUNT_STATUS_BLOCKED}">
                               <i><fmt:message key="accounts.lock.account"/></i>
                            </a>
                        </td>
                    </tr>
                    <br>
                    <tr>
                        <td colspan="3"></td>
                        <td>
                            <a href="controller?command=go_to_account_info&account_id=${workingAccount.id}">
                                <fmt:message key="accounts.manage"/>
                            </a>
                        </td>
                    </tr>
                </table>
            </div>
        </c:forEach>
        <c:if test="${not empty SUSPENDED_ACCS}">
            <h2><fmt:message key="accounts.suspended"/></h2>
            <c:forEach var="suspendedAccount" items="${SUSPENDED_ACCS}">
                <div class="operationbox">
                    <table class="blankTable">
                        <tr>
                            <td>
                                <b><fmt:message key="accounts.number"/></b>
                            </td>
                            <td>
                                <b><fmt:message key="accounts.balance"/></b>
                            </td>
                            <td>
                                <b><fmt:message key="accounts.interest"/></b>
                            </td>
                            <td>
                                <a href="controller?command=lock_or_suspend_account&account_id=${suspendedAccount.id}&account_new_status=${DBMetadata.ACCOUNT_STATUS_BLOCKED}">
                                    <i><fmt:message key="accounts.lock.account"/></i>
                                </a>
                            </td>
                        </tr>
                        <tr>
                            <td>${suspendedAccount.accountNumber}</td>
                            <td>${suspendedAccount.balance}$</td>
                            <td>${suspendedAccount.yearlyInterestRate}%</td>
                        </tr>
                        <br>
                        <tr>
                            <td colspan="3"></td>
                            <td>
                                <a href="controller?command=go_to_account_info&account_id=${suspendedAccount.id}">
                                    <fmt:message key="accounts.manage"/>
                                </a>
                            </td>
                        </tr>
                    </table>
                </div>
            </c:forEach>
        </c:if>
        <c:if test="${not empty BLOCKED_ACCS}">
            <h2><fmt:message key="accounts.locked"/></h2>
            <c:forEach var="blockedAccount" items="${BLOCKED_ACCS}">
                <div class="operationbox">
                    <table class="blankTable">
                        <tr>
                            <td>
                                <b><fmt:message key="accounts.number"/></b>
                            </td>
                            <td>
                                <b><fmt:message key="accounts.balance"/></b>
                            </td>
                            <td>
                                <b><fmt:message key="accounts.interest"/></b>
                            </td>
                        </tr>
                        <tr>
                            <td>${blockedAccount.accountNumber}</td>
                            <td>${blockedAccount.balance}$</td>
                            <td>${blockedAccount.yearlyInterestRate}%</td>
                        </tr>
                        <br>
                        <tr>
                            <td colspan="3"></td>
                            <td>
                                <a href="controller?command=go_to_account_info&account_id=${blockedAccount.id}">
                                    <fmt:message key="accounts.manage"/>
                                </a>
                            </td>
                        </tr>
                    </table>
                </div>
            </c:forEach>
        </c:if>
    </div>

    <div class="userColumn">
        <div class="whitebox">
            <fmt:message key="accounts.pending.requests"/>: ${PENDING_ACCS_COUNT}
        </div>
        <div class="whitebox">
            <a href="controller?command=new_account">
                <fmt:message key="accounts.create"/>
            </a>
        </div>

        <div class="whitebox">
            <a href="controller?command=go_to_cards_page">
                <fmt:message key="cards.title"/>
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
                        <c:when test="${ERROR_MSG eq Message.ACCOUNT_INFO_ERROR}">
                            <fmt:message key="accounts.error.info"/>
                        </c:when>
                        <c:when test="${ERROR_MSG eq Message.NOT_YOUR_ACCOUNT}">
                            <fmt:message key="accounts.error.alien"/>
                        </c:when>
                        <c:when test="${ERROR_MSG eq Message.TOO_MANY_CREATION_REQUESTS}">
                            <fmt:message key="accounts.error.many.requests"/>
                        </c:when>
                        <c:when test="${ERROR_MSG eq Message.WRONG_NEW_STATUS}">
                            <fmt:message key="accounts.error.wrong.new.status"/>
                        </c:when>
                        <c:otherwise>
                            <fmt:message key="error.unknown"/>
                        </c:otherwise>
                    </c:choose>
                </i>
            </div>
        </c:if>
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
