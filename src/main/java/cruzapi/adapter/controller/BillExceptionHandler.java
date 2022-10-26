package cruzapi.adapter.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;

import cruzapi.core.exception.BillException;

@RestControllerAdvice
class BillExceptionHandler
{
	@ExceptionHandler(BillException.class)
	public ResponseEntity<String> handle(BillException e)
	{
		return ResponseEntity.badRequest().body(e.getMessage());
	}
	
	@ExceptionHandler(HttpStatusCodeException.class)
	public ResponseEntity<String> handle(HttpStatusCodeException e)
	{
		if(e.getStatusCode() == HttpStatus.UNAUTHORIZED)
		{
			return ResponseEntity.status(e.getStatusCode())
					.body("You must generate an access token through builders API and pass it via parameter. "
							+ "\ne.g. /bill?token=" + new UUID(0L, 0L)
							+ "\nAPI: https://vagas.builders/api/builders/auth/tokens");
		}
		
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
