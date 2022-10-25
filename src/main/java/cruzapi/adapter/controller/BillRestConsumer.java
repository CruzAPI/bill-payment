package cruzapi.adapter.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import cruzapi.adapter.dto.BillDTO;
import cruzapi.adapter.dto.BillDetailsDTO;
import cruzapi.adapter.dto.BillRequestDTO;
import cruzapi.adapter.mapper.BillDetailsMapper;
import cruzapi.core.entity.BillDetails;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
class BillRestConsumer
{
	private final BillDetailsMapper mapper;
	
	public BillDetails requestBillDetails(RestTemplate restTemplate, BillDTO bill, String token)
	{
		String uri = "https://vagas.builders/api/builders/bill-payments/codes";
		
		restTemplate.getInterceptors().add((request, body, execution) ->
		{
			request.getHeaders().set(HttpHeaders.AUTHORIZATION, token);
			return execution.execute(request, body);
		});
		
		var requestEntity = new HttpEntity<>(new BillRequestDTO(bill.getBarCode()));
		var response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, BillDetailsDTO.class);
		
		return mapper.toEntity(response.getBody());
	}
}
