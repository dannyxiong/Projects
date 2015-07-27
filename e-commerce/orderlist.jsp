<%-- 
    Document   : payment
    Created on : Nov 25, 2014, 12:18:15 PM
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
            <hr><b>&nbsp;&nbsp;Order List</b><hr>
            <h1>Order List</h1>
            <br>
            <br>
            <div class="tableBoarders">
                <table style="width:35%">
                        <tr>
                            <th>Order Number</th>
                            <th>User ID</th>
                            <th>Order Date</th>
                            <th>Total</th>
                        </tr>

                    <c:forEach var="order" items="${theOrders}">
                        <tr>
                            <td>${order.orderNumber}</td>
                            <td>${order.userID}</td>
                            <td>${order.orderDate}</td>
                            <td>$${order.totalCost}</td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
        </div>
    </body>
</html>