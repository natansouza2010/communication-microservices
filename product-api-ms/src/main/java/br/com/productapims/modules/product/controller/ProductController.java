package br.com.productapims.modules.product.controller;

import br.com.productapims.modules.category.dto.CategoryRequest;
import br.com.productapims.modules.category.dto.CategoryResponse;
import br.com.productapims.modules.category.service.CategoryService;
import br.com.productapims.modules.product.dto.ProductRequest;
import br.com.productapims.modules.product.dto.ProductResponse;
import br.com.productapims.modules.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping
    public ProductResponse save(@RequestBody ProductRequest request){
        return productService.save(request);
    }
}
