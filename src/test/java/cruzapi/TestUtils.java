package cruzapi;

import java.math.BigDecimal;
import java.time.LocalDate;

import cruzapi.core.entity.BillDetails;
import cruzapi.core.entity.BillDetails.Type;

public class TestUtils
{
	private static final BillDetails BILL_DETAILS_1;
	private static final BillDetails BILL_DETAILS_2;
	private static final BillDetails BILL_DETAILS_3;
	private static final BillDetails BILL_DETAILS_4;
	
	static
	{
		BILL_DETAILS_1 = BillDetails.builder()
				.code("34191790010104351004791020150008291070026000")
				.dueDate(LocalDate.parse("2022-09-03"))
				.amount(new BigDecimal("260.0"))
				.recipientName("Microhouse Informática S/C Ltda")
				.recipientDocument("83698283000114")
				.type(Type.NPC)
				.build();
		
		BILL_DETAILS_2 = BillDetails.builder()
				.code("34191790010104351004791020150008191070069000")
				.dueDate(LocalDate.parse("2022-08-07"))
				.amount(new BigDecimal("690.0"))
				.recipientName("Loja Losef Ltda")
				.recipientDocument("32131826000186")
				.type(Type.NPC)
				.build();
		
		BILL_DETAILS_3 = BillDetails.builder()
				.code("34199800020104352008771020110004191070010000")
				.dueDate(LocalDate.parse("2022-10-20"))
				.amount(new BigDecimal("100.0"))
				.recipientName("Shop Yes Ltda")
				.recipientDocument("73615476000100")
				.type(Type.NPC)
				.build();
		
		BILL_DETAILS_4 = BillDetails.builder()
				.code("34197650070104357008271020110004991070040000")
				.dueDate(LocalDate.parse("2022-09-03"))
				.amount(new BigDecimal("400.0"))
				.recipientName("Shop Yes Ltda")
				.recipientDocument("73615476000100")
				.type(Type.NORMAL)
				.build();
	}
	
	public static BillDetails getBillDetails1()
	{
		return BILL_DETAILS_1.clone();
	}
	
	public static BillDetails getBillDetails2()
	{
		return BILL_DETAILS_2.clone();
	}
	
	public static BillDetails getBillDetails3()
	{
		return BILL_DETAILS_3.clone();
	}
	
	public static BillDetails getBillDetails4()
	{
		return BILL_DETAILS_4.clone();
	}
}
