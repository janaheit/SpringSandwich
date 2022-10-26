package be.abis.sandwichordersystem.model;

import java.text.NumberFormat;
import java.util.Locale;

public class Sandwich {

    // Attributes
    private static int COUNT=0;
    private int sandwichNr;
    private String name;
    private double price;
    private String description;
    private String category;

    // Constructor
    public Sandwich(String name) {
        this.name = name;
        this.sandwichNr = ++COUNT;
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
    public String toString() {
        String description;
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("nl", "BE"));
        if (price != 0) description = name+ " " + nf.format(price) + "";
        else description = name;
        return description;
    }


    // Getters and Setters

    public int getSandwichNr() {
        return sandwichNr;
    }

    public void setSandwichNr(int sandwichNr) {
        this.sandwichNr = sandwichNr;
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
