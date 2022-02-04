<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="by.epam.baranovsky.banking.constant.DBMetadata" %>
<%@ page import="by.epam.baranovsky.banking.constant.Message" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale.locale"/>

<c:set var="perPage" scope="page"  value="${5}"/>
<c:set var="loans" scope="page" value="${USER_LOANS}"/>
<c:set var="loansCount" scope="page" value="${fn.length(loans)}"/>
<c:set var="loansStart" value="${param.loans_start}"/>
<c:if test="${empty loansStart or loansStart < 0}">
    <c:set var="loansStart" value="0"/>
</c:if>
<c:if test="${loansCount < loansStart}">
    <c:set var="loansStart" value="${loansStart - perPage}"/>
</c:if>

<html>
<head>
    <title><fmt:message key="loans.title"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css"/>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script>
        $(document).ready(function() {

            $("#loan_length, #loan_starting_value").change(function () {
                var $startingValue = $("#loan_starting_value").val();
                var $term = $("#loan_length").val();

                var deltaInterest=30;
                var deltaTime=117;
                var interest = 10+deltaInterest*(1-(($term-3)/deltaTime));
                document.getElementById('interest_rate').setAttribute('value', interest);

                var mathematicalInterest = 1+interest/100;
                var timePower = $term/12;
                var totalValue =  $startingValue*Math.pow(mathematicalInterest, timePower);
                document.getElementById('total_payment').setAttribute('value', totalValue);

                var singlePayment = totalValue/$term;
                document.getElementById('single_payment').setAttribute('value', singlePayment);
            });
        });
    </script>
</head>
<body>
<div class="container">
    <div class="imgColumn">
        <h2><fmt:message key="loans.title"/></h2>
        <a href="controller?command=go_to_loans_page&loans_start=${loansStart - perPage}"><fmt:message key="home.operations.prev.page"/></a>${loansStart + 1} - ${loansStart + perPage}
        <a href="controller?command=go_to_loans_page&loans_start=${loansStart + perPage}"><fmt:message key="home.operations.next.page"/></a>
        <c:forEach var="loan" items="${loans}" begin="${loansStart}" end="${loansStart + perPage - 1}">
            <div class="operationbox">
                <table class="blankTable">
                    <tr>
                        <td><b><fmt:message key="loans.start"/></b></td>
                        <td><b><fmt:message key="loans.total"/></b></td>
                        <td><b><fmt:message key="loans.single"/></b></td>
                        <td><b><fmt:message key="accounts.interest"/></b></td>
                    </tr>
                    <tr>
                        <td>${loan.startingValue}$</td>
                        <td>${loan.totalPaymentValue}$</td>
                        <td>${loan.singlePaymentValue}$</td>
                        <td>${loan.yearlyInterestRate}$</td>
                    </tr>
                    <tr>
                        <td><b><fmt:message key="loans.issue"/></b></td>
                        <td><b><fmt:message key="loans.due"/></b></td>
                        <td><b><fmt:message key="accounts.status"/></b></td>
                    </tr>
                    <tr>
                        <td>${loan.issueDate}</td>
                        <td>${loan.dueDate}</td>
                        <td>
                            <c:choose>
                                <c:when test="${loan.statusId == DBMetadata.LOAN_STATUS_PENDING}">
                                    <fmt:message key="loans.status.pending"/>
                                </c:when>
                                <c:when test="${loan.statusId == DBMetadata.LOAN_STATUS_OVERDUE}">
                                    <fmt:message key="loans.status.overdue"/>
                                </c:when>
                                <c:when test="${loan.statusId == DBMetadata.LOAN_STATUS_CLOSED}">
                                    <fmt:message key="loans.status.closed"/>
                                </c:when>
                            </c:choose>
                        </td>
                    </tr>
                </table>
            </div>
        </c:forEach>
    </div>
    <div class="userColumn">
        <div class="whitebox">
            <h4><fmt:message key="loans.new.loan"/></h4>
            <form action="controller" method="post">
                <input type="hidden" name="command" value="new_loan"/>
                <table class="blankTable">
                    <tr>
                        <td><b><fmt:message key="loans.start"/></b></td>
                        <td><input type="number" id="loan_starting_value" name="loan_starting_value" step="0.01" min="500" max="100000" required/>$</td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <b><fmt:message key="loans.time.months"/>:</b>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <input type="range" id="loan_length" name="loan_length" min="3" max="120" value="3" step="1" required oninput="this.nextElementSibling.value = this.value"/>
                            <output>3</output>
                        </td>
                    </tr>
                    <tr>
                        <td><b><fmt:message key="loans.interest"/>:</b></td>
                        <td><input type="number" id="interest_rate" step="0.001" readonly/>%</td>
                    </tr>
                    <tr>
                        <td><fmt:message key="loans.total"/></td>
                        <td><input type="number" id="total_payment" step="0.001" readonly/>$</td>
                    </tr>
                    <tr>
                        <td><fmt:message key="loans.single"/></td>
                        <td><input type="number" id="single_payment" step="0.001" readonly/>$</td>
                    </tr>
                    <tr>
                        <td><b><fmt:message key="card.acc"/>:</b></td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <select name="account_id" required>
                                <c:forEach var="account" items="${USER_ACCOUNTS}">
                                    <option value="${account.id}">
                                            ${account.accountNumber}, <fmt:message key="accounts.balance"/>: <fmt:formatNumber type="number" maxFractionDigits="3" value="${account.balance}"/>$
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td><input type="submit" value="<fmt:message key="logination.submit"/>"/></td>
                    </tr>
                </table>
            </form>
        </div>
        <div class="whitebox">
            <a href="controller?command=go_to_accounts_page">
                <fmt:message key="accounts.title"/>
            </a>
        </div>
        <div class="whitebox">
            <a href="controller?command=go_to_cards_page">
                <fmt:message key="cards.title"/>
            </a>
        </div>
        <div class="whitebox">
            <a href="controller?command=go_to_bills_page">
                <fmt:message key="bills.title"/>
            </a>
        </div>
        <div class="whitebox">
            <a href="controller?command=go_to_penalties_page">
                <fmt:message key="penalties.title"/>
            </a>
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
                        <c:when test="${ERROR_MSG eq Message.LOAN_TOO_SHORT_OR_LONG}">
                            <fmt:message key="loans.time.error"/>
                        </c:when>
                        <c:when test="${ERROR_MSG eq Message.LOAN_VALUE_TOO_SMALL}">
                            <fmt:message key="loans.value.small"/>
                        </c:when>
                        <c:when test="${ERROR_MSG eq Message.LOAN_NOT_FOR_YOUR_ACCOUNT}">
                            <fmt:message key="loans.not.for.you"/>
                        </c:when>
                        <c:when test="${ERROR_MSG eq Message.TOO_MANY_LOANS}">
                            <fmt:message key="loans.too.many"/>
                        </c:when>
                        <c:when test="${ERROR_MSG eq Message.NO_MONEY}">
                            <fmt:message key="loans.no.money"/>
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
