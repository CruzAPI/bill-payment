package cruzapi.adapter.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillRequestDTO implements Serializable
{
	private static final long serialVersionUID = -1522620414561079053L;
	
	@Pattern(regexp = "[0-9]{44}")
	@NotBlank
	private String code;
}
