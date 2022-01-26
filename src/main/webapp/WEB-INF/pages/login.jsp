<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="by.epam.baranovsky.banking.constant.Message" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale.locale"/>

<html>
<head>
    <title><fmt:message key="logination.title"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css"/>
</head>
<body>
<div class="container">
    <div class="formContainer">
        <h4><fmt:message key="logination.title"/>:</h4>
        <form action="controller" method="post">
            <input type="hidden" name="command" value="login">
            <table class="formTable">
                <tr class="formRow">
                    <td><fmt:message key="user.email"/>:</td>
                    <td><input type="email" id="email" name="email" required></td>
                </tr>
                <tr class="formRow">
                    <td><fmt:message key="user.password"/>:</td>
                    <td><input type="password" id="password" name="password" required></td>
                </tr>
            </table>
            <input type="submit" value="<fmt:message key="logination.submit"/>" class="submitButton">
        </form>
        <c:if test="${ERROR_MSG != null}">
            <i class="errorMsg">
                <fmt:message key="logination.error"/>:
                <c:choose>
                    <c:when test="${ERROR_MSG eq Message.WRONG_EMAIL_OR_PASS}">
                        <fmt:message key="logination.wrongpass"/>
                    </c:when>
                    <c:when test="${ERROR_MSG eq Message.LOGIN_EXCEPTION}">
                        <fmt:message key="logination.fail"/>
                    </c:when>
                    <c:when test="${ERROR_MSG eq Message.WRONG_INPUT}">
                        <fmt:message key="logination.wronginput"/>
                    </c:when>
                    <c:when test="${ERROR_MSG eq Message.USER_BANNED}">
                        <fmt:message key="logination.banned"/>
                    </c:when>
                    <c:otherwise>
                        <fmt:message key="error.unknown"/>
                    </c:otherwise>
                </c:choose>
            </i>
        </c:if>
        <br>
        <fmt:message key="logination.noaccount"/> <a href="controller?command=go_to_register_page"><fmt:message key="registration.signup"/></a>
    </div>

</div>
</body>
</html>