package cruzapi.adapter.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Stream;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import cruzapi.TestUtils;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
class BillDTOTest
{
	@Autowired
	private Validator validator;
	
	private String validBarCode;
	
	@BeforeEach
	void beforeEach()
	{
		validBarCode = TestUtils.getBillDetails1().getCode();
	}
	
	@Test
	void testNotNullBarCodeValidation()
	{
		BillDTO billDTO = new BillDTO(null, LocalDate.MAX);
		
		Set<ConstraintViolation<BillDTO>> violations = validator.validate(billDTO);
		
		ConstraintViolation<BillDTO> violation = violations.iterator().next();
		
		assertSame(NotNull.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
		assertEquals("Bar code must not be null.", violation.getMessage());
		assertEquals(1, violations.size());
	}
	
	@ParameterizedTest
	@MethodSource("getInvalidBarCodes")
	void testPatternBarCodeValidation(String invalidBarCode)
	{
		BillDTO billDTO = new BillDTO(invalidBarCode, LocalDate.MAX);
		
		Set<ConstraintViolation<BillDTO>> violations = validator.validate(billDTO);
		
		ConstraintViolation<BillDTO> violation = violations.iterator().next();
		
		assertSame(Pattern.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
		assertEquals("Invalid bar code.", violation.getMessage());
		assertEquals(1, violations.size());
	}
	
	private static Stream<Arguments> getInvalidBarCodes()
	{
		return Stream.of(
				Arguments.of(""),
				Arguments.of("3419980002010435200877102011000419107001000O"),
				Arguments.of("341998000201043520087710201100041910700100000"),
				Arguments.of("3419980002010435200877102011000419107001000"),
				Arguments.of("-34199800020104352008771020110004191070010000"),
				Arguments.of("34199800020104352008771020110004191070010000L"),
				Arguments.of("-3419980002010435200877102011000419107001000"),
				Arguments.of("asdasdasdasdasdasdasdasdasdasdasdasdasdasdas"));
	}
	
	@Test
	void testNotNullPaymentDateValidation()
	{
		BillDTO billDTO = new BillDTO(validBarCode, null);
		
		Set<ConstraintViolation<BillDTO>> violations = validator.validate(billDTO);
		
		ConstraintViolation<BillDTO> violation = violations.iterator().next();
		
		assertSame(NotNull.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
		assertEquals("Payment date must not be null.", violation.getMessage());
		assertEquals(1, violations.size());
	}
	
	@ParameterizedTest
	@MethodSource("getPastDates")
	void testFutureOrPresetPaymentDateValidation(LocalDate pastDate)
	{
		BillDTO billDTO = new BillDTO(validBarCode, pastDate);
		
		Set<ConstraintViolation<BillDTO>> violations = validator.validate(billDTO);
		
		ConstraintViolation<BillDTO> violation = violations.iterator().next();
		
		assertSame(FutureOrPresent.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
		assertEquals("Payment date must be a date in the present or in the future.", violation.getMessage());
		assertEquals(1, violations.size());
	}
	
	private static Stream<Arguments> getPastDates()
	{
		return Stream.of(
				Arguments.of(LocalDate.now().minusDays(1L)),
				Arguments.of(LocalDate.now().minusDays(30L)),
				Arguments.of(LocalDate.MIN));
	}
}
