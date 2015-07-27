/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.util.ArrayList;

/**
 *
 * @author Danny
 */
public class Order {
    private int OrderNumber;
    private String OrderDate;
    private int UserID;
    private ArrayList<OrderItem> items;
    private double TaxRate;
    private double TotalCost;
    private boolean Paid;

    public Order(){
    }
    
    public Order(int OrderNumber, String OrderDate,int UserID,double TaxRate,double TotalCost,boolean Paid){
        this.OrderNumber = OrderNumber;
        this.OrderDate = OrderDate;
        this.UserID = UserID;
        this.TaxRate = 7.25;
        this.TotalCost = TotalCost;
        this.Paid = Paid;
    }
    
    /**
     * @return the OrderNumber
     */
    public int getOrderNumber() {
        return OrderNumber;
    }

    /**d
     * @param OrderNumber the OrderNumber to set
     */
    public void setOrderNumber(int OrderNumber) {
        this.OrderNumber = OrderNumber;
    }

    /**
     * @return the Date
     */
    public String getOrderDate() {
        return OrderDate;
    }

    /**
     * @param Date the Date to set
     */
    public void setOrderDate(String OrderDate) {
        this.OrderDate = OrderDate;
    }

    /**
     * @return the User
     */
    public int getUserID() {
        return UserID;
    }

    /**
     * @param User the User to set
     */
    public void setUserID(int UserID) {
        this.UserID = UserID;
    }

    /**
     * @return the items
     */
    public ArrayList<OrderItem> getItems() {
        return items;
    }

    /**
     * @param items the items to set
     */
    public void setItems(ArrayList<OrderItem> items) {
        this.items = items;
    }

    /**
     * @return the TaxRate
     */
    public double getTaxRate() {
        return TaxRate;
    }

    /**
     * @param TaxRate the TaxRate to set
     */
    public void setTaxRate(double TaxRate) {
        this.TaxRate = 7.25;
    }

    /**
     * @return the TotalCost
     */
    public double getTotalCost() {
        return TotalCost;
    }

    /**
     * @param TotalCost the TotalCost to set
     */
    public void setTotalCost(double TotalCost) {
        this.TotalCost = TotalCost;
    }

    /**
     * @return the Paid
     */
    public boolean getPaid() {
        return Paid;
    }

    /**
     * @param Paid the Paid to set
     */
    public void setPaid(boolean Paid) {
        this.Paid = Paid;
    }
}
