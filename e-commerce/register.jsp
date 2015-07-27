<%@page contentType="text/html" pageEncoding="utf-8"%>
<%@include file ="/header.jsp"%>
<%@include file ="/user-navigation.jsp"%>
<%@include file ="/site-navigation.jsp"%>
<%@include file ="/footer.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<%
    
%>
<html>
    <head>
        <title>
            Danny Xiong
            ITCS 4166
        </title>
		
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="styles/Main.css">
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
                Home    
        </div>
 
        <div id="content">
                <hr><b>&nbsp;&nbsp;Register</b><hr>
              
                <form action="UserController">
                    <input type="hidden" name="action" value="register" />
                    Username: <input type="text" style="width:70px;" name="username" />
                    Password: <input type="password" style="width:70px;" name="password" /><br><br>
                    
                    First Name: <input type="text" style="width:75px;" name="firstName" />
                    Last Name: <input type="text" style="width:75px;" name="lastName" /><br>
                    
                    Email: <input type="text" name="email" /><br>
                    Address1: <input type="text" name="address1" /><br>
                    Address2: <input type="text" name="address2" /><br>
                    City: <input type="text" style="width:80px;" name="city" />
                    State: <input type="text" style="width:30px;" name="state" />
                    Zipcode: <input type="text" style="width:60px;" name="zipcode" /><br>
                    Country: <input type="text" style="width:60px;" name="country" /><br><br>
                            <input type="submit" value="Sign up"/>
                </form>
                
                <br>
        </div>

    </body>
</html>

        