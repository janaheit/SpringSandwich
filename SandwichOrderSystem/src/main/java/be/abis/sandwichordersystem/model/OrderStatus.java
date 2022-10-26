package be.abis.sandwichordersystem.model;

public enum OrderStatus {

    // Options
    UNFILLED("Hasn't ordered yet"),
    ORDERED("Has ordered"),
    NOSANDWICH("Doesn't want a sandwich");

    private String message;

    // Constructor
    OrderStatus(String message) {
        this.message = message;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
