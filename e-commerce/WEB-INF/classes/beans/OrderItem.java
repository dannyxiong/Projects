/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import beans.Product;
import java.io.Serializable;

public class OrderItem implements Serializable {

    private Product product;
    private int quantity;
    private int OrderNumber;
    
    public OrderItem() {
        quantity  = 0;
    }

    public void setProduct(Product p) {
        product = p;
    }

    public Product getProduct() {
        return product;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotal() {
        double total = product.getPrice() * quantity;
        return total;
    }

    /**
     * @return the OrderNumber
     */
    public int getOrderNumber() {
        return OrderNumber;
    }

    /**
     * @param OrderNumber the OrderNumber to set
     */
    public void setOrderNumber(int OrderNumber) {
        this.OrderNumber = OrderNumber;
    }

}