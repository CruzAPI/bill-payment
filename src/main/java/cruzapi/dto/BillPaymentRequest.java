package cruzapi.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BillPaymentRequest
{
	@Pattern(regexp = "[0-9]{44}")
	@NotBlank
	private String code;
}
