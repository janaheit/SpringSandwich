package be.abis.sandwichordersystem.exception;

public class SessionAlreadyExistsException extends Exception{
    public SessionAlreadyExistsException(String message) {
        super(message);
    }
}
