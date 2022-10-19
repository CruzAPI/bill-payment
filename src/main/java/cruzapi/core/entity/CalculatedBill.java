package cruzapi.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
public class CalculatedBill implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private UUID uuid;
	private String code;
	private LocalDate paymentDate;
	private BigDecimal originalAmount;
	private LocalDate dueDate;
	private BigDecimal interestAmountCalculated;
	private BigDecimal fineAmountCalculated;
	private BigDecimal amount;
	private LocalDateTime requestDateTime;
	
	@Builder
	private CalculatedBill(String code, LocalDate paymentDate, BigDecimal originalAmount, LocalDate dueDate, 
			BigDecimal interestAmountCalculated, BigDecimal fineAmountCalculated, LocalDateTime requestDateTime)
	{
		this.code = code;
		this.paymentDate = paymentDate;
		this.originalAmount = originalAmount;
		this.dueDate = dueDate;
		this.interestAmountCalculated = interestAmountCalculated;
		this.fineAmountCalculated = fineAmountCalculated;
		this.requestDateTime = requestDateTime;
		
		recalculeAmount();
	}
	
	public BigDecimal recalculeAmount()
	{
		return amount = originalAmount.add(fineAmountCalculated).add(interestAmountCalculated);
	}
}
