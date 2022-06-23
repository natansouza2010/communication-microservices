package br.com.productapims.modules.product.service;


import br.com.productapims.config.exception.ValidationException;
import br.com.productapims.modules.category.dto.CategoryRequest;
import br.com.productapims.modules.category.dto.CategoryResponse;
import br.com.productapims.modules.category.model.Category;
import br.com.productapims.modules.category.repository.CategoryRepository;
import br.com.productapims.modules.category.service.CategoryService;
import br.com.productapims.modules.product.dto.ProductRequest;
import br.com.productapims.modules.product.dto.ProductResponse;
import br.com.productapims.modules.product.model.Product;
import br.com.productapims.modules.product.repository.ProductRepository;

import br.com.productapims.modules.supplier.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class ProductService {

    private static final Integer ZERO = 0;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SupplierService supplierService;

    public ProductResponse save (ProductRequest request){
        validateProductDataInformed(request);
        validateCategoryAndSupplierIdInformed(request);
        var category = categoryService.findById(request.getCategoryId());
        var supplier = supplierService.findById(request.getSupplierId());
        var product = productRepository.save(Product.of(request, supplier, category));
        return ProductResponse.of(product);
    }


    private void validateProductDataInformed(ProductRequest productRequest){
        if(isEmpty(productRequest.getName())){
            throw new ValidationException("The product name was not informed.");
        }
        if(isEmpty(productRequest.getQuantityAvailable())){
            throw new ValidationException("The product quantity was not informed.");
        }
        if(productRequest.getQuantityAvailable() <= ZERO){
            throw new ValidationException("The quantity should not be less or equal to zero.");
        }
    }

    private void validateCategoryAndSupplierIdInformed(ProductRequest productRequest){
        if(isEmpty(productRequest.getCategoryId()) ){
            throw new ValidationException("The category ID was not informed.");
        }
        if(isEmpty(productRequest.getSupplierId())){
            throw new ValidationException("The supplier ID was not informed.");
        }
    }



}
