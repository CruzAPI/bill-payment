package cruzapi.controller;

import java.time.Clock;

import javax.validation.Valid;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import cruzapi.BillPaymentType;
import cruzapi.RestTemplateResponseErrorHandler;
import cruzapi.dto.BillPayment;
import cruzapi.dto.BillPaymentDetails;
import cruzapi.dto.BillPaymentRequest;
import cruzapi.model.CalculatedBillPayment;
import cruzapi.service.BillPaymentService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BillPaymentController
{
	private final Clock clock;
	private final BillPaymentService billService;
	
	@GetMapping("/test")
	public ResponseEntity<?> test(String token, @RequestBody @Valid BillPayment bill, RestTemplate restTemplate)
	{
		try
		{
			ResponseEntity<BillPaymentDetails> response = requestBillDetails(restTemplate, bill, token);
			BillPaymentDetails billDetails = response.getBody();
			
			if(billDetails.getType() != BillPaymentType.NPC)
			{
				return ResponseEntity.badRequest().body("Bill payment must be of the NPC type.");
			}
			
			if(!billDetails.isExpired(clock))
			{
				return ResponseEntity.badRequest().body("Bill payment must be expired");
			}
			
			CalculatedBillPayment calculatedBill = billService.calculateBill(billDetails, bill.getPaymentDate());
			
			return ResponseEntity.status(HttpStatus.CREATED).body(billService.save(calculatedBill));
		}
		catch(HttpStatusCodeException e)
		{
			return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
		}
	}
	
	private ResponseEntity<BillPaymentDetails> requestBillDetails(RestTemplate restTemplate, BillPayment bill,
			String token)
	{
		restTemplate.getInterceptors().add((request, body, execution) ->
		{
			request.getHeaders().set(HttpHeaders.AUTHORIZATION, token);
			return execution.execute(request, body);
		});
		
		String uri = "https://vagas.builders/api/builders/bill-payments/codes";
		
		var requestEntity = new HttpEntity<>(new BillPaymentRequest(bill.getBarCode()));
		return restTemplate.exchange(uri, HttpMethod.POST, requestEntity, BillPaymentDetails.class);
	}
	
	@Bean
	public RestTemplate getRestTemplate(RestTemplateBuilder builder)
	{
		return builder.errorHandler(new RestTemplateResponseErrorHandler()).build();
	}
}
