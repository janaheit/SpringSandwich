package be.abis.sandwichordersystem.enums;

public enum Options {

    // Options

    RAUWKOST("Rauwkost"),
    GRILLEDVEGGIES("Gegrilde Groentjes"),
    CLUB("Club"),
    NO_BUTTER("Zonder boter");

    private String option;

    // Constructor
    Options(String option) {
        this.option = option;
    }

    // Getters and setters
    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public static Options fromStringToOption(String option){
        for (Options o: Options.values()){
            if (o.option.equalsIgnoreCase(option)){
                return o;
            }
        }
        return null;
    }
}
