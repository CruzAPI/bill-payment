package cruzapi;

import java.time.Clock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class Main
{
	public static void main(String[] args)
	{
		SpringApplication.run(Main.class, args);
	}
	
	@Bean
	public Clock clock()
	{
		return Clock.systemDefaultZone();
	}
}
