package br.com.productapims.modules.produto.repository;

import br.com.productapims.modules.produto.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
