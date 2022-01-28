<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="by.epam.baranovsky.banking.constant.Message" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale.locale"/>
<html>
<head>
    <title><fmt:message key="registration.signup"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css"/>
</head>
<body>
<div class="container">
    <div class="formContainer">
        <h4><fmt:message key="registration.signup"/>:</h4>
        <form action="controller" method="post">
            <input type="hidden" name="command" value="register">
            <table class="formTable">
                <tr class="formRow">
                    <td><fmt:message key="user.email"/>:</td>
                    <td><input type="email" id="email" name="email" required></td>
                </tr>
                <tr class="formRow">
                    <td><fmt:message key="user.password"/>:</td>
                    <td><input type="password" id="password" name="password" required></td>
                </tr>
                <tr class="formRow">
                    <td><fmt:message key="user.confirmpass"/>:</td>
                    <td><input type="password" id="conf_password" name="conf_password" required></td>
                </tr>
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
                    <td><input type="text" id="passport_series" name="passport_series" minlength="2" maxlength="2" pattern="[A-Z]{2}" required></td>
                </tr>
                <tr class="formRow">
                    <td><fmt:message key="user.pass.num"/>:</td>
                    <td><input type="text" id="passport_number" name="passport_number" maxlength="7" minlength="7" pattern="[0-9]{7}"  required></td>
                </tr>
                <tr class="formRow">
                    <td><fmt:message key="user.birthdate"/>:</td>
                    <td> <input type="date" id="birthdate" name="birthdate" min="1900-01-01" max="2020-01-01" required></td>
                </tr>
            </table>
            <input type="submit" value="<fmt:message key="logination.submit"/>" class="submitButton">
        </form>
        <c:if test="${ERROR_MSG != null}">
            <i class="errorMsg">
                <fmt:message key="logination.error"/>:
                <c:choose>
                    <c:when test="${ERROR_MSG eq Message.REGISTER_FAILED}">
                        <fmt:message key="registration.fail"/>
                    </c:when>
                    <c:when test="${ERROR_MSG eq Message.REGISTER_EXCEPTION}">
                        <fmt:message key="registration.fail"/>
                    </c:when>
                    <c:when test="${ERROR_MSG eq Message.REGISTER_PASS_MISMATCH}">
                        <fmt:message key="registration.password.mismatch"/>
                    </c:when>
                    <c:when test="${ERROR_MSG eq Message.USER_EXISTS}">
                        <fmt:message key="registration.user.exists"/>
                    </c:when>
                    <c:when test="${ERROR_MSG eq Message.WRONG_INPUT}">
                        <fmt:message key="registration.incorrectinput"/>
                    </c:when>
                    <c:otherwise>
                        <fmt:message key="error.unknown"/>
                    </c:otherwise>
                </c:choose>
            </i>
        </c:if>
        <br>
        <fmt:message key="registration.goto.login"/>
        <a href="controller?command=go_to_login_page">
            <fmt:message key="logination.title"/>
        </a>

    </div>

</div>
</body>
</html>
