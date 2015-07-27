/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import beans.Order;
import beans.OrderItem;
import beans.Product;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Danny
 */
public class OrderDB {
    
    
    public OrderDB() {
 
    }

    public boolean addOrder(Order order) {
        int ordernumber = 0;  
        Connection connection = DbAdmin.getConnection();
        PreparedStatement ps;
        // insert the new row into the table
        try {
            ps = connection.prepareStatement("INSERT INTO USER_ORDER (orderDate, userID, taxRate, totalCost, paid) VALUES (CURRENT_TIMESTAMP, ?, ?, ?, ?); ");
            //ps.setDate(1, order.getDate());
            ps.setInt(1, order.getUserID());
            ps.setDouble(2, order.getTaxRate());
            ps.setDouble(3, order.getTotalCost());
            ps.setBoolean(4, order.getPaid());
            
            ps.executeUpdate();
            
            
            ps = connection.prepareStatement("SELECT LAST_INSERT_ID();");
            ps.clearParameters();
            
            ResultSet result = ps.executeQuery();
                   
            if(result.next())
            {
                ordernumber = result.getInt(1);
            }
            
            
            if(ordernumber > 0){
                for (OrderItem orderItem : order.getItems()) {
                        orderItem.setOrderNumber(ordernumber);

                        ps.clearParameters();

                        ps = connection.prepareStatement("INSERT INTO ORDER_ITEM (orderNumber, productCode, quantity) "
                                                         + "VALUES (?, ?, ?); ");
                        ps.setInt(1, orderItem.getOrderNumber());
                        ps.setInt(2, orderItem.getProduct().getProductCode());
                        ps.setInt(3, orderItem.getQuantity());

                        ps.executeUpdate();


                }
                return true;
            }
            else{
                return false;
            }
            
        } catch (SQLException se) {
            if (((se.getErrorCode() == 30000) && ("23505".equals(se.getSQLState())))) {
                System.out.println("ERROR: Could not insert record into PRODUCT; dup primary key: " + order.getOrderNumber());
            } else {
                System.out.println("ERROR: Could not add row to PRODUCT table: " + order.getOrderNumber() + " " + se.getCause());
            }
            return false;
        } catch (Exception e) {
            System.out.println("ERROR: Could not add row to PRODUCT table: " + order.getOrderNumber());
            return false;
        }
 
        //System.out.println("Added product to PRODUCT table: " + product.getProductCode());

        // return the  product object
        
    }

    public ArrayList<Order> getAllOrders() {
        ArrayList<Order> orders = new ArrayList<Order>();

        Statement statement = DbAdmin.getNewStatement();
        ResultSet resultSet = null;

        int orderNumber = 0;
        String orderDate = "";
        int userID = 0;
        double taxRate = 0.00;
        double totalCost = 0.00;
        boolean paid = false;

        try {

            resultSet = statement.executeQuery(
                    "SELECT * FROM USER_ORDER");
            while (resultSet.next()) {
                orderNumber = resultSet.getInt("orderNumber");
                orderDate = resultSet.getString("orderDate");
                //categoryCode = resultSet.getInt("categoryCode");
                userID = resultSet.getInt("userID");
                taxRate = resultSet.getDouble("taxRate");
                totalCost = resultSet.getDouble("totalCost");
                paid = resultSet.getBoolean("paid");

                Order order = new Order (orderNumber, orderDate, userID, taxRate, totalCost, paid);
                orders.add(order);
                System.out.println("Found order in ORDER_PURCHASE table: " + orderNumber);
            }
        } catch (SQLException se) {
            System.out.println("ERROR: Could not execute SQL statement in: " + "ProductDB.getAllProducts()");
            System.out.println("ERROR: Could not execute SQL statement: " + se);
            return null;
        }
 
        return orders;
    }
    
    public ArrayList<Order> getAllOrdersByUser(int userID) {
        ArrayList<Order> orders = new ArrayList<Order>();

        

        int orderNumber = 0;
        String orderDate = "";
        double taxRate = 0.00;
        double totalCost = 0.00;
        boolean paid = false;

        try {
            Connection connection = DbAdmin.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM USER_ORDER WHERE userID = ? ;");
            ps.setInt(1, userID);
            ResultSet resultSet = ps.executeQuery();
            
            while (resultSet.next()) {
                
                orderNumber = resultSet.getInt("orderNumber");
                orderDate = resultSet.getString("orderDate");
                //categoryCode = resultSet.getInt("categoryCode");
                userID = resultSet.getInt("userID");
                taxRate = resultSet.getDouble("taxRate");
                totalCost = resultSet.getDouble("totalCost");
                paid = resultSet.getBoolean("paid");

                Order order = new Order (orderNumber, orderDate, userID, taxRate, totalCost, paid);
                orders.add(order);
                System.out.println("Found order in ORDER_PURCHASE table: " + orderNumber);
            }
        } catch (SQLException se) {
            System.out.println("ERROR: Could not execute SQL statement in: " + "ProductDB.getAllProducts()");
            System.out.println("ERROR: Could not execute SQL statement: " + se);
            return null;
        }
 
        return orders;
    }
}
