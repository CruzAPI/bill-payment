package cruzapi.core.port;

import java.time.LocalDate;

import cruzapi.core.entity.BillDetails;
import cruzapi.core.entity.CalculatedBill;
import cruzapi.core.exception.BillNotExpiredException;
import cruzapi.core.exception.BillNotNpcException;

public interface BillService
{
	CalculatedBill save(CalculatedBill calculatedBill);
	CalculatedBill calculateBill(BillDetails billDetails, LocalDate paymentDate) 
			throws BillNotNpcException, BillNotExpiredException;
}
