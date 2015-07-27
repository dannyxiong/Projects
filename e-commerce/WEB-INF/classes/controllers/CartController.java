package controllers;

import beans.Cart;
import beans.OrderItem;
import beans.Product;
import beans.User;
import database.ProductDB;
import database.UserDB;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;


public class CartController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        
        String url = "/shoppingCart.jsp";
        ServletContext sc = getServletContext();
        
       HttpSession session = request.getSession();
        
        // get current action
        String action = request.getParameter("action");
//        User danny = (User)session.getAttribute("user");
//        if(danny == null)
//        {
//            UserDB userDB = new UserDB();
//            danny = userDB.getUser(1);
//            session.setAttribute("user", danny);
//        }
        if (action == null) {
            action = "cart";  // default action
        }

        if (action.equals("cart")) {

            String productCode = request.getParameter("productCode");//Gets item.jsp - Add To Cart
            String quantityString = request.getParameter("quantity");
              
            int quantity;
            try {
                quantity = Integer.parseInt(quantityString);
                if (quantity < 0) {
                    quantity = 1;
                }
            } catch (NumberFormatException nfe) {
                quantity = 1;
            }
            
           
            

            
            
            Cart c = (Cart)session.getAttribute("cart");
            if (c == null) {
                c = new Cart();
            }
            
            int code = Integer.parseInt(productCode);
            ProductDB db = new ProductDB();
            Product p = db.getProduct(code);
            
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(p);
            orderItem.setQuantity(quantity);
           
            
            
            if (quantity > 0) {
                c.addItem(orderItem);
            } else if (quantity == 0) {
                c.removeItem(orderItem);
            }
            
            
            session.setAttribute("cart", c);
            url = "/shoppingCart.jsp";
        }
        else if (action.equals("checkout")) {
            url = "/order.jsp";
        }
        sc.getRequestDispatcher(url)
                .forward(request, response);
    }
    
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }    
}