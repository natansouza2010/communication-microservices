package br.com.productapims.modules.category.repository;

import br.com.productapims.modules.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
