<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="by.epam.baranovsky.banking.constant.DBMetadata" %>
<%@ page import="by.epam.baranovsky.banking.constant.Message" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale.locale"/>

<html>
<head>
    <title><fmt:message key="cards.title" /></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css"/>
</head>
<body>
<div class="container">
    <div class="imgColumn">
        <c:forEach var="card" items="${USER_CARDS}">
            <div class="operationbox">
                <table class="blankTable">
                    <c:if test="${card.cardTypeId == DBMetadata.CARD_TYPE_DEBIT}">
                        <tr>
                            <td><b><fmt:message key="card.number"/></b></td>
                            <td><b><fmt:message key="card.expiration"/></b></td>
                            <td><b><fmt:message key="card.type"/></b></td>
                        </tr>
                        <tr>
                            <td>${card.number}</td>
                            <td>${card.expirationDate}</td>
                            <td><fmt:message key="card.type.debit"/></td>
                        </tr>
                    </c:if>
                    <c:if test="${card.cardTypeId == DBMetadata.CARD_TYPE_CREDIT}">
                        <tr>
                            <td><b><fmt:message key="card.number"/></b></td>
                            <td><b><fmt:message key="card.expiration"/></b></td>
                            <td><b><fmt:message key="card.balance"/></b></td>
                            <td><b><fmt:message key="card.type"/></b></td>
                        </tr>
                        <tr>
                            <td>${card.number}</td>
                            <td>${card.expirationDate}</td>
                            <td>${card.balance}$</td>
                            <td><fmt:message key="card.type.credit"/></td>
                        </tr>
                    </c:if>
                    <c:if test="${card.cardTypeId == DBMetadata.CARD_TYPE_OVERDRAFT}">
                        <tr>
                            <td><b><fmt:message key="card.number"/></b></td>
                            <td><b><fmt:message key="card.expiration"/></b></td>
                            <td><b><fmt:message key="card.overdraft.limit"/></b></td>
                            <td><b><fmt:message key="card.type"/></b></td>
                        </tr>
                        <tr>
                            <td>${card.number}</td>
                            <td>${card.expirationDate}</td>
                            <td>${card.overdraftMax}$</td>
                            <td><fmt:message key="card.type.overdraft"/></td>
                        </tr>
                    </c:if>
                    <tr>
                        <td style="padding-top: 2em">
                            <a href="controller?command=lock_card&card_id=${card.id}">
                                <fmt:message key="card.lock"/>
                            </a>
                        </td>
                        <td style="padding-top: 2em">
                            <a href="controller?command=go_to_card_info&card_id=${card.id}">
                                <fmt:message key="card.see.info"/>
                            </a>
                        </td>
                    </tr>
                </table>
            </div>
        </c:forEach>
    </div>
    <div class="userColumn">
        <c:if test="${not empty USER_ACCOUNTS}">
            <div class="whitebox">
                <b><fmt:message key="cards.create.debit"/></b><br>
                <form action="controller" method="post">
                    <input type="hidden" name="command" value="new_card">
                    <input type="hidden" name="new_user_id" value="${USER_ID}">
                    <label for="account_number"><fmt:message key="card.new.for.acc"/></label>
                    <select id="account_number" name="account_number">
                        <c:forEach var="accNo" items="${USER_ACCOUNTS}">
                            <option value="${accNo}">${accNo}</option>
                        </c:forEach>
                    </select>
                    <input type="submit" value="<fmt:message key="cards.create.new"/>" class="submitButton">
                </form>
            </div>
        </c:if>
        <div class="whitebox">
            <a href="controller?command=go_to_accounts_page">
                <fmt:message key="accounts.title"/>
            </a>
        </div>
        <div class="whitebox">
            <a href="controller?command=go_to_bills_page"><fmt:message key="bills.title"/></a>
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
                        <c:when test="${ERROR_MSG eq Message.NOT_YOUR_ACCOUNT}">
                            <fmt:message key="accounts.error.alien"/>
                        </c:when>
                        <c:when test="${ERROR_MSG eq Message.CARD_CREATE_EXCEPTION}">
                            <fmt:message key="card.create.exception"/>
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
