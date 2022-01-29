<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="by.epam.baranovsky.banking.constant.DBMetadata" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale.locale"/>

<c:set var="accs" scope="page" value="${USER_ACCOUNTS}"/>
<c:set var="accsCount" scope="page" value="${fn.length(accs)}"/>
<c:set var="accsStart" value="${param.accs_start}"/>
<c:if test="${empty accsStart or accsStart < 0}">
    <c:set var="accsStart" value="0"/>
</c:if>
<c:if test="${accsCount < accsStart}">
    <c:set var="accsStart" value="${accsStart - perPage}"/>
</c:if>

<c:set var="cards" scope="page" value="${USER_CARDS}"/>
<c:set var="cardsCount" scope="page" value="${fn.length(cards)}"/>
<c:set var="cardsStart" value="${param.cards_start}"/>
<c:if test="${empty cardsStart or cardsStart < 0}">
    <c:set var="cardsStart" value="0"/>
</c:if>
<c:if test="${cardsCount < cardsStart}">
    <c:set var="cardsStart" value="${cardsStart - perPage}"/>
</c:if>

<c:set var="operations" scope="page" value="${OPERATIONS_DATA}"/>
<c:set var="opersCount" scope="page" value="${fn.length(operations)}"/>
<c:set var="opersStart" value="${param.opers_start}"/>
<c:if test="${empty opersStart or opersStart < 0}">
    <c:set var="opersStart" value="0"/>
</c:if>
<c:if test="${opersCount < opersStart}">
    <c:set var="opersStart" value="${opersStart - perPage}"/>
</c:if>

<c:set var="bills" scope="page" value="${USER_BILLS}"/>
<c:set var="billsCount" scope="page" value="${fn.length(bills)}"/>
<c:set var="billsStart" value="${param.bills_start}"/>
<c:if test="${empty billsStart or billsStart < 0}">
    <c:set var="billsStart" value="0"/>
</c:if>
<c:if test="${billsCount < billsStart}">
    <c:set var="billsStart" value="${billsStart - perPage}"/>
</c:if>

<c:set var="loans" scope="page" value="${USER_LOANS}"/>
<c:set var="loansCount" scope="page" value="${fn.length(loans)}"/>
<c:set var="loansStart" value="${param.loans_start}"/>
<c:if test="${empty loansStart or loansStart < 0}">
    <c:set var="loansStart" value="0"/>
</c:if>
<c:if test="${loansCount < loansStart}">
    <c:set var="loansStart" value="${loansStart - perPage}"/>
</c:if>

<c:set var="penalties" scope="page" value="${USER_PENALTIES}"/>
<c:set var="penaltyCount" scope="page" value="${fn.length(penalties)}"/>
<c:set var="penaltiesStart" value="${param.penalties_start}"/>
<c:if test="${empty penaltiesStart or penaltiesStart < 0}">
    <c:set var="penaltiesStart" value="0"/>
</c:if>
<c:if test="${penaltyCount < penaltiesStart}">
    <c:set var="penaltiesStart" value="${penaltiesStart - perPage}"/>
</c:if>

<c:set var="perPage" scope="page"  value="${3}"/>
<c:set var="userId" scope="page" value="${param.checked_user_id}"/>


<html>
<head>
    <title><fmt:message key="user.info"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/info.css"/>
</head>
<body>
<div class="container">
    <div class="formContainer">
        <h3><fmt:message key="user.data"/>:</h3>
        <div class="whitebox">
            <table class="blankTable">
                <tr>
                    <td>
                        <b><fmt:message key="user.email"/></b>
                    </td>
                    <td colspan="2">
                        ${USER_DATA.email}
                    </td>
                </tr>
                <tr>
                    <td>
                        <b><fmt:message key="all.users.full.name"/></b>
                    </td>
                    <td colspan="3">
                        ${USER_DATA.firstName} ${USER_DATA.lastName} ${USER_DATA.patronymic}
                    </td>
                </tr>
                <tr>
                    <td><b><fmt:message key="user.role"/>:</b></td>
                    <td>
                        <c:choose>
                            <c:when test="${USER_DATA.roleId eq DBMetadata.USER_ROLE_BANNED}">
                                <fmt:message key="user.role.banned"/>
                            </c:when>
                            <c:when test="${USER_DATA.roleId eq DBMetadata.USER_ROLE_REGULAR}">
                                <fmt:message key="user.role.regular"/>
                            </c:when>
                            <c:when test="${USER_DATA.roleId eq DBMetadata.USER_ROLE_EMPLOYEE}">
                                <fmt:message key="user.role.employee"/>
                            </c:when>
                            <c:when test="${USER_DATA.roleId eq DBMetadata.USER_ROLE_ADMIN}">
                                <fmt:message key="user.role.admin"/>
                            </c:when>
                        </c:choose>
                    </td>
                </tr>
                <tr>
                    <td>
                        <b><fmt:message key="all.users.passport"/>:</b>
                    </td>
                    <td>${USER_DATA.passportSeries}${USER_DATA.passportNumber}</td>
                    <td>
                        <b><fmt:message key="user.birthdate"/>:</b>
                    </td>
                    <td>${USER_DATA.birthDate}</td>
                </tr>
                <tr>
                    <td>
                        <b><fmt:message key="all.users.last.login"/>:</b>
                    </td>
                    <td>${USER_DATA.lastLogin}</td>
                    <td>
                        <b><fmt:message key="user.created"/>:</b>
                    </td>
                    <td>${USER_DATA.dateCreated}</td>
                </tr>
                <tr>
                    <td colspan="3" style="padding-top: 2em">
                        <h4><fmt:message key="accounts.title"/>:</h4>
                    </td>
                    <td>
                        <a href="controller?command=go_to_user_info&checked_user_id=${userId}&accs_start=${accsStart - perPage}"><fmt:message key="home.operations.prev.page"/></a>${accsStart + 1} - ${accsStart + perPage}
                        <a href="controller?command=go_to_user_info&checked_user_id=${userId}&accs_start=${accsStart + perPage}"><fmt:message key="home.operations.next.page"/></a>
                    </td>
                </tr>
                <tr>
                    <td colspan="4">
                        <c:forEach var="account" items="${accs}" begin="${accsStart}" end="${accsStart + perPage - 1}">
                            <div class="infobox">
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
                                            <b><fmt:message key="accounts.status"/></b>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>${account.accountNumber}</td>
                                        <td>${account.balance}</td>
                                        <td>${account.yearlyInterestRate}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${account.statusId == DBMetadata.ACCOUNT_STATUS_BLOCKED}">
                                                    <fmt:message key="accounts.locked"/>
                                                </c:when>
                                                <c:when test="${account.statusId == DBMetadata.ACCOUNT_STATUS_SUSPENDED}">
                                                    <fmt:message key="accounts.suspended"/>
                                                </c:when>
                                                <c:when test="${account.statusId == DBMetadata.ACCOUNT_STATUS_UNLOCKED}">
                                                    <fmt:message key="accounts.unlocked"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <fmt:message key="error.unknown"/>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </c:forEach>
                    </td>
                </tr>
                <tr>
                    <td colspan="3">
                        <h4><fmt:message key="user.info.operation.history"/>:</h4>
                    </td>
                    <td>
                        <a href="controller?command=go_to_user_info&checked_user_id=${userId}&opers_start=${opersStart - perPage}"><fmt:message key="home.operations.prev.page"/></a>${opersStart + 1} - ${opersStart + perPage}
                        <a href="controller?command=go_to_user_info&checked_user_id=${userId}&opers_start=${opersStart + perPage}"><fmt:message key="home.operations.next.page"/></a>
                    </td>
                </tr>
                <tr>
                    <td colspan="4">
                        <c:forEach var="entry" items="${operations}" begin="${opersStart}" end="${opersStart + perPage - 1}">
                            <div class="infobox">
                                <table class="blankTable">
                                    <c:choose>
                                        <c:when test="${entry.typeId == DBMetadata.OPERATION_TYPE_ACC_LOCK}">
                                            <tr>
                                                <td><b><fmt:message key="operations.acc.lock"/>:</b></td>
                                                <td>
                                                    <fmt:message key="transfer.acc.num"/>
                                                        ${entry.accountNumber}
                                                    (<fmt:message key="user.info.belongs.to.user"/>)
                                                </td>
                                                <td><b><fmt:message key="operations.date"/>:</b></td>
                                                <td>${entry.date}</td>
                                            </tr>
                                            <tr>
                                                <c:if test="${entry.bill != 0}">
                                                    <td><b><fmt:message key="operations.bill"/>:</b></td>
                                                    <td>${entry.bill}</td>
                                                </c:if>
                                                <c:if test="${entry.penalty != 0}">
                                                    <td><b><fmt:message key="operations.penalty"/>:</b></td>
                                                    <td>${entry.penalty}</td>
                                                </c:if>
                                            </tr>
                                        </c:when>
                                        <c:when test="${entry.typeId == DBMetadata.OPERATION_TYPE_ACC_SUSP}">
                                            <tr>
                                                <td><b><fmt:message key="operations.acc.suspension"/>:</b></td>
                                                <td>
                                                    <fmt:message key="transfer.acc.num"/>
                                                        ${entry.accountNumber}
                                                    (<fmt:message key="user.info.belongs.to.user"/>)
                                                </td>
                                                <td><b><fmt:message key="operations.date"/>:</b></td>
                                                <td>${entry.date}</td>
                                            </tr>
                                            <tr>
                                                <c:if test="${entry.bill != 0}">
                                                    <td><b><fmt:message key="operations.bill"/>:</b></td>
                                                    <td>${entry.bill}</td>
                                                </c:if>
                                                <c:if test="${entry.penalty != 0}">
                                                    <td><b><fmt:message key="operations.penalty"/>:</b></td>
                                                    <td>${entry.penalty}</td>
                                                </c:if>
                                            </tr>
                                        </c:when>
                                        <c:when test="${entry.typeId == DBMetadata.OPERATION_TYPE_ACC_UNLOCK}">
                                            <tr>
                                                <td><b><fmt:message key="operations.acc.unlock"/>:</b></td>
                                                <td>
                                                    <fmt:message key="transfer.acc.num"/>
                                                        ${entry.accountNumber}
                                                    (<fmt:message key="user.info.belongs.to.user"/>)
                                                </td>
                                                <td><b><fmt:message key="operations.date"/>:</b></td>
                                                <td>${entry.date}</td>
                                            </tr>
                                        </c:when>
                                        <c:when test="${entry.typeId == DBMetadata.OPERATION_TYPE_CARD_LOCK}">
                                            <tr>
                                                <td><b><fmt:message key="operations.card.lock"/>:</b></td>
                                                <td>
                                                    <fmt:message key="transfer.card.num"/>
                                                        ${entry.cardNumber}
                                                    (<fmt:message key="user.info.belongs.to.user"/>)
                                                </td>
                                                <td><b><fmt:message key="operations.date"/>:</b></td>
                                                <td>${entry.date}</td>
                                            </tr>
                                            <tr>
                                                <c:if test="${entry.bill != 0}">
                                                    <td><b><fmt:message key="operations.bill"/>:</b></td>
                                                    <td>${entry.bill}</td>
                                                </c:if>
                                                <c:if test="${entry.penalty != 0}">
                                                    <td><b><fmt:message key="operations.penalty"/>:</b></td>
                                                    <td>${entry.penalty}</td>
                                                </c:if>
                                            </tr>
                                        </c:when>
                                        <c:when test="${entry.typeId == DBMetadata.OPERATION_TYPE_CARD_UNLOCK}">
                                            <tr>
                                                <td><b><fmt:message key="operations.card.unlock"/>:</b></td>
                                                <td>
                                                    <fmt:message key="transfer.card.num"/>
                                                        ${entry.cardNumber}
                                                    (<fmt:message key="user.info.belongs.to.user"/>)
                                                </td>
                                                <td><b><fmt:message key="operations.date"/>:</b></td>
                                                <td>${entry.date}</td>
                                            </tr>
                                        </c:when>
                                        <c:when test="${entry.typeId == DBMetadata.OPERATION_TYPE_CARD_EXPIRE}">
                                            <tr>
                                                <td><b><fmt:message key="operations.card.expire"/>:</b></td>
                                                <td>
                                                    <fmt:message key="transfer.card.num"/>
                                                        ${entry.cardNumber}
                                                    (<fmt:message key="user.info.belongs.to.user"/>)
                                                </td>
                                                <td><b><fmt:message key="operations.date"/>:</b></td>
                                                <td>${entry.date}</td>
                                            </tr>
                                        </c:when>
                                        <c:when test="${entry.typeId == DBMetadata.OPERATION_TYPE_ACCRUAL}">
                                            <tr>
                                                <td><b><fmt:message key="operations.interest.accrual"/>:</b></td>
                                                <td>
                                                    <fmt:message key="transfer.acc.num"/>
                                                        ${entry.accountNumber}
                                                    (<fmt:message key="user.info.belongs.to.user"/>)
                                                </td>
                                                <td><b><fmt:message key="operations.date"/>:</b></td>
                                                <td>${entry.date}</td>
                                            </tr>
                                            <tr>
                                                <td></td>
                                                <td><b><fmt:message key="operations.value"/>: </b></td>
                                                <td>${entry.value}</td>
                                            </tr>
                                        </c:when>
                                        <c:when test="${entry.typeId == DBMetadata.OPERATION_TYPE_TRANSFER_A_A}">
                                            <tr>
                                                <td><b><fmt:message key="operations.transfer"/>:</b></td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <b><fmt:message key="transfer.from"/>:</b>
                                                </td>
                                                <td colspan="3">
                                                    <fmt:message key="transfer.acc.num"/>
                                                        ${entry.accountNumber}
                                                    <c:if test="${not entry.isAccrual}">
                                                        (<fmt:message key="user.info.belongs.to.user"/>)
                                                    </c:if>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <b><fmt:message key="transfer.to"/>:</b>
                                                </td>
                                                <td colspan="3">
                                                    <fmt:message key="transfer.acc.num"/>
                                                        ${entry.targetAccountNumber}
                                                    <c:if test="${entry.isAccrual}">
                                                        (<fmt:message key="user.info.belongs.to.user"/>)
                                                    </c:if>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td><b><fmt:message key="operations.date"/>:</b></td>
                                                <td>${entry.date}</td>
                                                <td><b><fmt:message key="operations.value"/>: </b></td>
                                                <td>${entry.value}</td>
                                            </tr>
                                            <tr>
                                                <c:if test="${entry.bill != 0}">
                                                    <td><b><fmt:message key="operations.bill"/>:</b></td>
                                                    <td>${entry.bill}</td>
                                                </c:if>
                                                <c:if test="${entry.penalty != 0}">
                                                    <td><b><fmt:message key="operations.penalty"/>:</b></td>
                                                    <td>${entry.penalty}</td>
                                                </c:if>
                                            </tr>
                                        </c:when>
                                        <c:when test="${entry.typeId == DBMetadata.OPERATION_TYPE_TRANSFER_A_C}">
                                            <tr>
                                                <td><b><fmt:message key="operations.transfer"/>:</b></td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <b><fmt:message key="transfer.from"/>:</b>
                                                </td>
                                                <td colspan="3">
                                                    <fmt:message key="transfer.acc.num"/>
                                                        ${entry.accountNumber}
                                                    <c:if test="${not entry.isAccrual}">
                                                        (<fmt:message key="user.info.belongs.to.user"/>)
                                                    </c:if>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <b><fmt:message key="transfer.to"/>:</b>
                                                </td>
                                                <td colspan="3">
                                                    <fmt:message key="transfer.card.num"/>
                                                        ${entry.targetCardNumber}
                                                    <c:if test="${entry.isAccrual}">
                                                        (<fmt:message key="user.info.belongs.to.user"/>)
                                                    </c:if>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td><b><fmt:message key="operations.date"/>:</b></td>
                                                <td>${entry.date}</td>
                                                <td><b><fmt:message key="operations.value"/>: </b></td>
                                                <td>${entry.value}</td>
                                            </tr>
                                            <tr>
                                                <c:if test="${entry.bill != 0}">
                                                    <td><b><fmt:message key="operations.bill"/>:</b></td>
                                                    <td>${entry.bill}</td>
                                                </c:if>
                                                <c:if test="${entry.penalty != 0}">
                                                    <td><b><fmt:message key="operations.penalty"/>:</b></td>
                                                    <td>${entry.penalty}</td>
                                                </c:if>
                                            </tr>
                                        </c:when>
                                        <c:when test="${entry.typeId == DBMetadata.OPERATION_TYPE_TRANSFER_C_A}">
                                            <tr>
                                                <td><b><fmt:message key="operations.transfer"/>:</b></td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <b><fmt:message key="transfer.from"/>:</b>
                                                </td>
                                                <td colspan="3">
                                                    <fmt:message key="transfer.card.num"/>
                                                        ${entry.cardNumber}
                                                    <c:if test="${not entry.isAccrual}">
                                                        (<fmt:message key="user.info.belongs.to.user"/>)
                                                    </c:if>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <b><fmt:message key="transfer.to"/>:</b>
                                                </td>
                                                <td colspan="3">
                                                    <fmt:message key="transfer.acc.num"/>
                                                        ${entry.targetAccountNumber}
                                                    <c:if test="${entry.isAccrual}">
                                                        (<fmt:message key="user.info.belongs.to.user"/>)
                                                    </c:if>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td><b><fmt:message key="operations.date"/>:</b></td>
                                                <td>${entry.date}</td>
                                                <td><b><fmt:message key="operations.value"/>: </b></td>
                                                <td>${entry.value}</td>
                                            </tr>
                                            <tr>
                                                <c:if test="${entry.bill != 0}">
                                                    <td><b><fmt:message key="operations.bill"/>:</b></td>
                                                    <td>${entry.bill}</td>
                                                </c:if>
                                                <c:if test="${entry.penalty != 0}">
                                                    <td><b><fmt:message key="operations.penalty"/>:</b></td>
                                                    <td>${entry.penalty}</td>
                                                </c:if>
                                            </tr>
                                        </c:when>
                                        <c:when test="${entry.typeId == DBMetadata.OPERATION_TYPE_TRANSFER_C_C}">
                                            <tr>
                                                <td><b><fmt:message key="operations.transfer"/>:</b></td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <b><fmt:message key="transfer.from"/>:</b>
                                                </td>
                                                <td colspan="3">
                                                    <fmt:message key="transfer.card.num"/>
                                                        ${entry.cardNumber}
                                                    <c:if test="${not entry.isAccrual}">
                                                        (<fmt:message key="user.info.belongs.to.user"/>)
                                                    </c:if>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <b><fmt:message key="transfer.to"/>:</b>
                                                </td>
                                                <td colspan="3">
                                                    <fmt:message key="transfer.card.num"/>
                                                        ${entry.targetCardNumber}
                                                    <c:if test="${entry.isAccrual}">
                                                        (<fmt:message key="user.info.belongs.to.user"/>)
                                                    </c:if>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td><b><fmt:message key="operations.date"/>:</b></td>
                                                <td>${entry.date}</td>
                                                <td><b><fmt:message key="operations.value"/>: </b></td>
                                                <td>${entry.value}</td>
                                            </tr>
                                            <tr>
                                                <c:if test="${entry.bill != 0}">
                                                    <td><b><fmt:message key="operations.bill"/>:</b></td>
                                                    <td>${entry.bill}</td>
                                                </c:if>
                                                <c:if test="${entry.penalty != 0}">
                                                    <td><b><fmt:message key="operations.penalty"/>:</b></td>
                                                    <td>${entry.penalty}</td>
                                                </c:if>
                                            </tr>
                                        </c:when>
                                    </c:choose>
                                </table>
                            </div>
                        </c:forEach>
                    </td>
                </tr>
                <tr>
                    <td colspan="3">
                        <h4><fmt:message key="user.info.loan.history"/>:</h4>
                    </td>
                    <td>
                        <a href="controller?command=go_to_user_info&checked_user_id=${userId}&loans_start=${loansStart - perPage}"><fmt:message key="home.operations.prev.page"/></a>${loansStart + 1} - ${loansStart + perPage}
                        <a href="controller?command=go_to_user_info&checked_user_id=${userId}&loans_start=${loansStart + perPage}"><fmt:message key="home.operations.next.page"/></a>
                    </td>
                </tr>
                <tr>
                    <td colspan="4">
                        <c:forEach var="loan" items="${loans}" begin="${loansStart}" end="${loansStart + perPage - 1}">
                            <div class="infobox">
                                <table class="blankTable">
                                    <tr>
                                        <td><b><fmt:message key="loans.start"/></b></td>
                                        <td><b><fmt:message key="loans.total"/></b></td>
                                        <td><b<fmt:message key="loans.single"/>></b></td>
                                        <td><b><fmt:message key="accounts.interest"/></b></td>
                                    </tr>
                                    <tr>
                                        <td>${loan.startingValue}$</td>
                                        <td>${loan.totalPaymentValue}$</td>
                                        <td>${loan.singlePaymentValue}$</td>
                                        <td>${loan.yearlyInterestRate}$</td>
                                    </tr>
                                    <tr>
                                        <td><b><fmt:message key="loans.issue"/></b></td>
                                        <td><b><fmt:message key="loans.due"/></b></td>
                                        <td><b><fmt:message key="accounts.status"/></b></td>
                                    </tr>
                                    <tr>
                                        <td>${loan.issueDate}</td>
                                        <td>${loan.dueDate}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${loan.statusId == DBMetadata.LOAN_STATUS_PENDING}">
                                                    <fmt:message key="loans.status.pending"/>
                                                </c:when>
                                                <c:when test="${loan.statusId == DBMetadata.LOAN_STATUS_OVERDUE}">
                                                    <fmt:message key="loans.status.overdue"/>
                                                </c:when>
                                                <c:when test="${loan.statusId == DBMetadata.LOAN_STATUS_CLOSED}">
                                                    <fmt:message key="loans.status.closed"/>
                                                </c:when>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </c:forEach>
                    </td>
                </tr>
                <tr>
                    <td colspan="3">
                        <h4><fmt:message key="user.info.bill.history"/>:</h4>
                    </td>
                    <td>
                        <a href="controller?command=go_to_user_info&checked_user_id=${userId}&bills_start=${billsStart - perPage}"><fmt:message key="home.operations.prev.page"/></a>${billsStart + 1} - ${billsStart + perPage}
                        <a href="controller?command=go_to_user_info&checked_user_id=${userId}&bills_start=${billsStart + perPage}"><fmt:message key="home.operations.next.page"/></a>
                    </td>
                </tr>
                <tr>
                    <td colspan="4">
                        <c:forEach var="bill" items="${bills}" begin="${billsStart}" end="${billsStart + perPage - 1}">
                            <div class="infobox">
                                <table class="blankTable">
                                    <tr>
                                        <td><b><fmt:message key="operations.value"/></b></td>
                                        <td><b><fmt:message key="loans.issue"/></b></td>
                                        <td><b><fmt:message key="loans.due"/></b></td>
                                        <td><b><fmt:message key="accounts.status"/></b></td>
                                    </tr>
                                    <tr>
                                        <td>${bill.value}</td>
                                        <td>${bill.issueDate}</td>
                                        <td>${bill.dueDate}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${bill.statusId == DBMetadata.BILL_STATUS_CLOSED}">
                                                    <fmt:message key="loans.status.closed"/>
                                                </c:when>
                                                <c:when test="${bill.statusId == DBMetadata.BILL_STATUS_PENDING}">
                                                    <fmt:message key="loans.status.pending"/>
                                                </c:when>
                                                <c:when test="${bill.statusId == DBMetadata.BILL_STATUS_OVERDUE}">
                                                    <fmt:message key="loans.status.overdue"/>
                                                </c:when>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </c:forEach>
                    </td>
                </tr>
                <tr>
                    <td colspan="3">
                        <h4><fmt:message key="user.info.penalty.history"/>:</h4>
                    </td>
                    <td>
                        <a href="controller?command=go_to_user_info&checked_user_id=${userId}&penalties_start=${penaltiesStart - perPage}"><fmt:message key="home.operations.prev.page"/></a>${penaltiesStart + 1} - ${penaltiesStart + perPage}
                        <a href="controller?command=go_to_user_info&checked_user_id=${userId}&penalties_start=${penaltiesStart + perPage}"><fmt:message key="home.operations.next.page"/></a>
                    </td>
                </tr>
                <tr>
                    <td colspan="4">
                        <c:forEach var="penalty" items="${penalties}" begin="${penaltiesStart}" end="${penaltiesStart + perPage - 1}" >
                            <div class="infobox">
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
                                </table>
                            </div>
                        </c:forEach>
                    </td>
                </tr>
                <tr>
                    <td colspan="3">
                        <h4><fmt:message key="cards.title"/>:</h4>
                    </td>
                    <td>
                        <a href="controller?command=go_to_user_info&checked_user_id=${userId}&cards_start=${cardsStart - perPage}"><fmt:message key="home.operations.prev.page"/></a>${cardsStart + 1} - ${cardsStart + perPage}
                        <a href="controller?command=go_to_user_info&checked_user_id=${userId}&cards_start=${cardsStart + perPage}"><fmt:message key="home.operations.next.page"/></a>
                    </td>
                </tr>

                <tr>
                    <td colspan="4">
                        <c:forEach var="card" items="${cards}" begin="${cardsStart}" end="${cardsStart+perPage-1}">
                            <div class="infobox">
                                <table class="blankTable">
                                    <c:if test="${card.getKey().cardTypeId == DBMetadata.CARD_TYPE_DEBIT}">
                                        <tr>
                                            <td><fmt:message key="card.number"/></td>
                                            <td><fmt:message key="card.expiration"/></td>
                                            <td><fmt:message key="card.type"/></td>
                                        </tr>
                                        <tr>
                                            <td>${card.getKey().number}</td>
                                            <td>${card.getKey().expirationDate}</td>
                                            <td><fmt:message key="card.type.debit"/></td>
                                        </tr>
                                    </c:if>
                                    <c:if test="${card.getKey().cardTypeId == DBMetadata.CARD_TYPE_CREDIT}">
                                        <tr>
                                            <td><fmt:message key="card.number"/></td>
                                            <td><fmt:message key="card.expiration"/></td>
                                            <td><fmt:message key="card.balance"/></td>
                                            <td><fmt:message key="card.type"/></td>
                                        </tr>
                                        <tr>
                                            <td>${card.getKey().number}</td>
                                            <td>${card.getKey().expirationDate}</td>
                                            <td>${card.getKey().balance}$</td>
                                            <td><fmt:message key="card.type.credit"/></td>
                                        </tr>
                                    </c:if>
                                    <c:if test="${card.getKey().cardTypeId == DBMetadata.CARD_TYPE_OVERDRAFT}">
                                        <tr>
                                            <td><fmt:message key="card.number"/></td>
                                            <td><fmt:message key="card.expiration"/></td>
                                            <td><fmt:message key="card.overdraft.limit"/></td>
                                            <td><fmt:message key="card.type"/></td>
                                        </tr>
                                        <tr>
                                            <td>${card.getKey().number}</td>
                                            <td>${card.getKey().expirationDate}</td>
                                            <td>${card.getKey().overdraftMax}$</td>
                                            <td><fmt:message key="card.type.overdraft"/></td>
                                        </tr>
                                    </c:if>
                                </table>
                            </div>
                        </c:forEach>
                    </td>
                </tr>
            </table>
        </div>
        <br>
        <a href="${PREV_PAGE}">
            <fmt:message key="card.info.back"/>
        </a>
    </div>
</div>

</body>
</html>
