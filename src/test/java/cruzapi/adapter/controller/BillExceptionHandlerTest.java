package cruzapi.adapter.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.client.HttpStatusCodeException;

import cruzapi.core.exception.BillException;

@ExtendWith(SpringExtension.class)
class BillExceptionHandlerTest
{
	private BillExceptionHandler handler;
	
	@BeforeEach
	void beforeEach()
	{
		handler = new BillExceptionHandler();
	}
	
	@Test
	void testBillExceptionHandler()
	{
		BillException exception = mock(BillException.class);
		
		when(exception.getMessage()).thenReturn(new String());
		
		ResponseEntity<String> actualResponse = handler.handle(exception);
		
		verify(exception).getMessage();
		
		assertSame(HttpStatus.BAD_REQUEST, actualResponse.getStatusCode());
		assertSame(exception.getMessage(), actualResponse.getBody());
	}
	
	@ParameterizedTest
	@MethodSource("httpStatusValues")
	void testHttpStatusCodeExceptionHandler(HttpStatus expectedHttpStatus)
	{
		HttpStatusCodeException exception = mock(HttpStatusCodeException.class);
		
		when(exception.getStatusCode()).thenReturn(expectedHttpStatus);
		when(exception.getResponseBodyAsString()).thenReturn(new String());
		
		ResponseEntity<String> actualResponse = handler.handle(exception);
		
		verify(exception, atLeastOnce()).getStatusCode();
		
		assertSame(expectedHttpStatus, actualResponse.getStatusCode());
		
		if(expectedHttpStatus == HttpStatus.UNAUTHORIZED)
		{
			assertEquals("You must generate an access token through builders API and pass it via parameter. "
					+ "\ne.g. /bill?token=" + new UUID(0L, 0L)
					+ "\nAPI: https://vagas.builders/api/builders/auth/tokens", actualResponse.getBody());
		}
		else
		{
			verify(exception).getResponseBodyAsString();
			
			assertSame(exception.getResponseBodyAsString(), actualResponse.getBody());
		}
	}
	
	private static Stream<Arguments> httpStatusValues()
	{
		List<Object> values = Arrays.asList(HttpStatus.values());
		Collections.shuffle(values);
		return values.stream().map(Arguments::of);
	}
	
	@Test
	void testMethodArgumentNotValidExceptionHandler()
	{
		MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
		FieldError fieldError = mock(FieldError.class);
		
		when(exception.getFieldError()).thenReturn(fieldError);
		when(fieldError.getDefaultMessage()).thenReturn(new String());
		
		ResponseEntity<String> actualResponse = handler.handle(exception);
		
		verify(exception).getFieldError();
		verify(fieldError).getDefaultMessage();
		
		assertSame(HttpStatus.BAD_REQUEST, actualResponse.getStatusCode());
		assertSame(fieldError.getDefaultMessage(), actualResponse.getBody());
	}
	
	@Test
	void testMethodArgumentNotValidExceptionHandlerEmptyError()
	{
		MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
		
		ResponseEntity<String> actualResponse = handler.handle(exception);
		
		verify(exception).getFieldError();
		
		assertSame(HttpStatus.BAD_REQUEST, actualResponse.getStatusCode());
		assertNull(actualResponse.getBody());
	}
}
