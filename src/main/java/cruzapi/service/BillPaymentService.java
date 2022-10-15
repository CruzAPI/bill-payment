package cruzapi.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import cruzapi.dto.BillPaymentDetails;
import cruzapi.model.CalculatedBillPayment;
import cruzapi.repository.CalculatedBillRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BillPaymentService
{
	private CalculatedBillRepository calculatedBillRepository;
	
	@Transactional
	public <S extends CalculatedBillPayment> S save(S entity)
	{
		return calculatedBillRepository.save(entity);
	}
	
	public CalculatedBillPayment calculateBill(BillPaymentDetails billDetails, LocalDate paymentDate)
	{
		CalculatedBillPayment calculatedBill = new CalculatedBillPayment();
		
		long daysLate = ChronoUnit.DAYS.between(billDetails.getDueDate(), paymentDate);
		
		MathContext mathContext = new MathContext(5, RoundingMode.UP);
		
		BigDecimal originalAmount = billDetails.getAmount();
		BigDecimal fineMultiplier = new BigDecimal("0.02", mathContext);
		BigDecimal interestMultiplierMontly = new BigDecimal("0.01", mathContext);
		BigDecimal interestMultiplier = new BigDecimal(daysLate, mathContext).divide(new BigDecimal("30"), mathContext)
				.multiply(interestMultiplierMontly);
		
		calculatedBill.setCode(billDetails.getCode());
		calculatedBill.setPaymentDate(paymentDate);
		calculatedBill.setDueDate(billDetails.getDueDate());
		calculatedBill.setOriginalAmout(originalAmount);
		calculatedBill.setFineAmountCalculated(originalAmount.multiply(fineMultiplier));
		calculatedBill.setInterestAmountCalculated(originalAmount.multiply(interestMultiplier));
		
		return calculatedBill;
	}
}
