package com.example.demo.exeption;

import java.time.LocalDateTime;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import
org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
@ExceptionHandler(ResourceNotFoundException.class)
public ResponseEntity<ErrorDetails>
handleResourceNotFound(ResourceNotFoundException ex,WebRequest request) {
ErrorDetails error = new ErrorDetails(
LocalDateTime.now(),
ex.getMessage(),
request.getDescription(false)
);
return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
}

@Override
protected ResponseEntity<Object> handleMethodArgumentNotValid(
MethodArgumentNotValidException ex,
HttpHeaders headers,
HttpStatusCode status,
WebRequest request) {
StringBuilder builder = new StringBuilder();
ex.getBindingResult().getFieldErrors().forEach(error -> {
builder.append(error.getField())
.append(" : ")
.append(error.getDefaultMessage())
.append("; ");

});
ErrorDetails errorDetails = new ErrorDetails(
LocalDateTime.now(),
"Validation Failed",
builder.toString()
);
return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
}
}