package br.com.productapims.modules.supplier.service;


import br.com.productapims.config.exception.ValidationException;
import br.com.productapims.modules.category.dto.CategoryRequest;
import br.com.productapims.modules.category.dto.CategoryResponse;
import br.com.productapims.modules.category.model.Category;
import br.com.productapims.modules.category.repository.CategoryRepository;
import br.com.productapims.modules.supplier.dto.SupplierRequest;
import br.com.productapims.modules.supplier.dto.SupplierResponse;
import br.com.productapims.modules.supplier.model.Supplier;
import br.com.productapims.modules.supplier.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;


@Service
public class SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;


    public Supplier findById(Integer id){
        return supplierRepository.findById(id)
                .orElseThrow(() -> new ValidationException("There's no supplier for the given ID."));
    }


    public SupplierResponse save(SupplierRequest supplierRequest){

        validateSupplierNameInformed(supplierRequest);
        var supplier = supplierRepository.save(Supplier.of(supplierRequest));

        return SupplierResponse.of(supplier);
    }


    private void validateSupplierNameInformed(SupplierRequest supplierRequest){
        if(isEmpty(supplierRequest.getName())){
            throw new ValidationException("The supplier name was not informed.");

        }
    }

}
