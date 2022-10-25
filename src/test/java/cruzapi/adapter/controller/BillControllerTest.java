package cruzapi.adapter.controller;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import cruzapi.adapter.dto.BillDTO;
import cruzapi.adapter.dto.CalculatedBillDTO;
import cruzapi.adapter.mapper.CalculatedBillMapper;
import cruzapi.core.entity.BillDetails;
import cruzapi.core.entity.CalculatedBill;
import cruzapi.core.port.BillService;

@ExtendWith(SpringExtension.class)
class BillControllerTest
{
	private BillController billController;
	
	private BillService billService;
	private BillRestConsumer consumer;
	private CalculatedBillMapper calculatedBillMapper;
	
	@BeforeEach
	void beforeEach()
	{
		consumer = mock(BillRestConsumer.class);
		billService = mock(BillService.class);
		calculatedBillMapper = mock(CalculatedBillMapper.class);
		
		billController = new BillController(billService, consumer, calculatedBillMapper);
	}
	
	@Test
	void testResponseCreated()
	{
		RestTemplate restTemplate = mock(RestTemplate.class);
		BillDTO billDTO = mock(BillDTO.class);
		BillDetails billDetails = mock(BillDetails.class);
		CalculatedBill calculatedBill = mock(CalculatedBill.class);
		CalculatedBill savedCalculatedBill = mock(CalculatedBill.class);
		CalculatedBillDTO calculatedBillDTO = mock(CalculatedBillDTO.class);
		
		LocalDate paymentDate = LocalDate.now();
		String token = new String();
		
		when(billDTO.getPaymentDate()).thenReturn(paymentDate);
		when(consumer.requestBillDetails(any(), any(), any())).thenReturn(billDetails);
		when(billService.calculateBill(billDetails, paymentDate)).thenReturn(calculatedBill);
		when(billService.save(calculatedBill)).thenReturn(savedCalculatedBill);
		when(calculatedBillMapper.toDTO(savedCalculatedBill)).thenReturn(calculatedBillDTO);
		
		ResponseEntity<CalculatedBillDTO> actualResponse = billController.calculateBill(restTemplate, billDTO, token);
		
		verify(consumer).requestBillDetails(same(restTemplate), same(billDTO), same(token));
		verify(billDTO).getPaymentDate();
		verify(billService).calculateBill(billDetails, paymentDate);
		verify(billService).save(calculatedBill);
		verify(calculatedBillMapper).toDTO(savedCalculatedBill);
		
		assertSame(HttpStatus.CREATED, actualResponse.getStatusCode());
		assertSame(calculatedBillDTO, actualResponse.getBody());
	}
}
