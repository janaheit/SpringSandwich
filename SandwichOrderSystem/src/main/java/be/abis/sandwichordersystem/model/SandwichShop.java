package be.abis.sandwichordersystem.model;


import be.abis.sandwichordersystem.enums.BreadType;
import be.abis.sandwichordersystem.enums.Options;

import java.util.List;

public class SandwichShop {

    // Attributes

    private String name;
    private List<Sandwich> sandwiches;
    private List<Options> options;
    private List<BreadType> breadTypes;

    // Constructors
    public SandwichShop(String name) {
    }
    public SandwichShop(String name, List<Sandwich> sandwiches, List<Options> options, List<BreadType> breadTypes) {
        super();
        this.name = name;
        this.sandwiches = sandwiches;
        this.options = options;
        this.breadTypes = breadTypes;
    }

    // Getters and Setters
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
}
