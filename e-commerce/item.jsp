<%@page import="beans.Product"%>
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="header.jsp" %>    
<%@include file="site-navigation.jsp" %>
<%@include file="user-navigation.jsp" %>
<%@include file="footer.jsp" %>

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
            <hr><b>&nbsp;&nbsp;Item: ${item.productName}</b><hr>
            <p class="padding2">
                <br>
                    <img src="${item.imageURL}" style="width:175px" alt="imageURL">
                <br>
                    <u>Brief Description:</u><br>
                    ${item.description}
                <br>
            </p>
            <br>
            
            
            <br>
            
            <center>
            Price: $${item.price}
            
           
            
            <a href="CatalogController"><button>Back</button></a> 
            <a href="cart?productCode=${item.productCode} "><button>Add To Cart</button></a>
            </center>
            
            <!--<form action="cart" method="post"> 
                <input type="hidden" name="productCode" value="${item.productCode}">
                <input type="submit" value="Add To Cart">
            </form>
            
            <form action="CatalogController">
               <input type="submit" value="Back">
            </form>
            -->
        </div>

    </body>
</html>