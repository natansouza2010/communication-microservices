package br.com.productapims.modules.category.service;


import br.com.productapims.config.exception.SuccessResponse;
import br.com.productapims.config.exception.ValidationException;
import br.com.productapims.modules.category.dto.CategoryRequest;
import br.com.productapims.modules.category.dto.CategoryResponse;
import br.com.productapims.modules.category.model.Category;
import br.com.productapims.modules.category.repository.CategoryRepository;
import br.com.productapims.modules.product.service.ProductService;
import br.com.productapims.modules.supplier.model.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;


@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductService productService;


    public Category findById(Integer id){
        if(isEmpty(id)){
            throw new ValidationException("The category id was not informed");
        }
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ValidationException("There's no category for the given ID."));
    }


    public CategoryResponse save(CategoryRequest categoryRequest){

        validateCategoryNameInformed(categoryRequest);
        var category = categoryRepository.save(Category.of(categoryRequest));

        return CategoryResponse.of(category);
    }
    public CategoryResponse update(CategoryRequest categoryRequest, Integer id){

        validateCategoryNameInformed(categoryRequest);
        validateInformedId(id);
        var category = Category.of(categoryRequest);
        category.setId(id);
        categoryRepository.save(category);

        return CategoryResponse.of(category);
    }

    public List<CategoryResponse> findByDescription(String description){
        if(isEmpty(description)){
            throw new ValidationException("The category description must be informed");
        }
        return categoryRepository.findByDescriptionIgnoreCaseContaining(description)
                .stream().map(CategoryResponse::of).collect(Collectors.toList());
    }

    public List<CategoryResponse> findAll(){
        return categoryRepository.findAll().stream().map(category -> CategoryResponse.of(category)).collect(Collectors.toList());
    }

    public CategoryResponse findByIdResponse(Integer id){
        return CategoryResponse.of(findById(id));
    }


    public SuccessResponse delete(Integer id){
        validateInformedId(id);
        if(productService.existsByCategoryId(id)){
            throw new ValidationException("You cannot delete this category because it's already defined by a product.");
        }
        categoryRepository.deleteById(id);
        return SuccessResponse.create("The category was deleted");

    }

    private void validateInformedId(Integer id){
        if(isEmpty(id)){
            throw new ValidationException("The category ID must be informed.");
        }
    }



    private void validateCategoryNameInformed(CategoryRequest categoryRequest){
        if(isEmpty(categoryRequest.getDescription())){
            throw new ValidationException("The category description was not informed.");

        }
    }

}
