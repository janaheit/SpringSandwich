package be.abis.sandwichordersystem.dto;

import be.abis.sandwichordersystem.enums.BreadType;
import be.abis.sandwichordersystem.enums.Options;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

public class SandwichShopDTO {
    // Attributes
    private int id;
    @NotEmpty
    private String name;

    // Constructor
    public SandwichShopDTO() {
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
