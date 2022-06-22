package br.com.productapims.modules.supplier.repository;

import br.com.productapims.modules.supplier.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
}
