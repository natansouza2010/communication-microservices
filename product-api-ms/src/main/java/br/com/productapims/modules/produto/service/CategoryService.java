package br.com.productapims.modules.produto.service;


import br.com.productapims.config.exception.ValidationException;
import br.com.productapims.modules.produto.dto.CategoryRequest;
import br.com.productapims.modules.produto.dto.CategoryResponse;
import br.com.productapims.modules.produto.model.Category;
import br.com.productapims.modules.produto.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;


@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;


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
