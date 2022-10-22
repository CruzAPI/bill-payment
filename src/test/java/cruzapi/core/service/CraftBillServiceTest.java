package cruzapi.core.service;

import static cruzapi.core.entity.BillDetails.Type.NORMAL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Stream;

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
	
	private BillDetails billDetails1;
	
	@BeforeEach
	void beforeEach()
	{
		repository = mock(CalculatedBillRepository.class);
		clock = mock(Clock.class);
		
		billDetails1 = TestUtils.getBillDetails1();
		
		when(clock.instant()).thenReturn(Instant.now());
		when(clock.getZone()).thenReturn(ZoneId.systemDefault());
		
		billService = new CraftBillService(repository, clock);
	}
	
	@Test
	void testThrowsBillNotExpiredException()
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
	void testThrowsBillNotNPCException()
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
	@MethodSource("calculatedBillAmountsParameters")
	void testCalculatedBillAmounts(BigDecimal originalAmount, 
			BigDecimal interest, BigDecimal fine, BigDecimal amount, long daysLate)
	{
		BillDetails billDetails = spy(this.billDetails1);
		LocalDate dueDate = billDetails.getDueDate();
		LocalDate paymentDate = dueDate.plusDays(daysLate);
		
		doReturn(true).when(billDetails).isExpired(clock);
		doReturn(originalAmount).when(billDetails).getAmount();
		
		CalculatedBill calculatedBill = billService.calculateBill(billDetails, paymentDate);
		
		verify(billDetails).isExpired(clock);
		verify(billDetails).getType();
		verify(billDetails, atLeastOnce()).getDueDate();
		verify(billDetails).getAmount();
		
		assertSame(originalAmount, calculatedBill.getOriginalAmount());
		assertThat(fine, comparesEqualTo(calculatedBill.getFineAmountCalculated()));
		assertThat(interest, comparesEqualTo(calculatedBill.getInterestAmountCalculated()));
		assertThat(amount, comparesEqualTo(calculatedBill.getAmount()));
	}
	
	private static Stream<Arguments> calculatedBillAmountsParameters()
	{
		return Stream.of(
				Arguments.of(new BigDecimal("100.0"), new BigDecimal("1.0"), new BigDecimal("2.0"), 
						new BigDecimal("103.0"), 30L),
				Arguments.of(new BigDecimal("260.0"), new BigDecimal("0.0858"), new BigDecimal("5.2"), 
						new BigDecimal("265.2858"), 1L));
	}
	
	@ParameterizedTest
	@MethodSource("calculatedBillObjectParameters")
	void testCalculatedBillObject(BillDetails billDetails, long daysLate)
	{
		billDetails = spy(billDetails);
		LocalDate dueDate = billDetails.getDueDate();
		LocalDate paymentDate = dueDate.plusDays(daysLate);
		
		doReturn(true).when(billDetails).isExpired(clock);
		
		CalculatedBill actual = billService.calculateBill(billDetails, paymentDate);
		
		verify(billDetails).isExpired(clock);
		verify(billDetails).getType();
		verify(billDetails, atLeastOnce()).getDueDate();
		verify(billDetails).getAmount();
		
		MathContext mathContext = new MathContext(2, RoundingMode.DOWN);
		BigDecimal originalAmount = billDetails.getAmount();
		BigDecimal fineMultiplier = new BigDecimal("0.02", mathContext);
		BigDecimal interestMultiplierMontly = new BigDecimal("0.01", mathContext);
		BigDecimal monthsLate = new BigDecimal(daysLate).divide(new BigDecimal("30"), mathContext);
		BigDecimal interestMultiplier = interestMultiplierMontly.multiply(monthsLate);
		
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
	
	private static Stream<Arguments> calculatedBillObjectParameters()
	{
		return Stream.of(
				Arguments.of(TestUtils.getBillDetails1(), 30L),
				Arguments.of(TestUtils.getBillDetails2(), 142L),
				Arguments.of(TestUtils.getBillDetails1(), 43L),
				Arguments.of(TestUtils.getBillDetails3(), 6L),
				Arguments.of(TestUtils.getBillDetails2(), 3L),
				Arguments.of(TestUtils.getBillDetails1(), 2L),
				Arguments.of(TestUtils.getBillDetails3(), 1L));
	}
	
	@Test
	void testSave()
	{
		CalculatedBill calculatedBill = mock(CalculatedBill.class);
		CalculatedBill expected = mock(CalculatedBill.class);
		
		when(repository.save(calculatedBill)).thenReturn(expected);
		
		CalculatedBill actual = billService.save(calculatedBill);
		
		verify(repository).save(calculatedBill);
		
		assertSame(expected, actual);
	}
}
