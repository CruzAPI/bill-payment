package cruzapi.adapter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;

import cruzapi.core.exception.BillException;

@RestControllerAdvice
public class BillExceptionHandler
{
	@ExceptionHandler(BillException.class)
	public ResponseEntity<?> handleBillException(BillException e)
	{
		return ResponseEntity.badRequest().body(e.getMessage());
	}
	
	@ExceptionHandler(HttpStatusCodeException.class)
	public ResponseEntity<?> handleHttpStatusCodeException(HttpStatusCodeException e)
	{
		return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
	}
}
