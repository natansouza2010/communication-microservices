package br.com.productapims.modules.supplier.service;


import br.com.productapims.config.exception.SuccessResponse;
import br.com.productapims.config.exception.ValidationException;
import br.com.productapims.modules.category.dto.CategoryRequest;
import br.com.productapims.modules.category.dto.CategoryResponse;
import br.com.productapims.modules.category.model.Category;
import br.com.productapims.modules.category.repository.CategoryRepository;
import br.com.productapims.modules.product.service.ProductService;
import br.com.productapims.modules.supplier.dto.SupplierRequest;
import br.com.productapims.modules.supplier.dto.SupplierResponse;
import br.com.productapims.modules.supplier.model.Supplier;
import br.com.productapims.modules.supplier.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;


@Service
public class SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ProductService productService;


    public Supplier findById(Integer id){
        validateInformedId(id);
        return supplierRepository.findById(id)
                .orElseThrow(() -> new ValidationException("There's no supplier for the given ID."));
    }


    public SupplierResponse save(SupplierRequest supplierRequest){

        validateSupplierNameInformed(supplierRequest);
        var supplier = supplierRepository.save(Supplier.of(supplierRequest));

        return SupplierResponse.of(supplier);
    }
    public SupplierResponse update(SupplierRequest supplierRequest, Integer supplierId){

        validateSupplierNameInformed(supplierRequest);
        validateInformedId(supplierId);
        var supplier = Supplier.of(supplierRequest);
        supplier.setId(supplierId);
        supplierRepository.save(supplier);
        return SupplierResponse.of(supplier);
    }

    public SupplierResponse findByIdResponse(Integer id){
        return SupplierResponse.of(findById(id));
    }

    public List<SupplierResponse> findByName(String name){
        if(isEmpty(name)){
            throw new ValidationException("The supplier name must be informed. ");
        }
        return supplierRepository.findByNameIgnoreCaseContaining(name).stream().map(supplier -> SupplierResponse.of(supplier)).collect(Collectors.toList());
    }

    public List<SupplierResponse> findAll(){
        return supplierRepository.findAll().stream().map(supplier -> SupplierResponse.of(supplier)).collect(Collectors.toList());
    }




    private void validateSupplierNameInformed(SupplierRequest supplierRequest){
        if(isEmpty(supplierRequest.getName())){
            throw new ValidationException("The supplier name was not informed.");

        }
    }

    public SuccessResponse delete(Integer id){
        validateInformedId(id);
        if(productService.existsBySupplierId(id)){
            throw new ValidationException("You cannot delete this supplier because it's already defined by a product.");
        }
        supplierRepository.deleteById(id);
        return SuccessResponse.create("The supplier was deleted");

    }

    private void validateInformedId(Integer id){
        if(isEmpty(id)){
            throw new ValidationException("The supplier ID must be informed.");
        }
    }
}
