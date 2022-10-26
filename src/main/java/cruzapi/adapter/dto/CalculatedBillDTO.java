package cruzapi.adapter.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@AllArgsConstructor
public class CalculatedBillDTO implements Serializable
{
	private static final long serialVersionUID = 3384141437103863151L;
	
	@JsonIgnore
	private String code;
	private LocalDate paymentDate;
	private BigDecimal originalAmout;
	private BigDecimal amount;
	private LocalDate dueDate;
	private BigDecimal interestAmountCalculated;
	private BigDecimal fineAmountCalculated;
}
