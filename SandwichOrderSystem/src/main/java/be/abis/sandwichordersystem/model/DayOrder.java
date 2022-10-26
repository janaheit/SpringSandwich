package be.abis.sandwichordersystem.model;

import java.time.LocalDate;

public class DayOrder {

    // Attributes
    private SandwichShop currentSandwichShop;
    private LocalDate date;

    // Constructor
    public DayOrder(SandwichShop currentSandwichShop, LocalDate date) {
        this.currentSandwichShop = currentSandwichShop;
        this.date = date;
    }

    // Getters and Setters
    public SandwichShop getCurrentSandwichShop() {
        return currentSandwichShop;
    }

    public void setCurrentSandwichShop(SandwichShop currentSandwichShop) {
        this.currentSandwichShop = currentSandwichShop;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
