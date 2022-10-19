package cruzapi.adapter.mapper;

import org.springframework.stereotype.Service;

import cruzapi.adapter.dto.CalculatedBillDTO;
import cruzapi.core.entity.CalculatedBill;

@Service
public class CalculatedBillMapper
{
	public CalculatedBillDTO toDTO(CalculatedBill bill)
	{
		return CalculatedBillDTO.builder()
				.code(bill.getCode())
				.paymentDate(bill.getPaymentDate())
				.originalAmout(bill.getOriginalAmount())
				.dueDate(bill.getDueDate())
				.interestAmountCalculated(bill.getInterestAmountCalculated())
				.fineAmountCalculated(bill.getFineAmountCalculated())
				.amount(bill.getAmount())
				.build();
	}
}
