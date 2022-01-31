<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="by.epam.baranovsky.banking.constant.DBMetadata" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale.locale"/>

<c:set var="operations" scope="page" value="${OPERATIONS_DATA}"/>
<c:set var="totalCount" scope="page" value="${fn.length(OPERATIONS_DATA)}"/>
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
    <title><fmt:message key="home.title" /></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css"/>
</head>
<body>
<div class="container">
    <div class="imgColumn">
        <c:if test="${USER_DATA == null}">
            <h1><fmt:message key="home.header"/></h1>
            <div class="operationbox">
                <fmt:message key="home.placeholder"/>
            </div>
        </c:if>
        <c:if test="${USER_DATA != null}">
            <h1><fmt:message key="user.info.operation.history"/></h1>
            <a href="controller?command=go_to_main_page&start=${pageStart - perPage}"><fmt:message key="home.operations.prev.page"/></a>${pageStart + 1} - ${pageStart + perPage}
            <a href="controller?command=go_to_main_page&start=${pageStart + perPage}"><fmt:message key="home.operations.next.page"/></a>
        </c:if>
        <c:forEach var="entry" items="${OPERATIONS_DATA}" begin="${pageStart}" end="${pageStart + perPage - 1}">
            <div class="operationbox">
                <table class="blankTable">
                    <c:choose>
                        <c:when test="${entry.typeId == DBMetadata.OPERATION_TYPE_ACC_LOCK}">
                            <tr>
                                <td><b><fmt:message key="operations.acc.lock"/>:</b></td>
                                <td>
                                    <fmt:message key="transfer.acc.num"/>
                                    ${entry.accountNumber}
                                    (<fmt:message key="operation.your"/>)
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
                                    (<fmt:message key="operation.your"/>)
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
                                    (<fmt:message key="operation.your"/>)
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
                                    (<fmt:message key="operation.your"/>)
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
                                    (<fmt:message key="operation.your"/>)
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
                                    (<fmt:message key="operation.your"/>)
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
                                    (<fmt:message key="operation.your"/>)
                                </td>
                                <td><b><fmt:message key="operations.date"/>:</b></td>
                                <td>${entry.date}</td>
                            </tr>
                            <tr>
                                <td></td>
                                <td><b><fmt:message key="operations.value"/>: </b></td>
                                <td>${entry.value}$</td>
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
                                         (<fmt:message key="operation.your"/>)
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
                                        (<fmt:message key="operation.your"/>)
                                    </c:if>
                                </td>
                            </tr>
                            <tr>
                                <td><b><fmt:message key="operations.date"/>:</b></td>
                                <td>${entry.date}</td>
                                <td><b><fmt:message key="operations.value"/>: </b></td>
                                <td>
                                    ${entry.value}
                                    <c:if test="${not entry.isAccrual}">
                                        + ${entry.commission}
                                    </c:if>$
                                </td>
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
                                        (<fmt:message key="operation.your"/>)
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
                                        (<fmt:message key="operation.your"/>)
                                    </c:if>
                                </td>
                            </tr>
                            <tr>
                                <td><b><fmt:message key="operations.date"/>:</b></td>
                                <td>${entry.date}</td>
                                <td><b><fmt:message key="operations.value"/>: </b></td>
                                <td>${entry.value}
                                    <c:if test="${not entry.isAccrual}">
                                        + ${entry.commission}
                                    </c:if>$
                                </td>
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
                                        (<fmt:message key="operation.your"/>)
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
                                        (<fmt:message key="operation.your"/>)
                                    </c:if>
                                </td>
                            </tr>
                            <tr>
                                <td><b><fmt:message key="operations.date"/>:</b></td>
                                <td>${entry.date}</td>
                                <td><b><fmt:message key="operations.value"/>: </b></td>
                                <td>${entry.value}
                                    <c:if test="${not entry.isAccrual}">
                                        + ${entry.commission}
                                    </c:if>$</td>
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
                                        (<fmt:message key="operation.your"/>)
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
                                        (<fmt:message key="operation.your"/>)
                                    </c:if>
                                </td>
                            </tr>
                            <tr>
                                <td><b><fmt:message key="operations.date"/>:</b></td>
                                <td>${entry.date}</td>
                                <td><b><fmt:message key="operations.value"/>: </b></td>
                                <td>${entry.value}
                                    <c:if test="${not entry.isAccrual}">
                                        + ${entry.commission}
                                    </c:if>$</td>
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

    </div>
    <div class="userColumn">
        <c:if test="${USER_DATA != null}">
            <div class="whitebox">
                <table class="blankTable">
                    <tr>
                        <td colspan="2"><h4><fmt:message key="home.welcome"/>, ${USER_DATA.firstName}</h4></td>
                    </tr>
                    <tr>
                        <td><b><fmt:message key="user.data"/></b></td>
                    </tr>
                    <tr>
                        <td><fmt:message key="user.lname"/></td>
                        <td><b>${USER_DATA.lastName}</b></td>
                    </tr>
                    <tr>
                        <td><fmt:message key="user.fname"/></td>
                        <td><b>${USER_DATA.firstName}</b></td>
                    </tr>
                    <c:if test="${USER_DATA.patronymic != null}">
                        <tr>
                            <td><fmt:message key="user.patronymic"/></td>
                            <td><b>${USER_DATA.patronymic}</b></td>
                        </tr>
                    </c:if>
                    <tr>
                        <td><fmt:message key="user.email"/></td>
                        <td><b>${USER_DATA.email}</b></td>
                    </tr>
                    <tr>
                        <td><fmt:message key="user.created"/></td>
                        <td><b>${USER_DATA.dateCreated}</b></td>
                    </tr>
                    <tr>
                        <td><fmt:message key="user.birthdate"/></td>
                        <td><b>${USER_DATA.birthDate}</b></td>
                    </tr>
                </table>
                <b>
                    <a href="controller?command=go_to_user_edit"><fmt:message key="edit.user.title"/> </a>
                </b>
                </div>
            <div class="whitebox">
                <a href="controller?command=go_to_accounts_page"><fmt:message key="accounts.title"/></a>
            </div>
            <div class="whitebox">
                <a href="controller?command=go_to_cards_page"><fmt:message key="cards.title"/></a>
            </div>
            <div class="whitebox">
                <a href="controller?command=go_to_bills_page"><fmt:message key="bills.title"/></a>
            </div>
            <div class="whitebox">
                <a href="controller?command=go_to_transfer_page"><fmt:message key="transfer.title"/></a>
            </div>
            <c:if test="${USER_ROLE_ID eq DBMetadata.USER_ROLE_ADMIN or USER_ROLE_ID eq DBMetadata.USER_ROLE_EMPLOYEE}">
                <div class="whitebox">
                    <a href="controller?command=go_to_all_users">
                        <fmt:message key="employee.start"/>
                    </a>
                </div>
            </c:if>
            <a href="controller?command=logout"><fmt:message key="home.logout.button"/></a>
            <br>
        </c:if>
        <c:if test="${USER_DATA == null}">
            <br>
            <div class="whitebox">
                <a href="controller?command=go_to_register_page"><fmt:message key="home.register.button"/></a>
                <br>
                <a href="controller?command=go_to_login_page"><fmt:message key="home.login.button"/></a>
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
