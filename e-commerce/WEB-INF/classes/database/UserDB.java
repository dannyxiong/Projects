package database;


import beans.User;
import java.sql.*;
import java.util.*;
import javax.servlet.*;

public class UserDB {

    public UserDB() {
    
    }
// This class handles the USER table.
    public void createUserTable() {

        Statement statement = DbAdmin.getNewStatement();

        try {
            statement.execute("CREATE TABLE users("
                    + "userID VARCHAR(50),firstName VARCHAR(50),"
                    + "lastName VARCHAR(50), emailAddr VARCHAR(50),"
                    + "address1 VARCHAR(50), address2 VARCHAR(50),"
                    + "city VARCHAR(50),state VARCHAR(50),zipcode VARCHAR(50),"
                    + "country VARCHAR(50),"
                    + "username VARCHAR(255),"
                    + "password VARCHAR(255),"
                    + "PRIMARY KEY (userID))");

            System.out.println("Created a new table: " + "USER");
        } catch (SQLException se) {
            if (se.getErrorCode() == 30000 && "X0Y32".equals(se.getSQLState())) {
                // we got the expected exception when the table is already there
            } else {
                // if the error code or SQLState is different, we have an unexpected exception
                System.out.println("ERROR: Could not create USER table: " + se);
            }
        }
  
    }

    public void addUser(User user) {

        Connection connection = DbAdmin.getConnection();
        PreparedStatement ps;
        // insert the new row into the table
        try {
             
            ps = connection.prepareStatement("INSERT INTO USER "
                    + "(FirstName,LastName,Email,Address1,Address2,City,State,ZipCode,Country,Username,Password) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"); //Gets the userID from the db
            //ps.setString(1, user.getUserID());
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getAddress1());
            ps.setString(5, user.getAddress2());
            ps.setString(6, user.getCity());
            ps.setString(7, user.getState());
            ps.setString(8, user.getZipCode());
            ps.setString(9, user.getCountry());
            ps.setString(10, user.getUsername());
            ps.setString(11, user.getPassword());
            
            ps.executeUpdate();
            
            ps = connection.prepareStatement("SELECT LAST_INSERT_ID();");
            ps.clearParameters();
            
            ResultSet result = ps.executeQuery();
            
            if (result.next())
            {
                user.setUserID(result.getInt(1));
            }

        } catch (SQLException se) {
            if (((se.getErrorCode() == 30000) && ("23505".equals(se.getSQLState())))) {
                System.out.println("ERROR: Could not insert record into USER; dup primary key: " + user.getUserID());
            } else {
                System.out.println("ERROR: Could not add row to USER table: " + user.getUserID() + " " + se.getCause());
            }
            
        } catch (Exception e) {
            System.out.println("ERROR: Could not add row to USER table: " + user.getUserID());
           
        }
    
        System.out.println("Added user to USER table: " + user.getUserID());

        // return the  User object
        
    }

    public User getUser(int userID) {

        Statement statement = DbAdmin.getNewStatement();
        ResultSet resultSet = null;
        
        String firstName = "";
        String lastName = "";
        String email = "";
        String address1 = "";
        String address2 = "";
        String city = "";
        String state = "";
        String zipcode = "";
        String country = "";
        String username = "";
        String password = "";
        String query = "";
        
        try {
            // Find the speciic row in the table
            query = "SELECT * FROM user WHERE userID = " + userID;

            resultSet = statement.executeQuery(query);
            if (!resultSet.next()) {
                System.out.println("WARNING: Could not find user in USER table: " + userID);
                return null;
            } else {
                firstName = resultSet.getString("firstName");
                lastName = resultSet.getString("lastName");
                email = resultSet.getString("email");
                address1 = resultSet.getString("address1");
                address2 = resultSet.getString("address2");
                city = resultSet.getString("city");
                state = resultSet.getString("state");
                zipcode = resultSet.getString("zipcode");
                country = resultSet.getString("country");
                username = resultSet.getString("username");
                password = resultSet.getString("password");
                System.out.println("Found user in user table: " + userID);
            }
        } catch (SQLException se) {
            System.out.println("ERROR: Could not exicute SQL statement: " + query);
            System.out.println("SQL error: " + se);
            return null;
        }
   
        return new User(userID, firstName, lastName, email, address1, address2, city, state, zipcode, country, username, password);
    }

    public ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<User>();

        Statement statement = DbAdmin.getNewStatement();
        ResultSet resultSet = null;

        int userID = 0;
        String firstName = "";
        String lastName = "";
        String email = "";
        String address1 = "";
        String address2 = "";
        String city = "";
        String state = "";
        String zipcode = "";
        String country = "";
        String username = "";
        String password = "";
        try {
            // Find the speciic row in the table
            resultSet = statement.executeQuery(
                    "SELECT * FROM user");
            while (resultSet.next()) {
                userID = resultSet.getInt("userID");
                firstName = resultSet.getString("firstName");
                lastName = resultSet.getString("lastName");
                email = resultSet.getString("email");
                address1 = resultSet.getString("address1");
                address2 = resultSet.getString("address2");
                city = resultSet.getString("city");
                state = resultSet.getString("state");
                zipcode = resultSet.getString("zipcode");
                country = resultSet.getString("country");
                username = resultSet.getString("username");
                password = resultSet.getString("password");
                User user = new User(userID, firstName, lastName, email, address1, address2, city, state, zipcode, country, username, password);
                users.add(user);
                System.out.println("Found user in USER table: " + userID);
            }
        } catch (SQLException se) {
            System.out.println("ERROR: Could not exicute SQL statement in: " + "UserDB.getAllUsers()");
            System.out.println("ERROR: Could not exicute SQL statement: " + se);
            return null;
        }
  
        return users;
    }
    
    public User getUser(String username, String password) {
        Connection connection = DbAdmin.getConnection();
        PreparedStatement ps;
        int userID = 0;
        String firstName = "";
        String lastName = "";
        String email = "";
        String address1 = "";
        String address2 = "";
        String city = "";
        String state = "";
        String zipcode = "";
        String country = "";

        String query = "";
        
        try {
            // Find the speciic row in the table
            ps = connection.prepareStatement ("SELECT * FROM user WHERE username = ? and password = ?;") ;
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet resultSet = ps.executeQuery();
            
            if (!resultSet.next()) {
                System.out.println("WARNING: Could not find user in USER table: " + userID);
                return null;
            } else {
                userID = resultSet.getInt("userid");
                firstName = resultSet.getString("firstName");
                lastName = resultSet.getString("lastName");
                email = resultSet.getString("email");
                address1 = resultSet.getString("address1");
                address2 = resultSet.getString("address2");
                city = resultSet.getString("city");
                state = resultSet.getString("state");
                zipcode = resultSet.getString("zipcode");
                country = resultSet.getString("country");
                username = resultSet.getString("username");
                password = resultSet.getString("password");
                
                System.out.println("Found user in user table: " + username);
            }
        } catch (SQLException se) {
            System.out.println("ERROR: Could not exicute SQL statement: " + query);
            System.out.println("SQL error: " + se);
            return null;
        }
   
        return new User(userID, firstName, lastName, email, address1, address2, city, state, zipcode, country, username, password);
    }

}