package cruzapi.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Entity
@Table
public class CalculatedBillPayment implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	@JsonIgnore
	private CalculatedBillPaymentId calculatedBillPaymentId;
	
	@Column(columnDefinition = "decimal(19,4)", nullable = false)
	@JsonProperty(index = 1)
	private BigDecimal originalAmout;
	
	@Column(columnDefinition = "decimal(19,4)", nullable = false)
	@JsonProperty(index = 2)
	private BigDecimal amount;
	
	@Column(nullable = false)
	@JsonProperty(index = 3)
	private LocalDate dueDate;
	
	@Column(columnDefinition = "decimal(19,4)", nullable = false)
	private BigDecimal interestAmountCalculated;
	
	@Column(columnDefinition = "decimal(19,4)", nullable = false)
	private BigDecimal fineAmountCalculated;
	
	@JsonIgnore
	public String getCode()
	{
		return calculatedBillPaymentId == null ? null : calculatedBillPaymentId.getCode();
	}
	
	@JsonProperty(index = 4)
	public LocalDate getPaymentDate()
	{
		return calculatedBillPaymentId == null ? null : calculatedBillPaymentId.getPaymentDate();
	}
	
	public BigDecimal calculeAmount()
	{
		return originalAmout.add(fineAmountCalculated).add(interestAmountCalculated);
	}
}
