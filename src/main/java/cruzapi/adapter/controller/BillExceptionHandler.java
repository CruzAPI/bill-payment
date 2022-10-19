package cruzapi.adapter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;

import cruzapi.core.exception.BillException;

@RestControllerAdvice
public class BillExceptionHandler
{
	@ExceptionHandler(BillException.class)
	public ResponseEntity<String> handle(BillException e)
	{
		return ResponseEntity.badRequest().body(e.getMessage());
	}
	
	@ExceptionHandler(HttpStatusCodeException.class)
	public ResponseEntity<String> handle(HttpStatusCodeException e)
	{
		return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> handle(MethodArgumentNotValidException e)
	{
		FieldError fieldError = e.getFieldError();
		
		if(fieldError != null)
		{
			return ResponseEntity.badRequest().body(fieldError.getDefaultMessage());
		}
		
		return ResponseEntity.badRequest().build();
	}
}
