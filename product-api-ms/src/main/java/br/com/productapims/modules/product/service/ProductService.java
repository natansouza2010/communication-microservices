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

import java.util.List;
import java.util.stream.Collectors;

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


    public List<ProductResponse> findByName(String name){
        if(isEmpty(name)){
            throw new ValidationException("The product name must be informed");
        }
        return productRepository.findByNameIgnoreCaseContaining(name).stream().map(product -> ProductResponse.of(product)).collect(Collectors.toList());
    }

    public List<ProductResponse> findBySupplierId(Integer supplierId){
        if(isEmpty(supplierId)){
            throw new ValidationException("The product's supplier ID must be informed");
        }
        return productRepository.findBySupplierId(supplierId)
                .stream().map(product -> ProductResponse.of(product)).collect(Collectors.toList());
    }

    public List<ProductResponse> findByCategoryId(Integer categoryId){
        if(isEmpty(categoryId)){
            throw new ValidationException("The product's category ID must be informed");
        }
        return productRepository.findByCategoryId(categoryId)
                .stream().map(product -> ProductResponse.of(product)).collect(Collectors.toList());
    }

    public List<ProductResponse> findAll(){
        return productRepository.findAll().stream().map(product -> ProductResponse.of(product)).collect(Collectors.toList());
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
