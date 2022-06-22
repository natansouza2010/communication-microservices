package br.com.productapims.modules.category.model;

import br.com.productapims.modules.category.dto.CategoryRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="CATEGORY")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;


    public static Category of(CategoryRequest categoryRequest){
        var category = new Category();
        BeanUtils.copyProperties(category, category);
        return category;
    }


}
