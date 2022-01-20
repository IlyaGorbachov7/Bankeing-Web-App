<%@ page import="by.epam.baranovsky.banking.controller.constant.SessionParamName" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Starting page</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css"/>
</head>
<body>

<div class="container">
    <div class="imgColumn">
        <h1>Приложение для банкинга?</h1>
        <img src="img/tmsn.jpg" alt="" width="35%" height="35%">
        <h2>Неужели закаточная машинка сломалась?</h2>
    </div>
    <div class="userColumn">
        <c:if test="${_USER_DATA != null}">
            <div class="dataBlock">
                <b>Welcome, ${_USER_DATA.firstName}</b>
                <table class="userTable">
                    <tr>
                        <td><b>User data:</b></td>
                    </tr>
                    <tr>
                        <td>L. name:</td>
                        <td><b>${_USER_DATA.lastName}</b></td>
                    </tr>
                    <tr>
                        <td>F. name:</td>
                        <td><b>${_USER_DATA.firstName}</b></td>
                    </tr>
                    <c:if test="${_USER_DATA.patronymic != null}">
                        <tr>
                            <td>Patronymic:</td>
                            <td><b>${_USER_DATA.patronymic}</b></td>
                        </tr>
                    </c:if>
                    <tr>
                        <td>Email:</td>
                        <td><b>${_USER_DATA.email}</b></td>
                    </tr>
                    <tr>
                        <td>Created:</td>
                        <td><b>${_USER_DATA.dateCreated}</b></td>
                    </tr>
                    <tr>
                        <td>Birthdate:</td>
                        <td><b>${_USER_DATA.birthDate}</b></td>
                    </tr>
                </table>

            </div>
            <div class="dataBlock">
                <a href="controller?command=goto_accounts_page">Your accounts</a>
            </div>

        </c:if>
        <c:if test="${_USER_DATA == null}">
            <div class="dataBlock">
                <a href="controller?command=goto_register_page">Register</a>
                <br>
                <a href="controller?command=goto_login_page">Login</a>
            </div>
        </c:if>
    </div>
    <div class="bottomCommands">
        <c:if test="${_USER_DATA != null}">
            <a href="controller?command=logout">Log out?</a>
            <br>
            <c:forEach var="command" items="${_PERMISSIONS}">
                <c:if test="${command == 'ADMIN_SURPRISE'}">
                    <a href="controller?command=${command}">Admin stuff!</a>
                </c:if>
            </c:forEach>
        </c:if>
    </div>
</div>


</body>
</html>
