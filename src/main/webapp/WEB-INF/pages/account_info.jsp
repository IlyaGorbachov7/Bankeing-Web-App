<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="by.epam.baranovsky.banking.constant.DBMetadata" %>
<%@ page import="by.epam.baranovsky.banking.constant.Message" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale.locale"/>
<c:set var="USER_INFO_ID" scope="session" value="${param.user_info_id}"/>

<html>
<head>
    <title><fmt:message key="account.info.title" /></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/info.css"/>
</head>
<body>
<div class="container">
    <div class="formContainer">
        <h3><fmt:message key="account.info.title"/>:</h3>
        <br>
        <div class="whitebox">
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
                    <td>${ACCOUNT_DATA.accountNumber}</td>
                    <td>${ACCOUNT_DATA.balance}$</td>
                    <td>${ACCOUNT_DATA.yearlyInterestRate}%</td>
                    <td>
                        <c:choose>
                            <c:when test="${ACCOUNT_DATA.statusId == DBMetadata.ACCOUNT_STATUS_BLOCKED}">
                                <fmt:message key="accounts.locked"/>
                            </c:when>
                            <c:when test="${ACCOUNT_DATA.statusId == DBMetadata.ACCOUNT_STATUS_SUSPENDED}">
                                <fmt:message key="accounts.suspended"/>
                            </c:when>
                            <c:when test="${ACCOUNT_DATA.statusId == DBMetadata.ACCOUNT_STATUS_UNLOCKED}">
                                <fmt:message key="accounts.unlocked"/>
                            </c:when>
                            <c:otherwise>
                                <fmt:message key="error.unknown"/>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <tr>
                    <td>
                        <fmt:message key="accounts.users"/>:
                    </td>
                </tr>
                <tr>
                    <td colspan="3">
                        <ul>
                            <c:forEach var="user" items="${ACCOUNT_USERS_INFO}">
                                <li>${user}</li>
                            </c:forEach>
                        </ul>
                    </td>
                </tr>
                <tr>
                    <td>
                        <fmt:message key="accounts.see.cards"/>:
                    </td>
                </tr>
                <tr>
                    <td colspan="3">
                        <ul>
                            <c:forEach var="card" items="${ACCOUNT_CARDS_INFO}">
                                <li>
                                    <a href="controller?command=go_to_card_info&card_id=${card.id}">
                                        ${card.number}
                                    </a>
                                </li>
                            </c:forEach>
                        </ul>
                    </td>
                </tr>
                <c:if test="${USER_ROLE_ID eq DBMetadata.USER_ROLE_ADMIN or USER_ROLE_ID eq DBMetadata.USER_ROLE_EMPLOYEE}">
                    <tr>
                        <c:if test="${USER_ROLE_ID eq DBMetadata.USER_ROLE_ADMIN or not VIEW_ONLY}">
                            <td style="padding-top: 2em; padding-bottom: 2em">
                                <a href="controller?command=update_account&account_id=${ACCOUNT_DATA.id}&account_new_status=${DBMetadata.ACCOUNT_STATUS_BLOCKED}">
                                    <fmt:message key="accounts.lock.account"/>
                                </a>
                            </td>
                        </c:if>
                        <c:if test="${USER_ROLE_ID eq DBMetadata.USER_ROLE_ADMIN}">
                            <td>
                                <a href="controller?command=update_account&account_id=${ACCOUNT_DATA.id}&account_new_status=${DBMetadata.ACCOUNT_STATUS_UNLOCKED}">
                                    <fmt:message key="unlock.this.card"/>
                                </a>
                            </td>
                        </c:if>
                        <td>
                            <a href="controller?command=update_account&account_id=${ACCOUNT_DATA.id}&account_new_status=${DBMetadata.ACCOUNT_STATUS_SUSPENDED}">
                                <fmt:message key="accounts.suspend.account"/>
                            </a>
                        </td>
                    </tr>

                </c:if>
                <c:if test="${ACCOUNT_DATA.statusId != DBMetadata.ACCOUNT_STATUS_BLOCKED and not VIEW_ONLY}">
                    <tr>
                        <td colspan="4">
                            <a href="controller?command=remove_account_user_self&account_id=${ACCOUNT_DATA.id}">
                                <fmt:message key="account.info.deny.ownership"/>
                            </a>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <a href="controller?command=new_card&new_user_id=${USER_ID}&account_number=${ACCOUNT_DATA.accountNumber}">
                                <fmt:message key="cards.new.for.yourself"/>
                            </a>, <fmt:message key="msg.or"/>:
                        </td>
                        <td colspan="2">
                            <form action="controller" method="post">
                                <input type="hidden" name="account_number" value="${ACCOUNT_DATA.accountNumber}">
                                <input type="hidden" name="account_id" value="${ACCOUNT_DATA.id}">
                                <select id="command" name="command">
                                    <option value="add_account_user"><fmt:message key="accounts.add.user"/></option>
                                    <option value="new_card"><fmt:message key="cards.new.for.another"/></option>
                                </select>
                                <table class="formTable">
                                    <tr class="formRow">
                                        <td><fmt:message key="user.fname"/>:</td>
                                        <td><input type="text" id="name" name="name" maxlength="45" required></td>
                                    </tr>
                                    <tr class="formRow">
                                        <td><fmt:message key="user.lname"/>:</td>
                                        <td><input type="text" id="surname" name="surname" maxlength="45" required></td>
                                    </tr>
                                    <tr class="formRow">
                                        <td><fmt:message key="user.patronymic"/>:</td>
                                        <td><input type="text" id="patronymic" maxlength="45" name="patronymic"></td>
                                    </tr>
                                    <tr class="formRow">
                                        <td><fmt:message key="user.pass.series"/>:</td>
                                        <td><input type="text" id="passport_series" name="passport_series" pattern="[A-Z]{2}" maxlength="2" minlength="2" required></td>
                                    </tr>
                                    <tr class="formRow">
                                        <td><fmt:message key="user.pass.num"/>:</td>
                                        <td><input type="text" id="passport_number" pattern="[0-9]{7}" name="passport_number" minlength="7" maxlength="7" required></td>
                                    </tr>
                                    <tr class="formRow">
                                        <td><fmt:message key="user.birthdate"/>:</td>
                                        <td> <input type="date" id="birthdate" name="birthdate" min="1900-01-01" required></td>
                                    </tr>
                                    <tr class="formRow">
                                        <td>
                                            <input type="submit" value="<fmt:message key="account.info.add"/>" class="submitButton">
                                        </td>
                                    </tr>
                                </table>
                            </form>
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td colspan="4">
                        <c:if test="${not empty ERROR_MSG}">
                            <i class="errorMsg">
                                <fmt:message key="logination.error"/>:
                                <c:choose>
                                    <c:when test="${ERROR_MSG eq Message.NOT_YOUR_ACCOUNT}">
                                        <fmt:message key="accounts.error.alien"/>
                                    </c:when>
                                    <c:when test="${ERROR_MSG eq Message.CANNOT_ADD_SELF}">
                                        <fmt:message key="accounts.error.wrong.add.self"/>
                                    </c:when>
                                    <c:when test="${ERROR_MSG eq Message.ACCOUNT_LOCKED}">
                                        <fmt:message key="account.info.locked"/>
                                    </c:when>
                                    <c:when test="${ERROR_MSG eq Message.NO_SUCH_USER}">
                                        <fmt:message key="edit.user.nouser"/>
                                    </c:when>
                                    <c:when test="${ERROR_MSG eq Message.AMBIGUOUS_USER_DATA}">
                                        <fmt:message key="account.info.ambiguous.data"/>
                                    </c:when>
                                    <c:when test="${ERROR_MSG eq Message.ONLY_USER}">
                                        <fmt:message key="account.info.only.user.left"/>
                                    </c:when>
                                    <c:when test="${ERROR_MSG eq Message.USER_BANNED}">
                                        <fmt:message key="logination.banned"/>
                                    </c:when>
                                    <c:when test="${ERROR_MSG eq Message.CARD_CREATE_EXCEPTION}">
                                        <fmt:message key="card.create.exception"/>
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
            <c:when test="${not empty USER_INFO_ID}">
                <a href="controller?command=go_to_user_info&checked_user_id=${USER_INFO_ID}">
                    <fmt:message key="card.info.back"/>
                </a>
            </c:when>
            <c:otherwise>
                <a href="controller?command=go_to_accounts_page">
                    <fmt:message key="card.info.back"/>
                </a>
            </c:otherwise>
        </c:choose>
    </div>

</div>
</body>
</html>
