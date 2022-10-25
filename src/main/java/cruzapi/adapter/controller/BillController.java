package cruzapi.adapter.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import cruzapi.adapter.dto.BillDTO;
import cruzapi.adapter.dto.CalculatedBillDTO;
import cruzapi.adapter.mapper.CalculatedBillMapper;
import cruzapi.core.entity.BillDetails;
import cruzapi.core.entity.CalculatedBill;
import cruzapi.core.port.BillService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BillController
{
	private final BillService billService;
	private final BillRestConsumer consumer;
	private final CalculatedBillMapper mapper;
	
	@PostMapping("/bill")
	public ResponseEntity<CalculatedBillDTO> calculateBill(RestTemplate restTemplate, @RequestBody @Valid BillDTO bill, String token)
	{
		BillDetails billDetails = consumer.requestBillDetails(restTemplate, bill, token);
		
		CalculatedBill calculatedBill;
		
		calculatedBill = billService.calculateBill(billDetails, bill.getPaymentDate());
		calculatedBill = billService.save(calculatedBill);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(calculatedBill));
	}
}
