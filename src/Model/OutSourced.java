/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 * @author Lloyd King
 */
public class OutSourced extends Part {

    private String companyName;

    public OutSourced(int partID, String name, double price, int numInStock, int min, int max, String company) {
        super();

        setId(partID);
        setName(name);
        setPrice(price);
        setStock(numInStock);
        setMin(min);
        setMax(max);
        setCompanyName(company);
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String name) {
        this.companyName = name;
    }

}
