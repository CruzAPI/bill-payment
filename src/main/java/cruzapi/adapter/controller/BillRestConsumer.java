package cruzapi.adapter.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import cruzapi.adapter.dto.BillDTO;
import cruzapi.adapter.dto.BillDetailsDTO;
import cruzapi.adapter.dto.BillRequestDTO;

@Component
class BillRestConsumer
{
	public ResponseEntity<BillDetailsDTO> requestBillDetails(RestTemplate restTemplate, BillDTO bill, String token)
	{
		String uri = "https://vagas.builders/api/builders/bill-payments/codes";
		
		restTemplate.getInterceptors().add((request, body, execution) ->
		{
			request.getHeaders().set(HttpHeaders.AUTHORIZATION, token);
			return execution.execute(request, body);
		});
		
		var requestEntity = new HttpEntity<>(new BillRequestDTO(bill.getBarCode()));
		return restTemplate.exchange(uri, HttpMethod.POST, requestEntity, BillDetailsDTO.class);
	}
}
