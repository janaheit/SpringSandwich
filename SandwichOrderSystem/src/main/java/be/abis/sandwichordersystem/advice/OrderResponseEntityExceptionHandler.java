package be.abis.sandwichordersystem.advice;

import be.abis.sandwichordersystem.error.ApiError;
import be.abis.sandwichordersystem.exception.PersonNotFoundException;
import be.abis.sandwichordersystem.exception.SandwichShopNotFoundException;
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




}