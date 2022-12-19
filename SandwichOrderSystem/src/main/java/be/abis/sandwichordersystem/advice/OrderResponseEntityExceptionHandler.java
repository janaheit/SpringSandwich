package be.abis.sandwichordersystem.advice;

import be.abis.sandwichordersystem.error.ApiError;
import be.abis.sandwichordersystem.exception.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class OrderResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(PersonNotFoundException.class)
    public ResponseEntity<? extends Object> handlePersonNotFound(PersonNotFoundException ex, WebRequest request){

        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiError err = new ApiError("person not found", status.value(), ex.getMessage());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("content-type", MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        return new ResponseEntity<ApiError>(err, responseHeaders, status);
    }

    @ExceptionHandler(SandwichShopNotFoundException.class)
    public ResponseEntity<? extends Object> handleSandwichShopNotFound(SandwichShopNotFoundException ex, WebRequest request){

        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiError err = new ApiError("sandwich shop not found", status.value(), ex.getMessage());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("content-type", MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        return new ResponseEntity<ApiError>(err, responseHeaders, status);
    }

    @ExceptionHandler(SandwichNotFoundException.class)
    public ResponseEntity<? extends Object> handleSandWichNotFoundException(SandwichNotFoundException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiError ae = new ApiError("sandwich not found", status.value(), ex.getMessage());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("content-type", MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        return new ResponseEntity<ApiError>(ae, responseHeaders, status);
    }

    @ExceptionHandler(SessionNotFoundException.class)
    public ResponseEntity<? extends Object> handleSessionNotFoundException(SessionNotFoundException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiError ae = new ApiError("session not found", status.value(), ex.getMessage());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("content-type", MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        return new ResponseEntity<ApiError>(ae, responseHeaders, status);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<? extends Object> handleOrderNotFoundException(OrderNotFoundException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiError ae = new ApiError("Order not found!", status.value(), ex.getMessage());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("content-type", MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        return new ResponseEntity<ApiError>(ae, responseHeaders, status);
    }

    @ExceptionHandler(DayOrderDoesNotExistYet.class)
    public ResponseEntity<? extends Object> handleDayOrderDoesNotExistYetException(DayOrderDoesNotExistYet ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiError ae = new ApiError("Day order does not yet exist!", status.value(), ex.getMessage());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("content-type", MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        return new ResponseEntity<ApiError>(ae, responseHeaders, status);
    }

    @ExceptionHandler(NothingToHandleException.class)
    public ResponseEntity<? extends Object> handleNothingToHandleException(NothingToHandleException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiError ae = new ApiError("nothing to handle", status.value(), ex.getMessage());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("content-type", MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        return new ResponseEntity<ApiError>(ae, responseHeaders, status);
    }


    @ExceptionHandler(OperationNotAllowedException.class)
    public ResponseEntity<? extends Object> handleOperationNotAllowedException(OperationNotAllowedException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiError ae = new ApiError("operation not allowed", status.value(), ex.getMessage());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("content-type", MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        return new ResponseEntity<ApiError>(ae, responseHeaders, status);
    }

    @ExceptionHandler(IngredientNotAvailableException.class)
    public ResponseEntity<? extends Object> handleIngredientNotAvailableException(IngredientNotAvailableException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiError ae = new ApiError("ingredient not available", status.value(), ex.getMessage());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("content-type", MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        return new ResponseEntity<ApiError>(ae, responseHeaders, status);
    }
}
