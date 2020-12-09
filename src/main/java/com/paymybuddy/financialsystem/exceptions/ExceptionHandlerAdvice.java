package com.paymybuddy.financialsystem.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.paymybuddy.financialsystem.model.Output;

@ControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

	/**
	 * This method is called whenever there are a resource exception.
	 * 
	 * @param e is an object of type ResourceException.
	 * @return a ResponseEntity containing the HttpStatus and a message.
	 */
	@ExceptionHandler(ResourceException.class)
	public ResponseEntity<String> handleResourceException(ResourceException e) {
		return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
	}

	/**
	 * This method is called whenever there are properties exception.
	 * 
	 * @param e is an object of type PropertiesException.
	 * @return a ResponseEntity containing the HttpStatus with a message and details.
	 */
	@ExceptionHandler(PropertiesException.class)
	public ResponseEntity<Object> handlePropertiesException(PropertiesException e) {
		Output output = new Output();
		output.setMessage(e.getMessage());
		output.setDetails(e.getDetails());
		return ResponseEntity.status(e.getHttpStatus()).body(output);
	}

}
