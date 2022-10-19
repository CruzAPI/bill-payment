package cruzapi.core.entity;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BillDetails
{
	private final String code;
	private final LocalDate dueDate;
	private final BigDecimal amount;
	private final String recipientName;
	private final String recipientDocument;
	private final Type type;
	
	public enum Type
	{
		NPC, NORMAL;
	}
	
	public boolean isExpired(Clock clock)
	{
		return dueDate.isBefore(LocalDate.now(clock));
	}
}
