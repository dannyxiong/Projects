/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controllers;


import beans.Product;
import beans.User;
import database.ProductDB;
import database.UserDB;
import java.beans.Statement;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CatalogController extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException 
    {
        String url = "/catalog.jsp";
        ServletContext sc = this.getServletContext();
      
        String productCode=request.getParameter("productCode");
        
        ProductDB db = new ProductDB();
        //UserDB userDB = new UserDB();
        HttpSession session = request.getSession();
        String action = request.getParameter("action");
//        User danny = (User)session.getAttribute("user");
//        if(danny == null)
//        {
//            UserDB userDB = new UserDB();
//            danny = userDB.getUser(1);
//            session.setAttribute("user", danny);
//        }
        if(productCode!=null )
        {
           int code = Integer.parseInt(productCode); // 999
           Product p = db.getProduct(code);
           
           if(p != null ) 
           {
                request.setAttribute("item", p);
                url = "/item.jsp";     
           }
           if(productCode.equals(""))
           {
               url = "/catalog.jsp";
           }
               
        }
        else
        {
            request.setAttribute("list",db.getAllProducts()); 
        }
        RequestDispatcher rd = sc.getRequestDispatcher(url);
        rd.forward(request, response);
        processRequest(request, response);   
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            
        }
    }
}
