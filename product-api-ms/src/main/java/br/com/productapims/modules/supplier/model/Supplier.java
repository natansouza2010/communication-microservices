package br.com.productapims.modules.supplier.model;


import br.com.productapims.modules.category.dto.CategoryRequest;
import br.com.productapims.modules.category.model.Category;
import br.com.productapims.modules.supplier.dto.SupplierRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="SUPPLIER")

public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    @Column(name = "NAME", nullable = false)
    private String name;



    public static Supplier of(SupplierRequest supplierRequest){
        var supplier = new Supplier();
        BeanUtils.copyProperties(supplierRequest, supplier);
        return supplier;
    }

}
