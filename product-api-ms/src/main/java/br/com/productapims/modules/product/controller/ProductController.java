package br.com.productapims.modules.product.controller;

import br.com.productapims.config.exception.SuccessResponse;
import br.com.productapims.modules.category.dto.CategoryRequest;
import br.com.productapims.modules.category.dto.CategoryResponse;
import br.com.productapims.modules.category.service.CategoryService;
import br.com.productapims.modules.product.dto.ProductRequest;
import br.com.productapims.modules.product.dto.ProductResponse;
import br.com.productapims.modules.product.dto.ProductSalesResponse;
import br.com.productapims.modules.product.service.ProductService;
import br.com.productapims.modules.supplier.dto.SupplierResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping
    public ProductResponse save(@RequestBody ProductRequest request){
        return productService.save(request);
    }

    @GetMapping
    public List<ProductResponse> findAll(){
        return productService.findAll();
    }
    @GetMapping("{id}")
    public ProductResponse findById(@PathVariable Integer id){
        return productService.findByIdResponse(id);
    }

    @GetMapping("name/{name}")
    public List<ProductResponse> findByName(@PathVariable String name) {
        return productService.findByName(name);
    }
    @GetMapping("category/{categoryId}")
    public List<ProductResponse> findByCategoryId(@PathVariable Integer categoryId) {
        return productService.findByCategoryId(categoryId);
    }
    @GetMapping("supplier/{supplierId}")
    public List<ProductResponse> findBySupplierId(@PathVariable Integer supplierId) {
        return productService.findBySupplierId(supplierId);
    }
    @PutMapping("{id}")
    public ProductResponse update (@RequestBody ProductRequest request ,@PathVariable Integer id){
        return productService.update(request, id);
    }
    @DeleteMapping("{id}")
    public SuccessResponse delete (@PathVariable Integer id){
        return productService.delete(id);
    }

    @GetMapping("{id}/sales")
    public ProductSalesResponse findProductsSales(@PathVariable Integer id){
        return productService.findProductSales(id);
    }
}
