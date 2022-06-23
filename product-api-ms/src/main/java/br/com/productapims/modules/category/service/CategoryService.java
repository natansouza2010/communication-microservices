package br.com.productapims.modules.category.service;


import br.com.productapims.config.exception.ValidationException;
import br.com.productapims.modules.category.dto.CategoryRequest;
import br.com.productapims.modules.category.dto.CategoryResponse;
import br.com.productapims.modules.category.model.Category;
import br.com.productapims.modules.category.repository.CategoryRepository;
import br.com.productapims.modules.supplier.model.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;


@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Category findById(Integer id){
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ValidationException("There's no category for the given ID."));
    }

    public CategoryResponse save(CategoryRequest categoryRequest){

        validateCategoryNameInformed(categoryRequest);
        var category = categoryRepository.save(Category.of(categoryRequest));

        return CategoryResponse.of(category);
    }


    private void validateCategoryNameInformed(CategoryRequest categoryRequest){
        if(isEmpty(categoryRequest.getDescription())){
            throw new ValidationException("The category description was not informed.");

        }
    }

}
