/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 * @author Lloyd King
 */
public class InHouse extends Part {

    private int machineID;

    public InHouse(int partID, String name, double price, int numInStock, int min, int max, int machID) {
        super();

        setId(partID);
        setName(name);
        setPrice(price);
        setStock(numInStock);
        setMin(min);
        setMax(max);
        setMachineID(machID);
    }

    public int getMachineID() {
        return machineID;
    }

    public void setMachineID(int id) {
        this.machineID = id;
    }

}
