package cruzapi.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Entity
@Table
public class CalculatedBillPayment
{
	@Id
	@GeneratedValue
	@Column(columnDefinition = "binary(16)")
	@JsonIgnore
	private UUID uuid;
	
	@JsonIgnore
	@Column(columnDefinition = "char(44)", nullable = false)
	private String code;
	
	@Column(columnDefinition = "decimal(19,4)", nullable = false)
	private BigDecimal originalAmout;
	
	@Column(columnDefinition = "decimal(19,4)", nullable = false)
	private BigDecimal amount;
	
	@Column(nullable = false)
	private LocalDate dueDate;
	
	@Column(nullable = false)
	private LocalDate paymentDate;
	
	@Column(columnDefinition = "decimal(19,4)", nullable = false)
	private BigDecimal interestAmountCalculated;
	
	@Column(columnDefinition = "decimal(19,4)", nullable = false)
	private BigDecimal fineAmountCalculated;
}
