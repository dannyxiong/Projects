<%@page contentType="text/html" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file ="/header.jsp"%>
<%@include file ="/user-navigation.jsp"%>
<%@include file ="/site-navigation.jsp"%>
<%@include file ="/footer.jsp"%>
<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<html>
    <head>
        <title>
            Danny Xiong
            ITCS 4166
        </title>
		
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="/styles/Main.css">
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
                <br>Home 
        </div>

        <div id="content">
            <hr>
                <b>&nbsp;&nbsp;Contact Us</b>
            <hr>
            Any question regarding to item details, item specifications, or technical difficulties,<br>
            Please email us at: dxiong4@uncc.edu
            <br>
        </div>

    </body>
</html>
