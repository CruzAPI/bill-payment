package cruzapi.adapter.config;

import java.time.Clock;
import java.time.temporal.ChronoUnit;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import cruzapi.adapter.repository.CraftCalculatedBillRepository;
import cruzapi.adapter.repository.SpringCalculatedBillRepository;
import cruzapi.core.port.CalculatedBillRepository;
import cruzapi.core.service.CraftBillService;

@Configuration
public class BeanConfig
{
	@Bean
	public CalculatedBillRepository getCalculatedBillRepository(SpringCalculatedBillRepository repository)
	{
		return new CraftCalculatedBillRepository(repository);
	}
	
	@Bean
	public CraftBillService getBillService(CalculatedBillRepository repository, Clock clock)
	{
		return new CraftBillService(repository, clock);
	}
	
	@Bean
	public RestTemplate getRestTemplate(RestTemplateBuilder builder)
	{
		return builder.build();
	}
}
