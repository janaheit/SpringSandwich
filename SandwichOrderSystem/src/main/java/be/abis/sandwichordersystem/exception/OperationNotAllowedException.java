package be.abis.sandwichordersystem.exception;

public class OperationNotAllowedException extends Exception {
    public OperationNotAllowedException(String message) {
        super(message);
    }
}
