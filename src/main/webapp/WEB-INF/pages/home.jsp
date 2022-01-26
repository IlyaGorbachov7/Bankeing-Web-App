<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale.locale"/>

<html>
<head>
    <title><fmt:message key="home.title" /></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css"/>
</head>
<body>
<div class="container">
    <div class="imgColumn">
        <h1><fmt:message key="home.header"/></h1>
        <c:forEach var="entry" items="${OPERATIONS_DATA}">
            <div class="operationbox">
                <table class="blankTable">
                    <tr>
                       <td colspan="4" width="100%">
                           <b><fmt:message key="operations.operation"/>: </b>${entry.value}
                       </td>
                    </tr>
                    <tr>
                        <td width="25%">
                            <b><fmt:message key="operations.date"/>: </b>
                            <br>${entry.key.operationDate}
                        </td>
                        <td width="25%">
                            <b><fmt:message key="operations.value"/>: </b>
                            <br>${entry.key.value}
                        </td>
                        <td width="25%">
                            <c:if test="${entry.key.billId != 0}">
                                <b><fmt:message key="operations.bill"/>: </b>
                                <br>${entry.key.billId}
                            </c:if>
                        </td>
                        <td width="25%">
                            <c:if test="${entry.key.penaltyId != 0}">
                                <b><fmt:message key="operations.penalty"/>: </b>
                                <br>${entry.key.penaltyId}
                            </c:if>
                        </td>
                    </tr>
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
            <br>
            <a href="controller?command=logout"><fmt:message key="home.logout.button"/></a>
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
