package cruzapi.adapter.mapper;

import org.springframework.stereotype.Service;

import cruzapi.adapter.dto.BillDetailsDTO;
import cruzapi.core.entity.BillDetails;

@Service
public class BillDetailsMapper
{
	public BillDetails toEntity(BillDetailsDTO b)
	{
		return BillDetails.builder()
				.code(b.getCode())
				.dueDate(b.getDueDate())
				.amount(b.getAmount())
				.recipientName(b.getRecipientName())
				.recipientDocument(b.getRecipientDocument())
				.type(b.getType())
				.build();
	}
}
