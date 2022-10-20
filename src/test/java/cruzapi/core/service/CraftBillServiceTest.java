package cruzapi.core.service;

import static cruzapi.core.entity.BillDetails.Type.NORMAL;
import static cruzapi.core.entity.BillDetails.Type.NPC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.hamcrest.MatcherAssert.assertThat;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Stream;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import cruzapi.TestUtils;
import cruzapi.core.entity.BillDetails;
import cruzapi.core.entity.CalculatedBill;
import cruzapi.core.exception.BillNotExpiredException;
import cruzapi.core.exception.BillNotNpcException;
import cruzapi.core.port.CalculatedBillRepository;

@ExtendWith(SpringExtension.class)
class CraftBillServiceTest
{
	private CraftBillService billService;
	
	private CalculatedBillRepository repository;
	private Clock clock;
	
	private BillDetails expiredBill;
	
	private static Stream<Arguments> calculatedBillParameters()
	{
		return Stream.of(
				Arguments.of(new BigDecimal("100.0"), new BigDecimal("1.0"), new BigDecimal("2.0"), 
						new BigDecimal("103.0"), 30L),
				Arguments.of(new BigDecimal("260.0"), new BigDecimal("0.0858"), new BigDecimal("5.2"), 
						new BigDecimal("265.2858"), 1L));
	}
	
	@BeforeEach
	void beforeEach()
	{
		repository = mock(CalculatedBillRepository.class);
		clock = mock(Clock.class);
		
		expiredBill = TestUtils.getBillDetails1();
		
		when(clock.instant()).thenReturn(Instant.now());
		when(clock.getZone()).thenReturn(ZoneId.systemDefault());
		
		billService = new CraftBillService(repository, clock);
	}
	
	@Test
	void assertThrowsBillNotExpiredException()
	{
		BillDetails billDetails = mock(BillDetails.class);
		
		when(billDetails.isExpired(clock)).thenReturn(false);
		
		BillNotExpiredException e = assertThrows(BillNotExpiredException.class,
				() -> billService.calculateBill(billDetails, null));
		
		assertEquals("Bill payment must be expired.", e.getMessage());
		verify(billDetails).isExpired(clock);
		verify(billDetails, never()).getType();
	}
	
	@Test
	void assertThrowsBillNotNPCException()
	{
		BillDetails billDetails = mock(BillDetails.class);
		
		when(billDetails.isExpired(clock)).thenReturn(true);
		when(billDetails.getType()).thenReturn(NORMAL);
		
		BillNotNpcException e = assertThrows(BillNotNpcException.class,
				() -> billService.calculateBill(billDetails, null));
		
		assertEquals("Bill payment must be of the NPC type.", e.getMessage());
		
		verify(billDetails).isExpired(clock);
		verify(billDetails).getType();
	}
	
	@ParameterizedTest
	@MethodSource("calculatedBillParameters")
	void assertCalculatedBillAmounts(BigDecimal originalAmount, 
			BigDecimal interest, BigDecimal fine, BigDecimal amount, long daysLate)
	{
		BillDetails billDetails = spy(this.expiredBill);
		LocalDate dueDate = billDetails.getDueDate();
		LocalDate paymentDate = dueDate.plusDays(daysLate);
		
		doReturn(true).when(billDetails).isExpired(clock);
		doReturn(originalAmount).when(billDetails).getAmount();
		
		CalculatedBill calculatedBill = billService.calculateBill(billDetails, paymentDate);
		
		verify(billDetails).isExpired(clock);
		verify(billDetails).getType();
		verify(billDetails, atLeastOnce()).getDueDate();
		verify(billDetails).getAmount();
		
		assertTrue(originalAmount == calculatedBill.getOriginalAmount());
		assertThat(fine, Matchers.comparesEqualTo(calculatedBill.getFineAmountCalculated()));
		assertThat(interest, Matchers.comparesEqualTo(calculatedBill.getInterestAmountCalculated()));
		assertThat(amount, Matchers.comparesEqualTo(calculatedBill.getAmount()));
	}
	
//	@Test
	void assertCalculatedBillObject()
	{
		long daysLate = 30L;
		
		BillDetails billDetails = spy(this.expiredBill);
		LocalDate dueDate = billDetails.getDueDate();
		LocalDate paymentDate = dueDate.plusDays(daysLate);
		
		doReturn(true).when(billDetails).isExpired(clock);
		
		CalculatedBill actual = billService.calculateBill(billDetails, paymentDate);
		
		verify(billDetails).isExpired(clock);
		verify(billDetails).getType();
		verify(billDetails, atLeastOnce()).getDueDate();
		verify(billDetails).getAmount();
		
		MathContext mathContext = new MathContext(5, RoundingMode.UP);
		BigDecimal originalAmount = billDetails.getAmount();
		BigDecimal fineMultiplier = new BigDecimal("0.02", mathContext);
		BigDecimal interestMultiplierMontly = new BigDecimal("0.01", mathContext);
		BigDecimal interestMultiplier = new BigDecimal(daysLate, mathContext).divide(new BigDecimal("30"), mathContext)
				.multiply(interestMultiplierMontly);
		
		CalculatedBill expected = CalculatedBill.builder()
				.code(billDetails.getCode())
				.paymentDate(paymentDate)
				.dueDate(billDetails.getDueDate())
				.originalAmount(originalAmount)
				.fineAmountCalculated(originalAmount.multiply(fineMultiplier))
				.interestAmountCalculated(originalAmount.multiply(interestMultiplier))
				.requestDateTime(LocalDateTime.now(clock))
				.build();
		
		assertEquals(expected, actual);
	}
}
