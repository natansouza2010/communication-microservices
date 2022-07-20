package br.com.productapims.modules.product.service;


import br.com.productapims.config.exception.SuccessResponse;
import br.com.productapims.config.exception.ValidationException;
import br.com.productapims.modules.category.dto.CategoryRequest;
import br.com.productapims.modules.category.dto.CategoryResponse;
import br.com.productapims.modules.category.model.Category;
import br.com.productapims.modules.category.repository.CategoryRepository;
import br.com.productapims.modules.category.service.CategoryService;
import br.com.productapims.modules.product.dto.*;
import br.com.productapims.modules.product.model.Product;
import br.com.productapims.modules.product.repository.ProductRepository;

import br.com.productapims.modules.sales.client.SalesClient;
import br.com.productapims.modules.sales.dto.SalesConfirmationDTO;
import br.com.productapims.modules.sales.enums.SalesStatus;
import br.com.productapims.modules.sales.rabbitmq.SalesConfirmationSender;
import br.com.productapims.modules.supplier.dto.SupplierResponse;
import br.com.productapims.modules.supplier.service.SupplierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.productapims.config.RequestUtil.getCurrentRequest;
import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Service
public class ProductService {

    private static final Integer ZERO = 0;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private SalesConfirmationSender salesConfirmationSender;

    @Autowired
    private SalesClient salesClient;

    public ProductResponse save (ProductRequest request){
        validateProductDataInformed(request);
        validateCategoryAndSupplierIdInformed(request);
        var category = categoryService.findById(request.getCategoryId());
        var supplier = supplierService.findById(request.getSupplierId());
        var product = productRepository.save(Product.of(request, supplier, category));
        return ProductResponse.of(product);
    }

    public ProductResponse update (ProductRequest request, Integer id){
        validateProductDataInformed(request);
        validateInformedId(id);
        validateCategoryAndSupplierIdInformed(request);
        var category = categoryService.findById(request.getCategoryId());
        var supplier = supplierService.findById(request.getSupplierId());
        var product = (Product.of(request, supplier, category));
        product.setId(id);
        productRepository.save(product);
        return ProductResponse.of(product);
    }


    public List<ProductResponse> findByName(String name){
        if(isEmpty(name)){
            throw new ValidationException("The product name must be informed");
        }
        return productRepository.findByNameIgnoreCaseContaining(name).stream().map(product -> ProductResponse.of(product)).collect(Collectors.toList());
    }

    public List<ProductResponse> findBySupplierId(Integer supplierId){
        if(isEmpty(supplierId)){
            throw new ValidationException("The product's supplier ID must be informed");
        }
        return productRepository.findBySupplierId(supplierId)
                .stream().map(product -> ProductResponse.of(product)).collect(Collectors.toList());
    }

    public List<ProductResponse> findByCategoryId(Integer categoryId){
        if(isEmpty(categoryId)){
            throw new ValidationException("The product's category ID must be informed");
        }
        return productRepository.findByCategoryId(categoryId)
                .stream().map(product -> ProductResponse.of(product)).collect(Collectors.toList());
    }

    public List<ProductResponse> findAll(){
        return productRepository.findAll().stream().map(product -> ProductResponse.of(product)).collect(Collectors.toList());
    }

    public Product findById(Integer id) {
        validateInformedId(id);
        return productRepository.findById(id).orElseThrow( () -> new ValidationException("There's no product for the given ID."));
    }

    public ProductResponse findByIdResponse(Integer id) {
        return ProductResponse.of(findById(id));
    }

    public Boolean existsByCategoryId(Integer categoryId){
        return productRepository.existsByCategoryId(categoryId);

    }
    public Boolean existsBySupplierId(Integer supplierId){
        return productRepository.existsBySupplierId(supplierId);

    }

    public SuccessResponse delete(Integer id){
        validateInformedId(id);
        productRepository.deleteById(id);
        return SuccessResponse.create("The product was deleted");

    }



    public void updateProductStock(ProductStockDTO product){
        try{
            validateStockUpdateData(product);
            updateStock(product);

        }catch (Exception e){
            log.error("Error while trying to update stock for message with error {}", e.getMessage(), e);
            var rejectedMessage = new SalesConfirmationDTO(product.getSalesId(), SalesStatus.REJECTED, product.getTransactionid());
            salesConfirmationSender.sendSalesConfirmationMessage(rejectedMessage);

        }


    }
    @Transactional
    private void updateStock(ProductStockDTO product){
        var productsForUpdate = new ArrayList<Product>();

        product.getProducts()
                .forEach(salesProduct-> {
                    var existingProduct = findById(salesProduct.getProductId());
                    validateQuantityInStock(salesProduct, existingProduct);
                    existingProduct.updateStock(salesProduct.getQuantity());
                    productsForUpdate.add(existingProduct);
//                    productRepository.save(existingProduct);
                });
        if(!isEmpty(productsForUpdate)){
            productRepository.saveAll(productsForUpdate);
            var approvedMessage = new SalesConfirmationDTO(product.getSalesId(), SalesStatus.APPROVED, product.getTransactionid());
            salesConfirmationSender.sendSalesConfirmationMessage(approvedMessage);
        }

    }

    @Transactional
    private void validateQuantityInStock(ProductQuantityDTO salesProduct, Product existingProduct){
        if(salesProduct.getQuantity() > existingProduct.getQuantityAvailable()) {
            throw new ValidationException(
                    String.format("The product %s is out of stock", existingProduct.getId())
            );
        }
    }

    @Transactional
    private void validateStockUpdateData(ProductStockDTO product){
        if(isEmpty(product) || isEmpty(product.getSalesId())){
            throw new ValidationException("The product data and the sales ID must be informed. ");

        }
        if(isEmpty(product.getProducts())){
            throw new ValidationException("The sales' products must be informed. ");
        }
        product.getProducts().forEach(
                salesProduct-> {
                    if(isEmpty(salesProduct.getQuantity()) || isEmpty(salesProduct.getProductId())){
                        throw new ValidationException("The productID and the quantity must be informed");
                    }
                }
        );
    }

    private void validateInformedId(Integer id){
        if(isEmpty(id)){
            throw new ValidationException("The product ID must be informed.");
        }
    }


    private void validateProductDataInformed(ProductRequest productRequest){
        if(isEmpty(productRequest.getName())){
            throw new ValidationException("The product name was not informed.");
        }
        if(isEmpty(productRequest.getQuantityAvailable())){
            throw new ValidationException("The product quantity was not informed.");
        }
        if(productRequest.getQuantityAvailable() <= ZERO){
            throw new ValidationException("The quantity should not be less or equal to zero.");
        }
    }

    private void validateCategoryAndSupplierIdInformed(ProductRequest productRequest){
        if(isEmpty(productRequest.getCategoryId()) ){
            throw new ValidationException("The category ID was not informed.");
        }
        if(isEmpty(productRequest.getSupplierId())){
            throw new ValidationException("The supplier ID was not informed.");
        }
    }

    public SuccessResponse checkProductsStock(ProductCheckStockRequest request){
        var currentRequest = getCurrentRequest();
        log.info("`Request to POST login with data ${JSON.stringify(req.body)} | transactionID: ${transactionid} | serviceID: ${serviceid}`");
        if(isEmpty(request) || isEmpty(request.getProducts())){
            throw new ValidationException("The request data and products must be informed.");
        }
        request.getProducts().forEach(this::validateStock);
        return SuccessResponse.create("The is stock is ok ! ");





    }

    private void validateStock(ProductQuantityDTO productQuantity){
        if(isEmpty(productQuantity.getProductId()) || isEmpty(productQuantity.getQuantity())){
            throw new ValidationException("Product ID and quantity must be informed");
        }
        var product = findById(productQuantity.getProductId());
        if(productQuantity.getQuantity() > product.getQuantityAvailable() ){
            throw new ValidationException(String.format("The product %s is out of stock.", product.getId()));


        }

    }

    public ProductSalesResponse findProductSales(Integer id){
        var product = findById(id);
        try{
            var sales = salesClient.findSalesByProductId(product.getId()).orElseThrow(
                    ()-> new ValidationException("The sales was not found by this product.")
            );

            return ProductSalesResponse.of(product, sales.getSalesIds());
        }catch (Exception e){
            throw new ValidationException("There was an error trying to get the products's sales.");
        }
    }



}
