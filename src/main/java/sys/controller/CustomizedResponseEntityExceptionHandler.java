package sys.controller;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import sys.exception.ErrorDetails;
import sys.exception.TodoNotFoundException;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<ErrorDetails> handleRuntimeException(final RuntimeException ex,
	    final WebRequest request) {
	final ErrorDetails errorDetails = new ErrorDetails(new Date(), "RuntimeException " + ex.getMessage(),
		HttpStatus.INTERNAL_SERVER_ERROR);

	return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TodoNotFoundException.class)
    public final ResponseEntity<ErrorDetails> handleTodoNotFoundException(final TodoNotFoundException ex,
	    final WebRequest request) {
	final ErrorDetails errorDetails = new ErrorDetails(new Date(), "TodoNotFoundException " + ex.getMessage(),
		HttpStatus.NOT_FOUND);

	return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<ErrorDetails> handleIllegalArgumentException(final IllegalArgumentException ex,
	    final WebRequest request) {
	final ErrorDetails errorDetails = new ErrorDetails(new Date(), "IllegalArgumentException " + ex.getMessage(),
		HttpStatus.BAD_REQUEST);

	return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}