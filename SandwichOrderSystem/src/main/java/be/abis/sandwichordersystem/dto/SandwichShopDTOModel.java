package be.abis.sandwichordersystem.dto;

import be.abis.sandwichordersystem.enums.BreadType;
import be.abis.sandwichordersystem.enums.Options;
import be.abis.sandwichordersystem.model.Sandwich;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

public class SandwichShopDTOModel {
    // Attributes
    @NotEmpty
    private String name;
    private List<Sandwich> sandwiches = new ArrayList<>();
    private List<Options> options;
    private List<BreadType> breadTypes;

    // Constructor
    public SandwichShopDTOModel() {
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
}
