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
                <br>
            Home>Catalog>Item>Cart
        </div>

        <div id="content">
            <hr><b>&nbsp;&nbsp;Order</b><hr>
            
            
            <u>Name:</u><br> ${user.firstName} ${user.lastName}<br>
            <br>
            <u>Address: </u><br>
            ${user.address1}
            <c:choose>
                <c:when test="${user.address2 != null}"> 
                    ${user.address2}
                </c:when>
                <c:otherwise></c:otherwise>
            </c:choose><br>
            ${user.city}, ${user.state} ${user.zipCode}<br>
            ${user.country}<br>
            <br>
            
            <div class="tableBoarders" >
                <table>
                    <tr>
                        <th>Image</th>
                        <th>Name</th>
                        <th>Description</th>
                        <th>Price</th>
                        <th>Amount</th>
                        <th>Quantity</th>
                    </tr>
                    
                    <c:forEach var="item" items="${cart.items}">
                            <tr>
                                        <td>
                                            <img src="${item.product.imageURL}" style="width:100px" alt="imageURL">
                                        </td>
                                        <td>${item.product.productName}</td>
                                        <td>${item.product.description}</td>
                                        <td>$${item.product.price}</td>
                                        <td>$${item.total}</td>
                                        <td>${item.quantity}
                                    </td>
                            </tr>
                     </c:forEach>
                </table>
            </div>
                <table>
                            <tr>
                                <td>&nbsp;</td>
                                <td>&nbsp;</td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr>
                                <td>Subtotal:</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${cart != null}">
                                           $${cart.getSubtotal()}
                                        </c:when>
                                        <c:otherwise>
                                            $0
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                   <form action="payment.jsp">
                                        <input type="hidden" name="items" value="${cart}">
                                        <input type="submit" value="Purchase">
                                    </form>
                                </td>
                            </tr>
                            <tr>
                                <td>Tax:</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${cart != null}">
                                           $${cart.getTax()}
                                        </c:when>
                                        <c:otherwise>
                                            $0
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <form action="CatalogController">
                                        <input type="submit" value="Continue Shopping">
                                    </form>
                                </td>
                            </tr>
                            <tr>
                                <td>Total:</td>
                                <td><c:choose>
                                        <c:when test="${cart != null}">
                                          $${cart.getTotalDisplay()}
                                        </c:when>
                                        <c:otherwise>
                                           $0
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>&nbsp;</td>
                            </tr>
                </table>
            <br>
        </div>
    </body>
</html>
