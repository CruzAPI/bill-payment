package cruzapi.dto;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import cruzapi.BillPaymentType;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BillPaymentDetails
{
	private String code;
	private LocalDate dueDate;
	private BigDecimal amount;
	private String recipientName;
	private String recipientDocument;
	private BillPaymentType type;
	
	@JsonIgnore
	public boolean isExpired(Clock clock)
	{
		return dueDate.isBefore(LocalDate.now(clock));
	}
}
