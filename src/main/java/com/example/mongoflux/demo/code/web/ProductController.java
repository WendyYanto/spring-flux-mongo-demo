package com.example.mongoflux.demo.code.web;

import com.example.mongoflux.demo.code.constants.ResponseCode;
import com.example.mongoflux.demo.code.entity.ListResponse;
import com.example.mongoflux.demo.code.entity.Product;
import com.example.mongoflux.demo.code.entity.ProductDeleteRequest;
import com.example.mongoflux.demo.code.entity.Response;
import com.example.mongoflux.demo.code.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(value = "/product", method = {RequestMethod.POST},
            produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Mono<Response<Product>> save(@RequestBody Product product) {
        return productService
                .saveProduct(product)
                .onErrorResume(e ->
                    Mono.fromCallable(() -> Response.<Product>builder()
                            .code(ResponseCode.SERVER_ERROR_CODE.getCode())
                            .message(e.getMessage())
                            .value(null)
                            .build()));
    }

    @RequestMapping(value = "/products", method = {RequestMethod.GET}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Mono<ListResponse<Product>> getProducts() {
        return productService.getProducts();
    }

    @RequestMapping(value = "/products/price", method = {RequestMethod.GET}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Mono<ListResponse<String>> getPrices() {
        return productService.getPrices();
    }

    @RequestMapping(value = "/product/{name}", method = {RequestMethod.GET}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Mono<Response<Product>> getProductsByName(@PathVariable(value = "name") String name) {
        return productService.findProductByName(name);
    }

    @RequestMapping(value = "/product", method = {RequestMethod.DELETE}, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Mono<Response<String>> deleteProductByName(@RequestBody ProductDeleteRequest productDeleteRequest) {
        return productService.deleteProductByName(productDeleteRequest);
    }

    @RequestMapping(value = "/products/count", method = {RequestMethod.GET}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Mono<Response<Long>> getProductCounts() {
        return productService.getProductCount();
    }
}
