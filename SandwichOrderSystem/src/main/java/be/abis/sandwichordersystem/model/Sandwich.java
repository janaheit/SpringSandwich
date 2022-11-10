package be.abis.sandwichordersystem.model;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

public class Sandwich {

    // Attributes
    private static int COUNT=0;
    private int sandwichID;
    private String name;
    private double price;
    private String description;
    private String category;

    // Constructor

    public Sandwich() {
        this.sandwichID = ++COUNT;
    }

    public Sandwich(String name) {
        this();
        this.name = name;
    }

    public Sandwich(String name, String category) {
        this(name);
        this.category = category;
    }

    public Sandwich(String name, String category, String description) {
        this(name, category);
        this.description = description;
    }

    // BUSINESS METHODS


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sandwich sandwich = (Sandwich) o;
        return sandwichID == sandwich.sandwichID && Double.compare(sandwich.price, price) == 0 && name.equals(sandwich.name) && Objects.equals(description, sandwich.description) && category.equals(sandwich.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sandwichID, name, price, category);
    }

    @Override
    public String toString() {
        String description;
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("nl", "BE"));
        if (price != 0) description = name+ " " + nf.format(price) + "";
        else description = name;
        return description;
    }


    // Getters and Setters

    public int getSandwichID() {
        return sandwichID;
    }

    public void setSandwichID(int sandwichID) {
        this.sandwichID = sandwichID;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
