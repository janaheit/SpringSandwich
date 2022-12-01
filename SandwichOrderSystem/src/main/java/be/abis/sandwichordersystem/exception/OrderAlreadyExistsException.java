package be.abis.sandwichordersystem.exception;

public class OrderAlreadyExistsException extends Exception {
    public OrderAlreadyExistsException(String message) {
        super(message);
    }
}
