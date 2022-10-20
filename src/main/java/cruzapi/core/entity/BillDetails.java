package cruzapi.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BillDetails implements Cloneable, Serializable
{
	private static final long serialVersionUID = 1L;
	
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
	
	@Override
	public BillDetails clone()
	{
		try
		{
			return (BillDetails) super.clone();
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}
