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

import cruzapi.adapter.dto.BillDTO;
import cruzapi.adapter.dto.BillDetailsDTO;
import cruzapi.adapter.dto.CalculatedBillDTO;
import cruzapi.adapter.mapper.BillDetailsMapper;
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
	private BillDetailsMapper billDetailsMapper;
	
	@BeforeEach
	void beforeEach()
	{
		consumer = mock(BillRestConsumer.class);
		billService = mock(BillService.class);
		calculatedBillMapper = mock(CalculatedBillMapper.class);
		billDetailsMapper = mock(BillDetailsMapper.class);
		
		billController = new BillController(billService, consumer, calculatedBillMapper, billDetailsMapper);
	}
	
	@Test
	@SuppressWarnings("unchecked")
	void testResponseCreated()
	{
		BillDTO billDTO = mock(BillDTO.class);
		BillDetailsDTO billDetailsDTO = mock(BillDetailsDTO.class);;
		BillDetails billDetails = mock(BillDetails.class);
		CalculatedBill calculatedBill = mock(CalculatedBill.class);
		CalculatedBill savedCalculatedBill = mock(CalculatedBill.class);
		CalculatedBillDTO calculatedBillDTO = mock(CalculatedBillDTO.class);
		ResponseEntity<BillDetailsDTO> response = mock(ResponseEntity.class);
		
		LocalDate paymentDate = LocalDate.now();
		
		when(billDTO.getPaymentDate()).thenReturn(paymentDate);
		when(response.getBody()).thenReturn(billDetailsDTO);
		when(consumer.requestBillDetails(any(), same(billDTO), any())).thenReturn(response);
		when(billDetailsMapper.toEntity(billDetailsDTO)).thenReturn(billDetails);
		when(billService.calculateBill(billDetails, paymentDate)).thenReturn(calculatedBill);
		when(billService.save(calculatedBill)).thenReturn(savedCalculatedBill);
		when(calculatedBillMapper.toDTO(savedCalculatedBill)).thenReturn(calculatedBillDTO);
		
		ResponseEntity<CalculatedBillDTO> actualResponse = billController.calculateBill(null, billDTO, null);
		
		verify(consumer).requestBillDetails(null, billDTO, null);
		verify(response).getBody();
		verify(billDetailsMapper).toEntity(billDetailsDTO);
		verify(billDTO).getPaymentDate();
		verify(billService).calculateBill(billDetails, paymentDate);
		verify(billService).save(calculatedBill);
		verify(calculatedBillMapper).toDTO(savedCalculatedBill);
		
		assertSame(HttpStatus.CREATED, actualResponse.getStatusCode());
		assertSame(calculatedBillDTO, actualResponse.getBody());
	}
}
