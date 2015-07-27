/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import database.DbAdmin;
import beans.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ProductDB {

 
    
    public ProductDB() {
 
    }
            
    public void createProductTable() {

        Statement statement = DbAdmin.getNewStatement();

        try {

            statement.execute("CREATE TABLE product("
                    + "productCode INT,productName VARCHAR(50),"
                    + "categoryCode INT,catalogCategory VARCHAR(50),"
                    + "price decimal(7,2),description VARCHAR(100),imageUrl VARCHAR(50),"
                    + "PRIMARY KEY (productCode))");
            System.out.println("Created a new table: " + "PRODUCT");
        } catch (SQLException se) {
            if (se.getErrorCode() == 30000 && "X0Y32".equals(se.getSQLState())) {
                // we got the expected exception when the table is already there
            } else {
                // if the error code or SQLState is different, we have an unexpected exception
                System.out.println("ERROR: Could not create PRODUCT table: " + se);
            }
        }
    }

    public Product addProduct(Product product) {

        Connection connection = DbAdmin.getConnection();
        PreparedStatement ps;
        // insert the new row into the table
        try {
            ps = connection.prepareStatement("INSERT INTO product VALUES (?, ?, ?, ?, ?, ?)");
            ps.setInt(1, product.getProductCode());
            ps.setString(2, product.getProductName());
            //ps.setInt(3, product.getCategoryCode());
            ps.setString(3, product.getCatalogCategory());
            ps.setString(4, product.getDescription());
            ps.setDouble(5, product.getPrice());
            ps.setString(6, product.getImageURL());

            ps.executeUpdate();

        } catch (SQLException se) {
            if (((se.getErrorCode() == 30000) && ("23505".equals(se.getSQLState())))) {
                System.out.println("ERROR: Could not insert record into PRODUCT; dup primary key: " + product.getProductCode());
            } else {
                System.out.println("ERROR: Could not add row to PRODUCT table: " + product.getProductCode() + " " + se.getCause());
            }
            return null;
        } catch (Exception e) {
            System.out.println("ERROR: Could not add row to PRODUCT table: " + product.getProductCode());
            return null;
        }
 
        System.out.println("Added product to PRODUCT table: " + product.getProductCode());

        // return the  product object
        return product;
    }

    public Product getProduct(int pcode) {

        Product product = null; // We don't know if the product exist yet.
      
        String query = "SELECT productName, catalogCategory, price, description, imageUrl"
                + " from PRODUCT where PRODUCT.productCode =  " + pcode;
        Statement statement = DbAdmin.getNewStatement();
        ResultSet resultSet = null;

        try {
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
   
                product = new Product(); // NOw we know the product exist; thus, populate it it.
                product.setProductCode(pcode);
                product.setProductName(resultSet.getString("productName"));
                //product.setCategoryCode(resultSet.getInt("categoryCode"));
                product.setCatalogCategory(resultSet.getString("catalogCategory"));
                product.setPrice(resultSet.getDouble("price"));
                product.setDescription(resultSet.getString("description"));
                product.setImageURL(resultSet.getString("imageURL"));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
 
        return product;

    }

    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> products = new ArrayList<Product>();

        Statement statement = DbAdmin.getNewStatement();
        ResultSet resultSet = null;

        int productCode = 0;
        String productName = "";
        //int categoryCode = 0;
        String catalogCategory = "";
        double price = 0.00;
        String description = "";
        String imageURL = "";

        try {

            resultSet = statement.executeQuery(
                    "SELECT productCode, productName, catalogCategory, price, description, imageURL FROM product ORDER BY productCode");
            while (resultSet.next()) {
                productCode = resultSet.getInt("productCode");
                productName = resultSet.getString("productName");
                //categoryCode = resultSet.getInt("categoryCode");
                catalogCategory = resultSet.getString("catalogCategory");
                price = resultSet.getDouble("price");
                description = resultSet.getString("description");
                imageURL = resultSet.getString("imageURL");

                Product product = new Product(productCode, productName, catalogCategory, price, description, imageURL);
                products.add(product);
                System.out.println("Found product in PRODUCT table: " + productCode);
            }
        } catch (SQLException se) {
            System.out.println("ERROR: Could not execute SQL statement in: " + "ProductDB.getAllProducts()");
            System.out.println("ERROR: Could not execute SQL statement: " + se);
            return null;
        }
 
        return products;
    }

}
