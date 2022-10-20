package cruzapi.adapter.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import cruzapi.adapter.mapper.BillDetailsMapper;
import cruzapi.adapter.mapper.CalculatedBillMapper;
import cruzapi.core.port.BillService;

@ExtendWith(SpringExtension.class)
class BillControllerTest
{
	private BillController billController;
	
	private BillService billService;
	private BillDetailsMapper billDetailsMapper;
	private CalculatedBillMapper calculatedBillMapper;
	
	@BeforeEach
	void beforeEach()
	{
		billService = mock(BillService.class);
		billDetailsMapper = mock(BillDetailsMapper.class);
		calculatedBillMapper = mock(CalculatedBillMapper.class);
		
		billController = new BillController(billService, calculatedBillMapper, billDetailsMapper);
	}
	
	@Test
	void calculateBillCreated()
	{
//		billController.calculateBill(null, null, null);
	}
}
