/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beans;

import java.io.Serializable;
import java.text.NumberFormat;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

//@Entity
public class Product implements Serializable {
    
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private int ProductCode;
    private String ProductName;
    private String CatalogCategory;
    private String Description;
    private String ImageURL;
    private double Price;

    public Product() {
    }

    public Product(int productCode, String productName, String catalogCategory, double price, String description, String imageURL) {
     
        this.ProductCode = productCode;
        this.ProductName = productName;
        this.CatalogCategory = catalogCategory;
        this.Description = description;
        this.ImageURL = imageURL;
        this.Price = price;
    }

    /**
     * @return the ProductCode
     */
    public int getProductCode() {
        return ProductCode;
    }

    /**
     * @param ProductCode the ProductCode to set
     */
    public void setProductCode(int ProductCode) {
        this.ProductCode = ProductCode;
    }

    /**
     * @return the ProductName
     */
    public String getProductName() {
        return ProductName;
    }

    /**
     * @param ProductName the ProductName to set
     */
    public void setProductName(String ProductName) {
        this.ProductName = ProductName;
    }

    /**
     * @return the CatalogCategory
     */
    public String getCatalogCategory() {
        return CatalogCategory;
    }

    /**
     * @param CatalogCategory the CatalogCategory to set
     */
    public void setCatalogCategory(String CatalogCategory) {
        this.CatalogCategory = CatalogCategory;
    }

    /**
     * @return the Description
     */
    public String getDescription() {
        return Description;
    }

    /**
     * @param Description the Description to set
     */
    public void setDescription(String Description) {
        this.Description = Description;
    }

    /**
     * @return the ImageURL
     */
    public String getImageURL() {
        return ImageURL;
    }

    /**
     * @param ImageURL the ImageURL to set
     */
    public void setImageURL(String ImageURL) {
        this.ImageURL = ImageURL;
    }

    /**
     * @return the Price
     */
    public double getPrice() {
        return Price;
    }

    /**
     * @param Price the Price to set
     */
    public void setPrice(double Price) {
        this.Price = Price;
    }



}
