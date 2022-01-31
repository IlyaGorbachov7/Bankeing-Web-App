<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="by.epam.baranovsky.banking.constant.Message" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale.locale"/>
<c:set var="passwordPattern" value="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}"/>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title><fmt:message key="edit.user.title"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css"/>
</head>
<body>
<div class="container">
    <div class="formContainer">
        <h4><fmt:message key="edit.user.title"/>:</h4>
        <form action="controller" method="post">
            <input type="hidden" name="command" value="edit_user">
            <table class="formTable">
                <tr class="formRow">
                    <td><fmt:message key="user.email"/>:</td>
                    <td><input type="email" id="email" name="email" required value="${USER_DATA.email}"></td>
                </tr>
                <tr class="formRow">
                    <td><fmt:message key="edit.user.newpass"/>:</td>
                    <td><input type="password" title="<fmt:message key="login.pass.tooltip"/>" id="password" name="password" pattern="${passwordPattern}"></td>
                </tr>
                <tr class="formRow">
                    <td><fmt:message key="edit.user.newpass.confirm"/>:</td>
                    <td><input type="password" id="conf_password" name="conf_password"></td>
                </tr>
                <tr class="formRow">
                    <td><fmt:message key="user.fname"/>:</td>
                    <td><input type="text" id="name" name="name" maxlength="45" value="${USER_DATA.firstName}" required ></td>
                </tr>
                <tr class="formRow">
                    <td><fmt:message key="user.lname"/>:</td>
                    <td><input type="text" id="surname" name="surname" maxlength="45" value="${USER_DATA.lastName}" required></td>
                </tr>
                <tr class="formRow">
                    <td><fmt:message key="user.patronymic"/>:</td>
                    <td><input type="text" id="patronymic" maxlength="45" name="patronymic" value="${USER_DATA.patronymic}"></td>
                </tr>
                <tr class="formRow">
                    <td><fmt:message key="user.pass.series"/>:</td>
                    <td><input type="text" id="passport_series" name="passport_series" maxlength="2" value="${USER_DATA.passportSeries}" required></td>
                </tr>
                <tr class="formRow">
                    <td><fmt:message key="user.pass.num"/>:</td>
                    <td><input type="number" id="passport_number" name="passport_number" maxlength="7" value="${USER_DATA.passportNumber}"  required></td>
                </tr>
                <tr class="formRow">
                    <td><fmt:message key="user.birthdate"/>:</td>
                    <td> <input type="date" id="birthdate" name="birthdate" min="1900-01-01" value="${USER_DATA.birthDate}" required></td>
                </tr>
            </table>
            <input type="submit" value="Submit" class="submitButton">
        </form>
        <c:if test="${ERROR_MSG != null}">
            <i class="errorMsg">
                <fmt:message key="logination.error"/>:
                <c:choose>
                    <c:when test="${ERROR_MSG eq Message.EDIT_USER_EXCEPTION}">
                        <fmt:message key="edit.user.exception"/>
                    </c:when>
                    <c:when test="${ERROR_MSG eq Message.EDIT_USER_PASS_MISMATCH}">
                        <fmt:message key="registration.password.mismatch"/>
                    </c:when>
                    <c:when test="${ERROR_MSG eq Message.WRONG_INPUT}">
                        <fmt:message key="registration.incorrectinput"/>
                    </c:when>
                    <c:when test="${ERROR_MSG eq Message.NO_SUCH_USER}">
                        <fmt:message key="edit.user.nouser"/>
                    </c:when>
                    <c:otherwise>
                        <fmt:message key="error.unknown"/>
                    </c:otherwise>
                </c:choose>
            </i>
        </c:if>
        <br>
        <a href="controller?command=go_to_main_page">
            <fmt:message key="edit.user.goto.main"/>
        </a>

    </div>

</div>
</body>
</html>
