package br.com.productapims.modules.product.dto;


import br.com.productapims.modules.category.dto.CategoryResponse;
import br.com.productapims.modules.supplier.dto.SupplierResponse;
import lombok.Data;

@Data
public class ProductRequest {
    private String name;
    private Integer quantityAvailable;
    private Integer supplierId;
    private Integer categoryId;
}
