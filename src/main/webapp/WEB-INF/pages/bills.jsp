<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="by.epam.baranovsky.banking.constant.DBMetadata" %>
<%@ page import="by.epam.baranovsky.banking.constant.Message" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale.locale"/>

<c:set var="perPage" scope="page"  value="${5}"/>

<c:set var="sent_bills" scope="page" value="${SENT_BILLS}"/>
<c:set var="sentBillsCount" scope="page" value="${fn.length(sent_bills)}"/>
<c:set var="sentBillsStart" value="${param.sent_bills_start}"/>
<c:if test="${empty sentBillsStart or sentBillsStart < 0}">
    <c:set var="sentBillsStart" value="0"/>
</c:if>
<c:if test="${sentBillsCount < sentBillsStart}">
    <c:set var="sentBillsStart" value="${sentBillsStart - perPage}"/>
</c:if>

<c:set var="acquired_bills" scope="page" value="${ACQUIRED_BILLS}"/>
<c:set var="acquiredBillsCount" scope="page" value="${fn.length(acquired_bills)}"/>
<c:set var="acquiredBillsStart" value="${param.acquired_bills_start}"/>
<c:if test="${empty acquiredBillsStart or acquiredBillsStart < 0}">
    <c:set var="acquiredBillsStart" value="0"/>
</c:if>
<c:if test="${acquiredBillsCount < acquiredBillsStart}">
    <c:set var="acquiredBillsStart" value="${acquiredBillsStart - perPage}"/>
</c:if>

<html>
<head>
    <title><fmt:message key="bills.title" /></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css"/>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script>
        $(document).ready(function() {
            var $penOrNoPen = $("select#penalty-or-no");

            $("#penalty-or-no").change(function () {
                var targetName = $penOrNoPen.find("option:selected").val();

                if(targetName.localeCompare("new_bill_no_penalty")){
                    document.getElementById('penalty_type_id').removeAttribute('disabled');
                    document.getElementById('penalty_type_and_value').removeAttribute('hidden');
                    document.getElementById('bill_due_date').removeAttribute('hidden');
                    document.getElementById('penalty_notice').removeAttribute('hidden');
                    document.getElementById('due_date_input').setAttribute('required',true);

                }else if(targetName.localeCompare("new_bill_with_penalty")){
                    document.getElementById('penalty_type_id').setAttribute('disabled',true);
                    document.getElementById('penalty_type_and_value').setAttribute('hidden',true);
                    document.getElementById('bill_due_date').setAttribute('hidden',true);
                    document.getElementById('penalty_notice').setAttribute('hidden',true);
                    document.getElementById('due_date_input').removeAttribute('required');
                }
            });
        });
    </script>
</head>
<body>
<div class="container">
    <div class="imgColumn">
        <h1 class="operationbox"><fmt:message key="bills.title"/></h1>
        <h2><fmt:message key="bills.acquired"/></h2>
        <a href="controller?command=go_to_bills_page&acquired_bills_start=${acquiredBillsStart - perPage}"><fmt:message key="home.operations.prev.page"/></a>${acquiredBillsStart + 1} - ${acquiredBillsStart + perPage}
        <a href="controller?command=go_to_bills_page&acquired_bills_start=${acquiredBillsStart + perPage}"><fmt:message key="home.operations.next.page"/></a>
        <c:forEach var="bill" items="${acquired_bills}" begin="${acquiredBillsStart}" end="${acquiredBillsStart + perPage - 1}">
            <div class="operationbox">
                <table class="blankTable">
                    <tr>
                        <td><b><fmt:message key="operations.value"/></b></td>
                        <td><b><fmt:message key="loans.issue"/></b></td>
                        <td><b><fmt:message key="loans.due"/></b></td>
                    </tr>
                    <tr>
                        <td>${bill.value}$</td>
                        <td>${bill.issueDate}</td>
                        <td>${bill.dueDate}</td>
                    </tr>
                    <tr>
                        <td style="padding-top: 1em"><b><fmt:message key="bills.for"/>:</b></td>
                        <td colspan="3">
                            ${bill.userFullName}
                        </td>
                    </tr>
                    <tr>
                        <td><b><fmt:message key="bills.from"/>:</b></td>
                        <td colspan="3">
                                ${bill.bearerFullName}
                        </td>
                    </tr>
                    <c:if test="${not empty bill.penaltyId and bill.penaltyId != 0}">
                        <tr>
                            <td><b><fmt:message key="bills.penalty"/></b></td>
                            <td>
                                <c:choose>
                                    <c:when test="${bill.penaltyTypeId eq DBMetadata.PENALTY_TYPE_FEE}">
                                        <fmt:message key="penalty.type.fee"/>
                                    </c:when>
                                    <c:when test="${bill.penaltyTypeId eq DBMetadata.PENALTY_TYPE_LAWSUIT}">
                                        <fmt:message key="penalty.type.lawsuit"/>
                                    </c:when>
                                    <c:when test="${bill.penaltyTypeId eq DBMetadata.PENALTY_TYPE_ACCS_LOCK}">
                                        <fmt:message key="penalty.type.acc.lock"/>
                                    </c:when>
                                    <c:when test="${bill.penaltyTypeId eq DBMetadata.PENALTY_TYPE_CARDS_LOCK}">
                                        <fmt:message key="penalty.type.card.lock"/>
                                    </c:when>
                                    <c:when test="${bill.penaltyTypeId eq DBMetadata.PENALTY_TYPE_ACCS_SUSP}">
                                        <fmt:message key="penalty.type.acc.suspension"/>
                                    </c:when>
                                </c:choose>
                            </td>
                            <c:if test="${not empty bill.penaltyValue and bill.penaltyValue != 0}">
                                <td><b><fmt:message key="operations.value"/>: </b></td>
                                <td>${bill.penaltyValue}$</td>
                            </c:if>
                        </tr>
                    </c:if>
                    <c:if test="${not empty bill.loanId and bill.loanId != 0}">
                        <tr>
                            <td><b><fmt:message key="bills.forloan"/> </b></td>
                            <td>${bill.loanId}</td>
                        </tr>
                    </c:if>
                    <c:if test="${not empty bill.notice}">
                        <tr>
                            <td><b><fmt:message key="penalty.notice"/> </b></td>
                            <td colspan="3">${bill.notice}</td>
                        </tr>
                    </c:if>
                    <tr>
                        <td style="padding-top: 2em">
                            <c:url var="transfer" value="controller?command=go_to_transfer_page">
                                <c:param name="bill_id" value="${bill.id}"/>
                            </c:url>
                            <a href="${transfer}"><fmt:message key="bills.pay"/></a>
                        </td>
                    </tr>
                </table>
            </div>
        </c:forEach>
        <h2><fmt:message key="bills.sent"/></h2>
        <a href="controller?command=go_to_bills_page&sent_bills_start=${sentBillsStart - perPage}"><fmt:message key="home.operations.prev.page"/></a>${sentBillsStart + 1} - ${sentBillsStart + perPage}
        <a href="controller?command=go_to_bills_page&sent_bills_start=${sentBillsStart + perPage}"><fmt:message key="home.operations.next.page"/></a>
        <c:forEach var="bill" items="${sent_bills}" begin="${sentBillsStart}" end="${sentBillsStart + perPage - 1}">
            <div class="operationbox">
                <table class="blankTable">
                    <tr>
                        <td><b><fmt:message key="operations.value"/></b></td>
                        <td><b><fmt:message key="loans.issue"/></b></td>
                        <td><b><fmt:message key="loans.due"/></b></td>
                    </tr>
                    <tr>
                        <td>${bill.value}$</td>
                        <td>${bill.issueDate}</td>
                        <td>${bill.dueDate}</td>
                    </tr>
                    <tr>
                        <td style="padding-top: 1em"><b><fmt:message key="bills.for"/>:</b></td>
                        <td colspan="3">
                                ${bill.userFullName}
                        </td>
                    </tr>
                    <tr>
                        <td><b><fmt:message key="bills.from"/>:</b></td>
                        <td colspan="3">
                                ${bill.bearerFullName}
                        </td>
                    </tr>
                    <c:if test="${not empty bill.penaltyId and bill.penaltyId != 0}">
                        <tr>
                            <td><b><fmt:message key="bills.penalty"/></b></td>
                            <td>
                                <c:choose>
                                    <c:when test="${bill.penaltyTypeId eq DBMetadata.PENALTY_TYPE_FEE}">
                                        <fmt:message key="penalty.type.fee"/>
                                    </c:when>
                                    <c:when test="${bill.penaltyTypeId eq DBMetadata.PENALTY_TYPE_LAWSUIT}">
                                        <fmt:message key="penalty.type.lawsuit"/>
                                    </c:when>
                                    <c:when test="${bill.penaltyTypeId eq DBMetadata.PENALTY_TYPE_ACCS_LOCK}">
                                        <fmt:message key="penalty.type.acc.lock"/>
                                    </c:when>
                                    <c:when test="${bill.penaltyTypeId eq DBMetadata.PENALTY_TYPE_CARDS_LOCK}">
                                        <fmt:message key="penalty.type.card.lock"/>
                                    </c:when>
                                    <c:when test="${bill.penaltyTypeId eq DBMetadata.PENALTY_TYPE_ACCS_SUSP}">
                                        <fmt:message key="penalty.type.acc.suspension"/>
                                    </c:when>
                                </c:choose>
                            </td>
                            <c:if test="${not empty bill.penaltyValue and bill.penaltyValue != 0}">
                                <td><b><fmt:message key="operations.value"/>: </b></td>
                                <td>${bill.penaltyValue}$</td>
                            </c:if>
                        </tr>
                    </c:if>
                    <c:if test="${not empty bill.loanId and bill.loanId != 0}">
                        <tr>
                            <td><b><fmt:message key="bills.forloan"/> </b></td>
                            <td>${bill.loanId}</td>
                        </tr>
                    </c:if>
                    <c:if test="${not empty bill.notice}">
                        <tr>
                            <td><b><fmt:message key="penalty.notice"/> </b></td>
                            <td colspan="3">${bill.notice}</td>
                        </tr>
                    </c:if>
                </table>
            </div>
        </c:forEach>
    </div>
    <div class="userColumn">
       <div class="whitebox">
           <form action="controller" method="post">
              <table class="blankTable">
                   <tr>
                       <td colspan="3">
                           <select id="penalty-or-no" name="command" required>
                               <option value="new_bill_no_penalty" selected>
                                   <fmt:message key="bills.new.bill.no.penalty"/>
                               </option>
                               <option value="new_bill_with_penalty">
                                   <fmt:message key="bills.new.with.penalty"/>
                               </option>
                           </select>
                       </td>
                   </tr>
                   <tr>
                       <td><fmt:message key="user.email"/>:</td>
                       <td><input type="email" id="email" name="email" placeholder="<fmt:message key="bills.for"/>" required></td>
                   </tr>
                   <tr>
                       <td colspan="3"><b><fmt:message key="all.users.full.name"/>:</b></td>
                   </tr>
                   <tr>
                       <td><input type="text" width="15em" id="surname" name="surname" placeholder="<fmt:message key="user.lname"/>" maxlength="45" required></td>
                       <td><input type="text" width="15em" id="name" name="name" maxlength="45" placeholder="<fmt:message key="user.fname"/>" required></td>
                       <td><input type="text" width="15em" id="patronymic" maxlength="45" name="patronymic" placeholder="<fmt:message key="user.patronymic"/>"></td>
                   </tr>
                   <tr>
                       <td><fmt:message key="user.birthdate"/>:</td>
                       <td> <input type="date" id="birthdate" name="birthdate" min="1900-01-01" required></td>
                   </tr>
                   <tr>
                       <td colspan="3">
                           <select name="account_id" required>
                               <option disabled selected><fmt:message key="bills.new.account"/></option>
                               <c:forEach var="account" items="${USER_ACCOUNTS}">
                                   <option value="${account.id}">
                                           ${account.accountNumber}, <fmt:message key="accounts.balance"/>: <fmt:formatNumber type="number" maxFractionDigits="3" value="${account.balance}"/>$</option>
                               </c:forEach>
                           </select>
                       </td>
                   </tr>
                  <tr id="bill_due_date" hidden>
                      <td>
                          <b><fmt:message key="loans.due"/>:</b>
                      </td>
                      <td>
                        <input type="date" id="due_date_input" name="bill_due_date"/>
                      </td>
                  </tr>
                   <tr>
                       <td>
                           <input name="bill_value" placeholder="<fmt:message key="operations.value"/>" type="number" min="0" step=".01" required/>
                       </td>
                       <td colspan="2">
                           <textarea name="bill_notice" rows="1" placeholder="<fmt:message key="bills.notice.for.bill"/>"></textarea>
                       </td>
                   </tr>
                  <tr id="penalty_type_and_value" hidden>
                      <td>
                          <select name="penalty_type_id" id="penalty_type_id" required>
                              <option selected disabled><fmt:message key="bills.new.select.penalty"/></option>
                              <option value="${DBMetadata.PENALTY_TYPE_ACCS_LOCK}">
                                  <fmt:message key="penalty.type.acc.lock"/>
                              </option>
                              <option value="${DBMetadata.PENALTY_TYPE_CARDS_LOCK}">
                                  <fmt:message key="penalty.type.card.lock"/>
                              </option>
                              <option value="${DBMetadata.PENALTY_TYPE_ACCS_SUSP}">
                                  <fmt:message key="penalty.type.acc.suspension"/>
                              </option>
                              <option value="${DBMetadata.PENALTY_TYPE_FEE}">
                                  <fmt:message key="penalty.type.fee"/>
                              </option>
                              <option value="${DBMetadata.PENALTY_TYPE_LAWSUIT}">
                                  <fmt:message key="penalty.type.lawsuit"/>
                              </option>
                          </select>
                      </td>
                      <td>
                          <input type="number" name="penalty_value" min="0" value="300"/>
                      </td>
                  </tr>
                  <tr id="penalty_notice" hidden>
                      <td colspan="3">
                          <textarea name="penalty_notice" rows="1" placeholder="<fmt:message key="bills.notice.for.penalty"/>"></textarea>
                      </td>
                  </tr>
                  <tr>
                       <td></td>
                       <td>
                           <input type="submit" value="<fmt:message key="logination.submit"/>">
                       </td>
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
            <a href="controller?command=go_to_loans_page">
                <fmt:message key="loans.title"/>
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
                        <c:when test="${ERROR_MSG eq Message.NO_SUCH_USER}">
                            <fmt:message key="edit.user.nouser"/>
                        </c:when>
                        <c:when test="${ERROR_MSG eq Message.ACCOUNT_LOCKED_OR_NOT_YOURS}">
                            <fmt:message key="bills.error.account.wrong"/>
                        </c:when>
                        <c:when test="${ERROR_MSG eq Message.AMBIGUOUS_USER_DATA}">
                            <fmt:message key="account.info.ambiguous.data"/>
                        </c:when>
                        <c:when test="${ERROR_MSG eq Message.USER_BANNED}">
                            <fmt:message key="logination.banned"/>
                        </c:when>
                        <c:when test="${ERROR_MSG eq Message.BILL_TO_SELF}">
                            <fmt:message key="bills.error.bill.to.self"/>
                        </c:when>
                        <c:when test="${ERROR_MSG eq Message.OPERATION_INVALID_VALUE}">
                            <fmt:message key="transfer.err.invalid.value"/>
                        </c:when>
                        <c:when test="${ERROR_MSG eq Message.TOO_MANY_BILL_REQUESTS}">
                            <fmt:message key="bills.error.too.many"/>
                        </c:when>
                        <c:when test="${ERROR_MSG eq Message.PENALTY_BILL_INTERSECTION}">
                            <fmt:message key="transfer.err.penalty.bill.intersect"/>
                        </c:when>
                        <c:when test="${ERROR_MSG eq Message.DUE_DATE_TOO_CLOSE}">
                            <fmt:message key="bills.new.err.due.date"/>
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
