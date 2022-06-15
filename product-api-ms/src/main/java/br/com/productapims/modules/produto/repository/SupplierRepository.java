package br.com.productapims.modules.produto.repository;

import br.com.productapims.modules.produto.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
}
