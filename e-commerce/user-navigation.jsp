<%@page contentType="text/html" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
	<head>
	    <meta charset="utf-8">
        <title>
			Name: Danny Xiong, John Minter
			4166, HW5
		</title>
        <link rel="stylesheet" href="styles/Main.css">
	</head>
	
	<body>
        <div id="userspec">
            <br>
            
            <c:choose>
                <c:when test="${user != null}">
                    <a href="LoginController?action=logout">Logout ${user.username}</a>&nbsp;|&nbsp;
                </c:when>
                <c:otherwise>
                    <a href="login.jsp">Login</a>&nbsp;|&nbsp;
                </c:otherwise>
                
            </c:choose>    
                <a href="shoppingCart.jsp">Cart</a>
                
                &nbsp;|&nbsp;
                
                <a href="OrderController?action=viewOrders">Orders</a>
        </div>
	</body>
</html>
