package br.com.productapims.modules.produto.repository;

import br.com.productapims.modules.produto.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
