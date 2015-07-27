/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import beans.Cart;
import beans.Order;
import beans.OrderItem;
import beans.Product;
import beans.User;
import database.OrderDB;
import database.UserDB;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.sql.Date;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Danny
 */
public class OrderController extends HttpServlet {
    

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String url = "/order.jsp";
        ServletContext sc = getServletContext();
        
        HttpSession session = request.getSession();
        
       
        String action = request.getParameter("action");
        User user = (User)session.getAttribute("user");
//        
//        User danny = (User)session.getAttribute("user");
//        if(danny == null)
//        {
//            UserDB userDB = new UserDB();
//            danny = userDB.getUser(1);
//            session.setAttribute("user", danny);
//        }
        if(action.equals("confirmOrder") && user != null){ 
            
            
//            String cardType = request.getParameter("cardType");
//            String creditCardNumber = request.getParameter("creditCardNumber");
//            String expirationMonth = request.getParameter("expirationMonth");
//            String expirationYear = request.getParameter("expirationYear");
//            String cvv = request.getParameter("CVV");
//            
            
            
            Cart c = (Cart)session.getAttribute("cart");
            
            
              
           

            Order order = new Order();
            OrderDB oDB = new OrderDB();


            //order.setDate(date);
            order.setUserID(user.getUserID());
            order.setTaxRate(c.getOrderTax());
            order.setTotalCost(c.getTotal());
            order.setPaid(true);
            order.setItems(c.getItems());

            
            boolean savedToOrderTable = oDB.addOrder(order);// add order to USER_ORDER table in MYSQL
            
            if(savedToOrderTable){
                 url = "/invoice.jsp";
            }
            else{
                url="/orderfail.jsp";
            }
                
                
                
            
//            int code;
//            code = item.getProduct().getProductCode();
//            int quantity = item.getQuantity();
//        
//            OrderDB oDB = new OrderDB();
//            Order order = new Order(4433,date,"hello",432.04,.042,true);
//            
//            oDB.addOrder(order);
        }
        else if(user == null){
            url="/catalog.jsp";
        }
        if(action.equals("viewOrders")){
      
            
            if(user != null){
                
                OrderDB oDB = new OrderDB();
                
                ArrayList<Order> orders = oDB.getAllOrdersByUser(user.getUserID());
                
                
                session.setAttribute("theOrders", orders);
                
                url="/orderlist.jsp"; 
            }
            
        }
        sc.getRequestDispatcher(url)
                .forward(request, response);
        
        
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);  
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
        
        
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
