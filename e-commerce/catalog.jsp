<%@page contentType="text/html" pageEncoding="utf-8"%>
<%@include file ="/header.jsp"%>
<%@include file ="/user-navigation.jsp"%>
<%@include file ="/site-navigation.jsp"%>
<%@include file ="/footer.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="beans.Product"%>
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
                Home>Catalog
        </div>
        
        <div id="content">
            <hr><b>&nbsp;&nbsp;Catalog</b><hr>
            
                
            <table>
                <tr>
                    <th bgcolor=teal; style='padding: 10px'><h2>Reels</h2></th>
                    <td style='padding: 15px; border: 1px solid green;'>
                        <a href="CatalogController?productCode=101">
                                Shimano Stella FI
                        </a>
                        <br>
                        <a href="CatalogController?productCode=201">
                                Penn Spinfisher V
                        </a>
                        <br>
                        <a href="CatalogController?productCode=301">
                                Daiwa Saltiga
                        </a>
                    </td>
                </tr>
                <tr>
                    <th bgcolor=grey style='padding: 10px'><h2>Rods</h2></th>
                    <td style='padding: 15px; border: 1px solid grey;'>
                        <a href="CatalogController?productCode=401">
                                St Croix Triumph
                        </a>
                        <br>
                        <a href="CatalogController?productCode=501">
                                Abu Garcia Villain
                        </a>
                        <br>
                        <a href="CatalogController?productCode=601">
                                Daiwa Zillion
                        </a>


                    </td>
                </tr>
            </table>           
        </div>                                
    </body>
</html>
