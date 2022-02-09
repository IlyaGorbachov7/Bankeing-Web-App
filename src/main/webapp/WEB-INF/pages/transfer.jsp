<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="by.epam.baranovsky.banking.constant.DBMetadata" %>
<%@ page import="by.epam.baranovsky.banking.constant.Message" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale.locale"/>
<html>
<head>
    <title><fmt:message key="transfer.title" /></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/info.css"/>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
    <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
    <script>
        $(document).ready(function() {
            $('.select-source').select2();
        });
    </script>
    <script>
        $(document).ready(function() {
            var $selectCardOrAcc = $("select#card-or-acc");
            var $selectSource = $("select#select-source");
            var $sPclone = $selectSource.clone();

            $("#card-or-acc").change(function () {
                var sourceName = $selectCardOrAcc.find("option:selected").text();
                $selectSource.removeAttr('disabled');
                $selectSource.find("optgroup").remove();
                $selectSource.append($sPclone.find("optgroup[label='" + sourceName + "']").clone());
                $selectSource.attr('name',$selectCardOrAcc.find("option:selected").val())
            });
        });
    </script>
    <script>
        $(document).ready(function (){
            var targerCardOrAcc = $("select#target-card-or-acc");
            $("#target-card-or-acc").change(function () {
                var targetName = targerCardOrAcc.find("option:selected").val();

                if(targetName.localeCompare("card")){
                    document.getElementById("target-account").removeAttribute('hidden');
                    document.getElementById("input-cexp").setAttribute('value',"");
                    document.getElementById("input-cnum").setAttribute('value',"");
                    document.getElementById("target-card").setAttribute('hidden', true);
                }
                if(targetName.localeCompare("acc")){
                    document.getElementById("target-card").removeAttribute('hidden');
                    document.getElementById("input-anum").setAttribute('value',"");
                    document.getElementById("target-account").setAttribute('hidden', true);
                }

                document.getElementById("submit").removeAttribute('hidden');

            });
        });
    </script>
</head>
<body>
<div class="container">
    <div class="formContainer">
        <h3><fmt:message key="transfer.title"/>:</h3>
        <br>
        <div class="whitebox">
            <form action="controller" method="post">
                <input type="hidden" name="command" value="go_to_transfer_confirm_page"/>
                <input type="hidden" name="bill_id" value="${BILL_ID}"/>
                <input type="hidden" name="penalty_id" value="${PENALTY_ID}"/>
                <table class="blankTable">
                    <tr>
                        <td>
                            <select id="card-or-acc">
                                <option disabled selected value> <fmt:message key="transfer.from"/>: </option>
                                <option value="card_id"><fmt:message key="transfer.card"/></option>
                                <option value="account_id"><fmt:message key="transfer.acc"/></option>
                            </select>
                        </td>
                        <td colspan="3">
                            <select id="select-source" class="select-source" required disabled>
                                <option disabled selected value> <fmt:message key="transfer.select"/> </option>
                                <optgroup label="<fmt:message key="transfer.card"/>">
                                    <c:forEach var="card" items="${USER_CARDS}">
                                        <option value="${card.value.id}">
                                            <fmt:message key="transfer.card"/> ${card.value.number} <fmt:message key="transfer.balance"/>: ${card.key}
                                        </option>
                                    </c:forEach>
                                </optgroup>
                                <optgroup label="<fmt:message key="transfer.acc"/>">
                                    <c:forEach var="acc" items="${USER_ACCOUNTS}">
                                        <option value="${acc.value.id}">
                                            <fmt:message key="transfer.acc"/> ${acc.value.accountNumber} <fmt:message key="transfer.balance"/>: ${acc.key}
                                        </option>
                                    </c:forEach>
                                </optgroup>
                            </select>
                        </td>
                    </tr>
                    <tr><td></td></tr>
                    <tr>
                        <td><b><fmt:message key="operations.value"/>:</b></td>
                        <td colspan="2">
                            <c:choose>
                                <c:when test="${not empty ACCOUNT_DATA}">
                                    <input type="number" min="0" name="transfer_value" value="${TRANSFER_VALUE}" readonly required/>
                                </c:when>
                                <c:otherwise>
                                    <input type="number" min="0" name="transfer_value" step=".01" required/>
                                </c:otherwise>
                            </c:choose>
                            </td>
                    </tr>
                    <tr><td></td></tr>
                    <tr>
                        <td><b><fmt:message key="transfer.target"/>:</b> </td>
                        <c:choose>
                            <c:when test="${not empty ACCOUNT_DATA}">
                                <td>
                                    <select id="target-card-or-acc" disabled required>
                                        <option value="card"><fmt:message key="transfer.card"/></option>
                                        <option selected value="acc"><fmt:message key="transfer.acc"/></option>
                                    </select>
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td>
                                    <select id="target-card-or-acc"  required>
                                        <option disabled selected value> <fmt:message key="transfer.select"/> </option>
                                        <option value="card"><fmt:message key="transfer.card"/></option>
                                        <option value="acc"><fmt:message key="transfer.acc"/></option>
                                    </select>
                                </td>
                            </c:otherwise>
                        </c:choose>

                    </tr>
                    <tr><td></td></tr>
                    <tr id="target-card" hidden>
                        <td></td>
                        <td>
                            <b><fmt:message key="transfer.card.num"/>:</b>
                        </td>
                        <td colspan="2">
                            <input id="input-cnum" type="text" name="target_card_number" placeholder="<fmt:message key="transfer.card.num.pattern"/>" maxlength="16" pattern="\d{16}"/>
                            <input id="input-cexp" type="text" name="target_card_expiration" placeholder="<fmt:message key="transfer.card.expire"/>" pattern="\d{4}\-\d{2}" />
                        </td>
                    </tr>
                    <c:choose>
                        <c:when test="${not empty ACCOUNT_DATA}">
                            <tr id="target-account">
                                <td></td>
                                <td>
                                    <b><fmt:message key="transfer.acc.num"/>:</b>
                                </td>
                                <td colspan="2">
                                    <input id="input-anum" readonly type="text" name="target_account_number" placeholder="<fmt:message key="transfer.acc.num.pattern"/>" pattern="[A-Z]{2}\d{18}" maxlength="20" value="${ACCOUNT_DATA.accountNumber}" style="width: 80%"/>
                                </td>
                            </tr>
                            <tr>
                                <td></td>
                                <td colspan="2">
                                    <input id="submit" type="submit" value="<fmt:message key="logination.submit"/>"/>
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <tr id="target-account" hidden>
                                <td></td>
                                <td>
                                    <b><fmt:message key="transfer.acc.num"/>:</b>
                                </td>
                                <td colspan="2">
                                    <input id="input-anum" type="text" name="target_account_number" placeholder="<fmt:message key="transfer.acc.num.pattern"/>" pattern="[A-Z]{2}\d{18}" maxlength="20" value="${ACCOUNT_DATA.accountNumber}" style="width: 80%"/>
                                </td>
                            </tr>
                            <tr>
                                <td></td>
                                <td colspan="2">
                                    <input hidden id="submit" type="submit" value="<fmt:message key="logination.submit"/>"/>
                                </td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </table>

            </form>
            <br>
            <c:if test="${ not empty ERROR_MSG}">
                <i class="errorMsg">
                    <fmt:message key="logination.error"/>:
                    <c:choose>
                        <c:when test="${ERROR_MSG eq Message.OPERATION_ILLEGAL}">
                            <fmt:message key="transfer.err.illegal"/>
                        </c:when>
                        <c:when test="${ERROR_MSG eq Message.OPERATION_INVALID_VALUE}">
                            <fmt:message key="transfer.err.invalid.value"/>
                        </c:when>
                        <c:when test="${ERROR_MSG eq Message.OPERATION_NOT_ENOUGH_DATA}">
                                <fmt:message key="transfer.err.no.data"/>
                        </c:when>
                        <c:when test="${ERROR_MSG eq Message.NO_SUCH_RECEIVER}">
                                <fmt:message key="transfer.err.no.recipient"/>
                        </c:when>
                        <c:when test="${ERROR_MSG eq Message.PENALTY_BILL_INTERSECTION}">
                            <fmt:message key="transfer.err.penalty.bill.intersect"/>
                        </c:when>
                        <c:otherwise>
                                <fmt:message key="error.unknown"/>
                        </c:otherwise>
                    </c:choose>
                </i>
            </c:if>

        </div>
        <br>
        <a href="${PREV_PAGE}">
            <fmt:message key="card.info.back"/>
        </a>
    </div>
</div>
</body>
</html>
