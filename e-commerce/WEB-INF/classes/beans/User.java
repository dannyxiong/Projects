/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//@Entity
public class User implements Serializable {
    
    //@Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private int UserID;
    private String FirstName;
    private String LastName;
    private String Email;
    private String Address1;
    private String Address2;
    private String City;
    private String State;
    private String ZipCode;
    private String Country;
    private String Username;
    private String Password;

    public User(int userID, String firstName, String lastName, String email, String address1, String address2, String city, String state, String zipcode, String country, String username, String password) {
        this.UserID = userID;
        this.FirstName = firstName;
        this.LastName = lastName;
        this.Email = email;
        this.Address1 = address1;
        this.Address2 = address2;
        this.City = city;
        this.State = state;
        this.ZipCode = zipcode;
        this.Country = country;
        this.Username = username;
        this.Password = password;
    }

    public User() {
       //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the UserID
     */
    public int getUserID() {
        return UserID;
    }

    /**
     * @param UserID the UserID to set
     */
    public void setUserID(int UserID) {
        this.UserID = UserID;
    }

    /**
     * @return the FirstName
     */
    public String getFirstName() {
        return FirstName;
    }

    /**
     * @param FirstName the FirstName to set
     */
    public void setFirstName(String FirstName) {
        this.FirstName = FirstName;
    }

    /**
     * @return the LastName
     */
    public String getLastName() {
        return LastName;
    }

    /**
     * @param LastName the LastName to set
     */
    public void setLastName(String LastName) {
        this.LastName = LastName;
    }

    /**
     * @return the Email
     */
    public String getEmail() {
        return Email;
    }

    /**
     * @param Email the Email to set
     */
    public void setEmail(String Email) {
        this.Email = Email;
    }

    /**
     * @return the Address1
     */
    public String getAddress1() {
        return Address1;
    }

    /**
     * @param Address1 the Address1 to set
     */
    public void setAddress1(String Address1) {
        this.Address1 = Address1;
    }

    /**
     * @return the Address2
     */
    public String getAddress2() {
        return Address2;
    }

    /**
     * @param Address2 the Address2 to set
     */
    public void setAddress2(String Address2) {
        this.Address2 = Address2;
    }

    /**
     * @return the City
     */
    public String getCity() {
        return City;
    }

    /**
     * @param City the City to set
     */
    public void setCity(String City) {
        this.City = City;
    }

    /**
     * @return the State
     */
    public String getState() {
        return State;
    }

    /**
     * @param State the State to set
     */
    public void setState(String State) {
        this.State = State;
    }

    /**
     * @return the ZipCode
     */
    public String getZipCode() {
        return ZipCode;
    }

    /**
     * @param ZipCode the ZipCode to set
     */
    public void setZipCode(String ZipCode) {
        this.ZipCode = ZipCode;
    }

    /**
     * @return the Country
     */
    public String getCountry() {
        return Country;
    }

    /**
     * @param Country the Country to set
     */
    public void setCountry(String Country) {
        this.Country = Country;
    }

    /**
     * @return the Username
     */
    public String getUsername() {
        return Username;
    }

    /**
     * @param Username the Username to set
     */
    public void setUsername(String Username) {
        this.Username = Username;
    }

    /**
     * @return the Password
     */
    public String getPassword() {
        return Password;
    }

    /**
     * @param Password the Password to set
     */
    public void setPassword(String Password) {
        this.Password = Password;
    }
    

}
