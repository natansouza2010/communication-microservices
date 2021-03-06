package br.com.productapims.modules.product.model;


import br.com.productapims.modules.category.model.Category;
import br.com.productapims.modules.product.dto.ProductRequest;
import br.com.productapims.modules.supplier.dto.SupplierRequest;
import br.com.productapims.modules.supplier.model.Supplier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "PRODUCT")
@Table(name ="PRODUCT")
@Builder
@Slf4j
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    @Column(name = "NAME", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "FK_SUPPLIER", nullable = false)
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "FK_CATEGORY", nullable = false)
    private Category category;

    @Column(name = "QUANTITY_AVAILABLE", nullable = false)
    private Integer quantityAvailable;

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist(){
        createdAt = LocalDateTime.now();
    }


    public static Product of(ProductRequest productRequest, Supplier supplier, Category category){
        return Product
                .builder()
                .name(productRequest.getName())
                .quantityAvailable(productRequest.getQuantityAvailable())
                .category(category)
                .supplier(supplier)
                .build();

    }

    public void updateStock(Integer quantity){
        this.quantityAvailable = this.quantityAvailable - quantity;
        log.info(String.format("Stock Available %d from product'ID %d", quantityAvailable, id));
    }
}


