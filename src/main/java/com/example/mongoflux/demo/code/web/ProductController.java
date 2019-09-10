package com.example.mongoflux.demo.code.web;

import com.example.mongoflux.demo.code.entity.ListResponse;
import com.example.mongoflux.demo.code.entity.Product;
import com.example.mongoflux.demo.code.entity.Response;
import com.example.mongoflux.demo.code.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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
                    .code(500)
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
}
