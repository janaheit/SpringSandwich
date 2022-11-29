package be.abis.sandwichordersystem.enums;

public enum BreadType {

    // Options

    GREY("grijs"),
    WHITE("wit"),
    SOMETHING("Completely different");

    private String breadType;
    // constructor
    BreadType(String breadType) {
        this.breadType = breadType;
    }

    public static BreadType fromStringToBreadType(String bread){
        for (BreadType b: BreadType.values()){
            if (b.breadType.equalsIgnoreCase(bread)){
                return b;
            }
        }
        return null;
    }

    // Getters and Setters


    public String getBreadType() {
        return breadType;
    }

    public void setBreadType(String breadType) {
        this.breadType = breadType;
    }
}
