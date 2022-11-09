package be.abis.sandwichordersystem.model;


import be.abis.sandwichordersystem.enums.BreadType;
import be.abis.sandwichordersystem.enums.Options;
import be.abis.sandwichordersystem.exception.SandwichNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class SandwichShop {

    // Attributes

    private String name;
    private List<Sandwich> sandwiches = new ArrayList<>();
    private List<Options> options;
    private List<BreadType> breadTypes;
    private static int COUNT=0;
    private int sandwichShopID;

    // Constructors

    public SandwichShop() {
        this.sandwichShopID = ++COUNT;
    }

    public SandwichShop(String name) {
        this();
    }
    public SandwichShop(String name, List<Sandwich> sandwiches, List<Options> options, List<BreadType> breadTypes) {
        this(name);
        this.sandwiches = sandwiches;
        this.options = options;
        this.breadTypes = breadTypes;
    }

    // Getters and Setters

    public int getSandwichShopID() {
        return sandwichShopID;
    }

    public void setSandwichShopID(int sandwichShopID) {
        this.sandwichShopID = sandwichShopID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Sandwich> getSandwiches() {
        return sandwiches;
    }

    public void setSandwiches(List<Sandwich> sandwiches) {
        this.sandwiches = sandwiches;
    }

    public List<Options> getOptions() {
        return options;
    }

    public void setOptions(List<Options> options) {
        this.options = options;
    }

    public List<BreadType> getBreadTypes() {
        return breadTypes;
    }

    public void setBreadTypes(List<BreadType> breadTypes) {
        this.breadTypes = breadTypes;
    }

    // Business methods
    public void addSandwich(Sandwich sandwich){
        sandwiches.add(sandwich);
    }

    public void deleteSandwich(Sandwich sandwich) throws SandwichNotFoundException {
        if(sandwiches.contains(sandwich)) {
            sandwiches.remove(sandwich);
        } else {
            throw new SandwichNotFoundException("Cannot delete sandwich since it's not found in this sandwichshop");
        }
    }
}
