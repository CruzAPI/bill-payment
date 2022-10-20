package cruzapi.adapter.controller;

import javax.validation.Valid;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import cruzapi.adapter.dto.BillDTO;
import cruzapi.adapter.dto.BillDetailsDTO;
import cruzapi.adapter.dto.BillRequestDTO;
import cruzapi.adapter.mapper.BillDetailsMapper;
import cruzapi.adapter.mapper.CalculatedBillMapper;
import cruzapi.core.entity.BillDetails;
import cruzapi.core.entity.CalculatedBill;
import cruzapi.core.port.BillService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BillController
{
	private final String uri = "https://vagas.builders/api/builders/bill-payments/codes";
	private final BillService billService;
	private final CalculatedBillMapper mapper;
	private final BillDetailsMapper billDetailsMapper;
	
	@GetMapping("/test")
	public ResponseEntity<?> calculateBill(String token, @RequestBody @Valid BillDTO bill, RestTemplate restTemplate)
	{
		ResponseEntity<BillDetailsDTO> response = requestBillDetails(restTemplate, bill, token);
		BillDetails billDetails = billDetailsMapper.toEntity(response.getBody());
		
		CalculatedBill calculatedBill;
		
		calculatedBill = billService.calculateBill(billDetails, bill.getPaymentDate());
		calculatedBill = billService.save(calculatedBill);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(calculatedBill));
	}
	
	private ResponseEntity<BillDetailsDTO> requestBillDetails(RestTemplate restTemplate, BillDTO bill, String token)
	{
		restTemplate.getInterceptors().add((request, body, execution) ->
		{
			request.getHeaders().set(HttpHeaders.AUTHORIZATION, token);
			return execution.execute(request, body);
		});
		
		var requestEntity = new HttpEntity<>(new BillRequestDTO(bill.getBarCode()));
		return restTemplate.exchange(uri, HttpMethod.POST, requestEntity, BillDetailsDTO.class);
	}
}
