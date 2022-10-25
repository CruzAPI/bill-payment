package cruzapi.adapter.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import cruzapi.TestUtils;
import cruzapi.adapter.dto.BillDTO;
import cruzapi.core.entity.BillDetails;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class BillControllerMockMvcTest
{
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private BillRestConsumer consumer;
	
	@RepeatedTest(value = 2)
	void testCreated() throws Exception
	{
		BillDetails billDetails = TestUtils.getBillDetails1();
		BillDTO billDTO = new BillDTO(billDetails.getCode(), LocalDate.now());
		
		when(consumer.requestBillDetails(any(RestTemplate.class), any(BillDTO.class), nullable(String.class)))
				.thenReturn(billDetails);
		
		mockMvc.perform(post("/bill")
						.contentType(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8")
						.content(objectMapper.writeValueAsString(billDTO)))
				.andExpectAll(
						status().isCreated(), 
						content().contentType(MediaType.APPLICATION_JSON));
	}
}
