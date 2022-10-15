package cruzapi.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import cruzapi.model.CalculatedBillPayment;

public interface CalculatedBillRepository extends JpaRepository<CalculatedBillPayment, UUID>
{
	
}
