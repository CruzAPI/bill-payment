package cruzapi.adapter.dto;

import java.time.LocalDate;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BillDTO
{
	@Pattern(regexp = "[0-9]{44}")
	@NotBlank
	private String barCode;
	
	@FutureOrPresent
	@NotNull
	private LocalDate paymentDate;
}
