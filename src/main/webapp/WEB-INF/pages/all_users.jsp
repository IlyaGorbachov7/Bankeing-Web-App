<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="by.epam.baranovsky.banking.constant.DBMetadata" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale.locale"/>

<c:set var="all_users" scope="page" value="${ALL_USERS}"/>
<c:set var="totalCount" scope="page" value="${fn.length(all_users)}"/>
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
    <title><fmt:message key="all.users.title"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css"/>
</head>
<body>
<div class="container">
    <div class="imgColumn">
        <h2><fmt:message key="all.users.header"/>:</h2>
        <a href="controller?command=go_to_all_users&start=${pageStart - perPage}"><fmt:message key="home.operations.prev.page"/></a>${pageStart + 1} - ${pageStart + perPage}
        <a href="controller?command=go_to_all_users&start=${pageStart + perPage}"><fmt:message key="home.operations.next.page"/></a>
        <br>
        <form action="controller" method="post">
            <input type="hidden" name="command" value="go_to_all_users">
            <input type="text" name="email" placeholder="<fmt:message key="user.email"/>">
            <input type="submit" value="<fmt:message key="search.glass"/>">
        </form>
        <c:forEach var="user" items="${ALL_USERS}" begin="${pageStart}" end="${pageStart + perPage - 1}">
         <div class="operationbox">
             <table class="blankTable">
                 <tr>
                     <td>
                         <fmt:message key="user.data"/>:
                     </td>
                     <td>
                         <c:choose>
                             <c:when test="${user.roleId eq DBMetadata.USER_ROLE_BANNED}">
                                 <fmt:message key="user.role.banned"/>
                             </c:when>
                             <c:when test="${user.roleId eq DBMetadata.USER_ROLE_REGULAR}">
                                 <fmt:message key="user.role.regular"/>
                             </c:when>
                             <c:when test="${user.roleId eq DBMetadata.USER_ROLE_EMPLOYEE}">
                                 <fmt:message key="user.role.employee"/>
                             </c:when>
                             <c:when test="${user.roleId eq DBMetadata.USER_ROLE_ADMIN}">
                                 <fmt:message key="user.role.admin"/>
                             </c:when>
                         </c:choose>
                     </td>
                 </tr>
                 <tr>
                     <td>
                         <b><fmt:message key="user.email"/></b>
                     </td>
                     <td colspan="2">
                             ${user.email}
                     </td>
                 </tr>
                 <tr>
                     <td>
                         <b><fmt:message key="all.users.full.name"/></b>
                     </td>
                     <td colspan="3">
                             ${user.firstName} ${user.lastName} ${user.patronymic}
                     </td>
                 </tr>
                 <tr>
                     <td>
                         <b><fmt:message key="all.users.passport"/>:</b>
                     </td>
                     <td>${user.passportSeries}${user.passportNumber}</td>
                     <td>
                         <b><fmt:message key="user.birthdate"/>:</b>
                     </td>
                     <td>${user.birthDate}</td>
                 </tr>
                 <tr>
                     <td>
                         <b><fmt:message key="all.users.last.login"/>:</b>
                     </td>
                     <td>${user.lastLogin}</td>
                     <td>
                         <b><fmt:message key="user.created"/>:</b>
                     </td>
                     <td>${user.dateCreated}</td>
                 </tr>
                 <tr>
                     <td colspan="3"></td>
                     <td style="padding-top: 2em">
                         <a href="controller?command=go_to_user_info&checked_user_id=${user.id}">
                             <fmt:message key="user.info"/>
                         </a>
                     </td>
                 </tr>
                 <c:if test="${USER_ROLE_ID eq DBMetadata.USER_ROLE_ADMIN}">
                     <tr>
                         <td>
                             <a href="<c:url value="controller?command=change_user_role&checked_user_id=${user.id}&new_role_id=${DBMetadata.USER_ROLE_BANNED}"/>">
                                 <fmt:message key="all.users.ban"/>
                             </a>
                         </td>
                         <td>
                             <a href="<c:url value="controller?command=change_user_role&checked_user_id=${user.id}&new_role_id=${DBMetadata.USER_ROLE_REGULAR}"/>">
                                 <fmt:message key="all.users.unban"/>
                             </a>
                         </td>
                         <td>
                             <a href="<c:url value="controller?command=change_user_role&checked_user_id=${user.id}&new_role_id=${DBMetadata.USER_ROLE_EMPLOYEE}"/>">
                                 <fmt:message key="all.users.make.employee"/>
                             </a>
                         </td>
                         <td>
                             <a href="<c:url value="controller?command=change_user_role&checked_user_id=${user.id}&new_role_id=${DBMetadata.USER_ROLE_ADMIN}"/>">
                                 <fmt:message key="all.users.make.admin"/>
                             </a>
                         </td>
                     </tr>
                 </c:if>
             </table>
         </div>
        </c:forEach>

    </div>
    <div class="userColumn">
        <div class="whitebox">
            <a href="controller?command=go_to_pending_accounts">
                <fmt:message key="accounts.pending.requests"/>
            </a>
        </div>
        <div class="whitebox">
            <a href="controller?command=go_to_bill_requests">
                <fmt:message key="bill.requests.title"/>
            </a>
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
                        <c:when test="${ERROR_MSG eq Message.WRONG_NEW_ROLE}">
                            <fmt:message key="change.user.role.wrong.role"/>
                        </c:when>
                        <c:otherwise>
                            <fmt:message key="error.unknown"/>
                        </c:otherwise>
                    </c:choose>
                </i>
            </div>
        </c:if>
        <br>
        <a href="controller?command=logout"><fmt:message key="home.logout.button"/></a>
        <br>
        <div>
            <a href="controller?command=change_locale&locale=en">EN  </a>
            <a href="controller?command=change_locale&locale=ru">  RU</a>
        </div>
    </div>
</div>
</body>
</html>
