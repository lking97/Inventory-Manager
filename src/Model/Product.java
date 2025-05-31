/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.ArrayList;

/**
 * @author Lloyd King
 */
public class Product {

    private ArrayList<Part> associatedParts = new ArrayList<Part>();
    private int productID;
    private String name;
    private double price = 0.0;
    private int inStock = 0;
    private int min;
    private int max;
    private double cost;

    public Product(int productID, String name, double price, int inStock, int min, int max) {
        setProductID(productID);
        setName(name);
        setPrice(price);
        setInStock(inStock);
        setMin(min);
        setMax(max);

    }

    public ArrayList<Part> getAssociatedParts() {
        return associatedParts;
    }

    public void setAssociatedParts(ArrayList<Part> associatedParts) {
        this.associatedParts = associatedParts;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getInStock() {
        return inStock;
    }

    public void setInStock(int inStock) {
        this.inStock = inStock;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void addAssociatedPart(Part partToAdd) {
        associatedParts.add(partToAdd);
    }

    public boolean removeAssociatedPart(int partToRemove) {
        int i;
        for (i = 0; i < associatedParts.size(); i++) {
            if (associatedParts.get(i).getId() == partToRemove) {
                associatedParts.remove(i);
                return true;
            }
        }

        return false;
    }

    public Part lookupAssociatedPart(int partToSearch) {
        for (int i = 0; i < associatedParts.size(); i++) {
            if (associatedParts.get(i).getId() == partToSearch) {
                return associatedParts.get(i);
            }
        }
        return null;
    }

    public int getPartsListSize() {
        return associatedParts.size();
    }

}
