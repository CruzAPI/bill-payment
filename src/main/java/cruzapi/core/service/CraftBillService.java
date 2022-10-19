package cruzapi.core.service;

import static cruzapi.core.entity.BillDetails.Type.NPC;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import cruzapi.core.entity.BillDetails;
import cruzapi.core.entity.CalculatedBill;
import cruzapi.core.exception.BillNotExpiredException;
import cruzapi.core.exception.BillNotNpcException;
import cruzapi.core.port.BillService;
import cruzapi.core.port.CalculatedBillRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CraftBillService implements BillService
{
	private final CalculatedBillRepository repository;
	private final Clock clock;
	
	@Override
	public CalculatedBill save(CalculatedBill entity)
	{
		return repository.save(entity);
	}
	
	@Override
	public CalculatedBill calculateBill(BillDetails billDetails, LocalDate paymentDate)
			throws BillNotExpiredException, BillNotNpcException
	{
		if(!billDetails.isExpired(clock))
		{
			throw new BillNotExpiredException("Bill payment must be expired.");
		}
		
		if(billDetails.getType() != NPC)
		{
			throw new BillNotNpcException("Bill payment must be of the NPC type.");
		}
		
		long daysLate = ChronoUnit.DAYS.between(billDetails.getDueDate(), paymentDate);
		
		MathContext mathContext = new MathContext(5, RoundingMode.UP);
		BigDecimal originalAmount = billDetails.getAmount();
		BigDecimal fineMultiplier = new BigDecimal("0.02", mathContext);
		BigDecimal interestMultiplierMontly = new BigDecimal("0.01", mathContext);
		BigDecimal interestMultiplier = new BigDecimal(daysLate, mathContext).divide(new BigDecimal("30"), mathContext)
				.multiply(interestMultiplierMontly);
		
		return CalculatedBill.builder()
				.code(billDetails.getCode())
				.paymentDate(paymentDate)
				.dueDate(billDetails.getDueDate())
				.originalAmount(originalAmount)
				.fineAmountCalculated(originalAmount.multiply(fineMultiplier))
				.interestAmountCalculated(originalAmount.multiply(interestMultiplier))
				.requestDateTime(LocalDateTime.now(clock))
				.build();
	}
}
