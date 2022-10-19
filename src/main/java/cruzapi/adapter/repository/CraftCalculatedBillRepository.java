package cruzapi.adapter.repository;

import javax.transaction.Transactional;

import cruzapi.core.entity.CalculatedBill;
import cruzapi.core.port.CalculatedBillRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CraftCalculatedBillRepository implements CalculatedBillRepository
{
	private final SpringCalculatedBillRepository repository;
	
	@Override
	@Transactional
	public CalculatedBill save(CalculatedBill entity)
	{
		return repository.save(entity);
	}
}
