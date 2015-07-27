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
            <hr><b>&nbsp;&nbsp;Shopping Cart</b><hr>
            <br>
            <div class="tableBoarders">
                <table>
                    <tr>
                        <th>Image</th>
                        <th>Name</th>
                        <th>Description</th>
                        <th>Price</th>
                        <th>Amount</th>
                        <th>Quantity</th>
                        <th>&nbsp;</th>
                    </tr>
                    <c:choose>
                        <c:when test="${cart != null}">
                            <c:forEach var="item" items="${cart.items}">
                                <tr>
                                      <td><img src="${item.product.imageURL}" style="width:100px" alt="imageURL">
                                      <td>${item.product.productName}
                                      <td>${item.product.description}
                                      <td>$${item.product.price}
                                      <td>$${item.total}

                                      <td> 

                                            <form action="" method="post">
                                              <input type="hidden" name="productCode" value="${item.product.productCode}">
                                              <input type="text" name="quantity" value='${item.quantity}' >
                                              <input type="submit" value="Update">
                                            </form>

                                      </td>
                                      <td>
                                          <form action="cart" >
                                                <input type="hidden" name="quantity" value="0"/>
                                                <input type="hidden" name="productCode" value="${item.product.productCode}"/>
                                                <input type="submit"  value="Remove"/>                           
                                          </form>
                                          <!--<a href="cart?productCode=${item.product.productCode}&amp;quantity=0">Remove</a>-->
                                      </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td>&nbsp;</td>
                                <td>&nbsp;</td>
                                <td>&nbsp;</td>
                                <td>&nbsp;</td>
                                <td>&nbsp;</td>
                                <td>&nbsp;</td>
                                <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </table>
             </div>
            <br>
            <center>
            <c:choose>
                <c:when test="${cart != null}">
                    Subtotal: $${cart.getSubtotal()}
                </c:when>
                <c:otherwise>
                    Subtotal: $0
                </c:otherwise>
            </c:choose>
            <br>
            <br>
            <a href="order.jsp" ><button>Checkout</button></a> 
            <a href="CatalogController"><button>Continue Shopping</button></a></center>
            <br>
            

            
            
           
            
            <!--<form action="order.jsp">
                <input type="submit" value="Checkout">
            </form>
            
            <form action="CatalogController">
                <input type="submit" value="Continue Shopping">
            </form>
            -->
        </div>
    </body>
</html>
