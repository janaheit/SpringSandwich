package be.abis.sandwichordersystem.exception;

public class OptionNotAvailableRuntimeException extends RuntimeException{
    public OptionNotAvailableRuntimeException(String message) {
        super(message);
    }
}
