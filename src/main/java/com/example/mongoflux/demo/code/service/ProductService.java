package com.example.mongoflux.demo.code.service;

import com.example.mongoflux.demo.code.entity.ListResponse;
import com.example.mongoflux.demo.code.entity.Product;
import com.example.mongoflux.demo.code.entity.ProductDeleteRequest;
import com.example.mongoflux.demo.code.entity.Response;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {
    Mono<ListResponse<Product>> getProducts();

    Mono<Response<Product>> findProductByName(String name);

    Mono<Response<Product>> saveProduct(Product product);

    Mono<ListResponse<String>> getPrices();

    Mono<Response<String>> deleteProductByName(ProductDeleteRequest productDeleteRequest);

    Mono<Response<Long>> getProductCount();
}
