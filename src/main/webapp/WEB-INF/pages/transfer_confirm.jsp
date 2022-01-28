<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="by.epam.baranovsky.banking.constant.DBMetadata" %>
<%@ page import="by.epam.baranovsky.banking.constant.Message" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale.locale"/>
<html>
<head>
    <title><fmt:message key="transfer.confirm.title" /></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/info.css"/>
</head>
<body>
<div class="container">
    <div class="formContainer">
        <h3><fmt:message key="transfer.confirm.title"/></h3>

        <div class="whitebox">
            <table class="blankTable">
                <tr>
                    <td>
                        <b><fmt:message key="transfer.from"/>:</b>
                    </td>
                </tr>
                <c:if test="${not empty OWN_ACC}">
                    <tr>
                        <td>
                            <b><i><fmt:message key="transfer.acc"/></i></b>
                        </td>
                    </tr>
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
                        <td>${OWN_ACC.accountNumber}</td>
                        <td>${OWN_ACC.balance}$</td>
                        <td>${OWN_ACC.yearlyInterestRate}%</td>
                        <td>
                            <c:choose>
                                <c:when test="${OWN_ACC.statusId == DBMetadata.ACCOUNT_STATUS_BLOCKED}">
                                    <fmt:message key="accounts.locked"/>
                                </c:when>
                                <c:when test="${OWN_ACC.statusId == DBMetadata.ACCOUNT_STATUS_SUSPENDED}">
                                    <fmt:message key="accounts.suspended"/>
                                </c:when>
                                <c:when test="${OWN_ACC.statusId == DBMetadata.ACCOUNT_STATUS_UNLOCKED}">
                                    <fmt:message key="accounts.unlocked"/>
                                </c:when>
                                <c:otherwise>
                                    <fmt:message key="error.unknown"/>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:if>
                <c:if test="${not empty OWN_CARD}">
                    <tr>
                        <td>
                            <b><i><fmt:message key="transfer.card"/></i></b>
                        </td>
                    </tr>
                    <tr>
                        <td><b><fmt:message key="card.number"/></b></td>
                        <td><b><fmt:message key="card.expiration"/></b></td>
                        <td><b><fmt:message key="card.type"/></b></td>
                        <c:if test="${not empty OWN_CARD.balance}">
                            <td>
                                <b><fmt:message key="card.balance"/>$</b>
                            </td>
                        </c:if>
                    </tr>
                    <tr>
                        <td>${OWN_CARD.number}</td>
                        <td>${OWN_CARD.expirationDate}</td>
                        <td>
                            <c:choose>
                                <c:when test="${OWN_CARD.cardTypeId == DBMetadata.CARD_TYPE_DEBIT}">
                                    <fmt:message key="card.type.debit"/>
                                </c:when>
                                <c:when test="${OWN_CARD.cardTypeId == DBMetadata.CARD_TYPE_OVERDRAFT}">
                                    <fmt:message key="card.type.overdraft"/>
                                </c:when>
                                <c:when test="${OWN_CARD.cardTypeId == DBMetadata.CARD_TYPE_CREDIT}">
                                    <fmt:message key="card.type.credit"/>
                                </c:when>
                            </c:choose>
                        </td>
                        <c:if test="${not empty OWN_CARD.balance}">
                            <td>${OWN_CARD.balance}$</td>
                        </c:if>
                    </tr>
                </c:if>
                <tr >
                    <td></td>
                    <td style="padding-top: 2em; padding-bottom: 2em">
                        <b><fmt:message key="operations.value"/>:</b>
                    </td>
                    <td style="padding-top: 2em; padding-bottom: 2em">
                       ${TRANSFER_VALUE}
                    </td>
                </tr>
                <tr>
                    <td>
                        <b><fmt:message key="transfer.target"/></b>
                    </td>
                </tr>
                <c:if test="${not empty TARGET_ACC}">
                    <tr>
                        <td>
                            <b><i><fmt:message key="transfer.acc"/></i></b>
                        </td>
                    </tr>
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
                        <td>${TARGET_ACC.accountNumber}</td>
                        <td>${TARGET_ACC.balance}$</td>
                        <td>${TARGET_ACC.yearlyInterestRate}%</td>
                        <td>
                            <c:choose>
                                <c:when test="${TARGET_ACC.statusId == DBMetadata.ACCOUNT_STATUS_BLOCKED}">
                                    <fmt:message key="accounts.locked"/>
                                </c:when>
                                <c:when test="${TARGET_ACC.statusId == DBMetadata.ACCOUNT_STATUS_SUSPENDED}">
                                    <fmt:message key="accounts.suspended"/>
                                </c:when>
                                <c:when test="${TARGET_ACC.statusId ==DBMetadata.ACCOUNT_STATUS_UNLOCKED}">
                                    <fmt:message key="accounts.unlocked"/>
                                </c:when>
                                <c:otherwise>
                                    <fmt:message key="error.unknown"/>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:if>
                <c:if test="${not empty TARGET_CARD}">
                    <tr>
                        <td>
                            <b><i><fmt:message key="transfer.card"/></i></b>
                        </td>
                    </tr>
                    <tr>
                        <td><b><fmt:message key="card.number"/></b></td>
                        <td><b><fmt:message key="card.expiration"/></b></td>
                        <td><b><fmt:message key="card.type"/></b></td>

                    </tr>
                    <tr>
                        <td>${TARGET_CARD.number}</td>
                        <td>${TARGET_CARD.expirationDate}</td>
                        <td>
                            <c:choose>
                                <c:when test="${TARGET_CARD.cardTypeId == DBMetadata.CARD_TYPE_DEBIT}">
                                    <fmt:message key="card.type.debit"/>
                                </c:when>
                                <c:when test="${TARGET_CARD.cardTypeId == DBMetadata.CARD_TYPE_OVERDRAFT}">
                                    <fmt:message key="card.type.overdraft"/>
                                </c:when>
                                <c:when test="${TARGET_CARD.cardTypeId == DBMetadata.CARD_TYPE_CREDIT}">
                                    <fmt:message key="card.type.credit"/>
                                </c:when>
                            </c:choose>
                        </td>
                    </tr>
                </c:if>
            </table>
            <c:if test="${not empty BILL_ID}">
                <br>
                <b><fmt:message key="operations.bill"/>: </b>${BILL_ID}
            </c:if>
            <c:if test="${not empty PENALTY_ID}">
                <br>
                <b><fmt:message key="operations.penalty"/>: </b>${PENALTY_ID}
            </c:if>
            <br>
            <form action="controller" method="post">
                <input type="hidden" name="command" value="transfer"/>
                <input type="hidden" name="account_id" value="${OWN_ACC.id}">
                <input type="hidden" name="card_id" value="${OWN_CARD.id}">
                <input type="hidden" name="target_account_id" value="${TARGET_ACC.id}">
                <input type="hidden" name="target_card_id" value="${TARGET_CARD.id}">
                <input type="hidden" name="penalty_id" value="${PENALTY_ID}">
                <input type="hidden" name="bill_id" value="${BILL_ID}">
                <input type="hidden" name="transfer_value" value="${TRANSFER_VALUE}">
                <input type="submit" value="<fmt:message key="logination.submit"/>">
            </form>
            <c:if test="${ not empty ERROR_MSG}">
                <br>
                ${ERROR_MSG}
            </c:if>
        </div>
        <br>
        <a href="${PREV_PAGE}">
            <fmt:message key="card.info.back"/>
        </a>
    </div>
</div>


</body>
</html>
