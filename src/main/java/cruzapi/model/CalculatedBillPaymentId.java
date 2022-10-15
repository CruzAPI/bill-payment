package cruzapi.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Embeddable;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class CalculatedBillPaymentId implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String code;
	private LocalDate paymentDate;
}
