<%-- 
    Document   : invoice
    Created on : Nov 30, 2014, 10:37:09 AM
    Author     : Danny
--%>

<%@page contentType="text/html" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file ="/header.jsp"%>
<%@include file ="/user-navigation.jsp"%>
<%@include file ="/site-navigation.jsp"%>
<%@include file ="/footer.jsp"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Danny's Bait and Tackle</title>
    </head>
    <body>
        
        <div id="login">
            <c:choose>
                <c:when test="${user != null}">
                ${user.firstName} ${user.lastName} 
                </c:when>
                <c:otherwise>
                        Not signed in.
                </c:otherwise>
            </c:choose>
                <br>
                <br>
                <br> 
                <br>
                <br>
                <br>
                <br>
                <br>
            Home>Catalog>Item>Cart
        </div>

        <div id="content">
            <hr><b>&nbsp;&nbsp;Invoice</b><hr>
            <h1>Invoice</h1>
            <br>
            <br>
            <h2>Paid In Full</h2>
        </div>
    </body>
</html>