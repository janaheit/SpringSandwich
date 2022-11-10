package be.abis.sandwichordersystem.util;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class DateUtil {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");

    public static LocalDate parseStringToDate(String dateString){
        return LocalDate.parse(dateString, formatter);
    }
}
