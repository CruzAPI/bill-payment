package cruzapi.adapter.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import cruzapi.core.entity.BillDetails.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BillDetailsDTO implements Serializable
{
	private static final long serialVersionUID = -1160890682733083476L;
	
	private String code;
	private LocalDate dueDate;
	private BigDecimal amount;
	private String recipientName;
	private String recipientDocument;
	private Type type;
}
