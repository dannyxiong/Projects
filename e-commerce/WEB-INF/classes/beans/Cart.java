/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;


import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Cart implements Serializable {

    private ArrayList<OrderItem> items;
    DecimalFormat df = new DecimalFormat("##.##");
    
    public Cart() {
        items = new ArrayList<OrderItem>();
    }

    public ArrayList<OrderItem> getItems() {
        return items;
    }

    public int getCount() {
        return items.size();
    }

    public void addItem(OrderItem item) {
        int code = item.getProduct().getProductCode();
        int quantity = item.getQuantity();
        // Update quantity if product is already in the cart
        for (OrderItem lineItem : items) {
            if (lineItem.getProduct().getProductCode() == code) {
                lineItem.setQuantity(quantity);
                return;
            }
        }
        
        // If product does not exist, then add it.
        items.add(item);
    }

    public void removeItem(OrderItem item) {
        int code = item.getProduct().getProductCode();
        for (int i = 0; i < items.size(); i++) {
            OrderItem lineItem = items.get(i);
            if (lineItem.getProduct().getProductCode() == (code)) {
                items.remove(i);
                return;
            }
        }
    }
    public String getSubtotal(){
        
        double subTotal = 0;
        for (OrderItem item : items) {
            double itemPrice = item.getTotal();
            subTotal = subTotal + itemPrice;
        }
      
        return df.format(subTotal); 
    }
    
    public double getTotal(){
        
        double Total = 0;
        for (OrderItem item : items) {
            double itemPrice = item.getTotal();
            Total = Total + itemPrice;
        }
        Total = Total * 1.0725;
        
        return Total; 
    }
    
    public String getTotalDisplay(){
        
        double Total = 0;
        for (OrderItem item : items) {
            double itemPrice = item.getTotal();
            Total = Total + itemPrice;
        }
        Total = Total * 1.0725;
        
        return df.format(Total); 
    }
    public String getTax(){
        double TaxSub = 0.0725;
        double subTotal = 0;
        for (OrderItem item : items) {
            double itemPrice = item.getTotal();
            subTotal = subTotal + itemPrice;
        }
        
        TaxSub = TaxSub * subTotal;
        
        return df.format(TaxSub);
    }
    
        public double getOrderTax(){
            double TaxSub = 0.0725;
            return TaxSub;
    }
}
