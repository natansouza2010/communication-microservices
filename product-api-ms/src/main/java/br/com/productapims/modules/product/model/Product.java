package br.com.productapims.modules.product.model;


import br.com.productapims.modules.category.model.Category;
import br.com.productapims.modules.product.dto.ProductRequest;
import br.com.productapims.modules.supplier.dto.SupplierRequest;
import br.com.productapims.modules.supplier.model.Supplier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "PRODUCT")
@Table(name ="PRODUCT")
@Builder
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

    @Column(name = "CREATE_AT", nullable = false, updatable = false)
    private LocalDateTime createAt;

    @PrePersist
    public void prePersist(){
        createAt = LocalDateTime.now();
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
}


