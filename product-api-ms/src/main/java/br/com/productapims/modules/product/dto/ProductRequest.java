package br.com.productapims.modules.product.dto;


import br.com.productapims.modules.category.dto.CategoryResponse;
import br.com.productapims.modules.supplier.dto.SupplierResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductRequest {
    private String name;
    @JsonProperty("quantity_available")
    private Integer quantityAvailable;
    private Integer supplierId;
    private Integer categoryId;
}
