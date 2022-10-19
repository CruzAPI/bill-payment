package cruzapi.adapter.dto;

import java.time.LocalDate;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BillDTO
{
	@Pattern(regexp = "[0-9]{44}", message = "Invalid bar code.")
	@NotNull(message = "Bar code must not be null.")
	private String barCode;
	
	@FutureOrPresent(message = "Payment date must be a date in the present or in the future.")
	@NotNull(message = "Payment date must not be null.")
	private LocalDate paymentDate;
}
