<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*, java.text.*, com.epam.rd.jsp.currencies.CurrenciesOfCurrentTestCase" %>

<jsp:useBean id="currencies" class="com.epam.rd.jsp.currencies.CurrenciesOfCurrentTestCase" scope="request"/>

<h1>Exchange Rates for ${param.from}</h1>
<ul>
    <c:forEach var="rate" items="${currencies.getExchangeRates(param.from)}">
        <li>1 ${param.from} = ${rate.getKey()} ${rate.getValue()}</li>
    </c:forEach>
</ul>