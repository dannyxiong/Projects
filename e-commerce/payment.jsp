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
            <hr><b>&nbsp;&nbsp;Payment</b><hr>
            <h1>Enter Your Payment Information</h1>
            <form action="OrderController">
                
                <input type="hidden" name="action" value="confirmOrder">
                <table style="width:35%">
                    <tr>
                        <td style="padding: 10px;">Credit Card Type:</td>
                        <td>
                            <select name="cardType">
                                    <option value="Visa">Visa</option>
                                    <option value="MasterCard">MasterCard</option>
                                    <option value="Discover">Discover</option>
                                    <option value="American Express">American Express</option>
                            </select>
                        </td>

                    <tr>
                        <td style="padding: 10px;">Card Number:</td>
                        <td>
                            <input type="text" name="creditCardNumber" maxlength="16">                            
                        </td>

                    </tr>
                    <tr>
                        <td style="padding: 10px;">Expiration Date(MM/YYYY):</td>
                        <td><select name="expirationMonth">
                                                    <option value=''>Month</option>
                                                    <option value='01'>January</option>
                                                    <option value='02'>February</option>
                                                    <option value='03'>March</option>
                                                    <option value='04'>April</option>
                                                    <option value='05'>May</option>
                                                    <option value='06'>June</option>
                                                    <option value='07'>July</option>
                                                    <option value='08'>August</option>
                                                    <option value='09'>September</option>
                                                    <option value='10'>October</option>
                                                    <option value='11'>November</option>
                                                    <option value='12'>December</option>
                                      </select>
                                      <select name="expirationYear">
                                                    <option value=''>Year</option>
                                                    <option value='2014'>2014</option>
                                                    <option value='2015'>2015</option>
                                                    <option value='2016'>2016</option>
                                                    <option value='2017'>2017</option>
                                                    <option value='2018'>2018</option>
                                                    <option value='2019'>2019</option>
                                                    <option value='2020'>2020</option>
                                                    <option value='2021'>2021</option>
                                                    <option value='2022'>2022</option>
                                      </select></td>
                    </tr>

                    <tr>
                        <td style="padding: 10px;">CVV(3-Digit): </td>
                        <td> 
                            <input name="CVV" id="CVV" maxlength="3"/>
                        </td>
                    </tr>
                    <tr>
                        <td style="padding: 10px;">Your card will be charged a total of:</td>
                        <td>
                            <font size="5">$${cart.getTotalDisplay()}</font>
                        </td>

                    </tr>   
                    <tr>
                        <td></td>
                        <td>
                            <input type="submit" value="Confirm Payment">
                        </td>

                    </tr>
                </table>
                
            </form>
        </div>
    </body>
</html>
