package cruzapi.adapter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cruzapi.core.entity.CalculatedBill;

public interface SpringCalculatedBillRepository extends JpaRepository<CalculatedBill, String>
{
	
}
