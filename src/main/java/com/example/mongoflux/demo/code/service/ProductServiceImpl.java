package com.example.mongoflux.demo.code.service;

import com.example.mongoflux.demo.code.constants.MongoCode;
import com.example.mongoflux.demo.code.constants.ResponseCode;
import com.example.mongoflux.demo.code.entity.ListResponse;
import com.example.mongoflux.demo.code.entity.Product;
import com.example.mongoflux.demo.code.entity.ProductDeleteRequest;
import com.example.mongoflux.demo.code.entity.Response;
import com.example.mongoflux.demo.code.exception.ProductValidationException;
import com.example.mongoflux.demo.code.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ReactiveRedisOperations<String, Product> productReactiveRedisOperations;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ReactiveRedisOperations<String, Product> productReactiveRedisOperations) {
        this.productRepository = productRepository;
        this.productReactiveRedisOperations = productReactiveRedisOperations;
    }

    @Override
    public Mono<ListResponse<Product>> getProducts() {
        return productRepository
            .findAll()
            .doOnNext(this::cacheProduct)
            .collectList()
            .map(this::mapToProductListResponse);
    }

    @Override
    public Mono<Response<Product>> findProductByName(String name) {
        return Mono.fromCallable(() -> name)
            .flatMap(this::findProductName)
            .doOnNext(this::cacheProduct)
            .map(this::mapToProductResponse);
    }

    @Override
    public Mono<Response<Product>> saveProduct(Product product) {
        return Mono.fromCallable(() -> product)
            .map(this::validateProduct)
            .flatMap(productRepository::save)
            .map(this::mapToProductResponse);
    }

    @Override
    public Mono<ListResponse<String>> getPrices() {
        return productRepository
            .findAll()
            .map(Product::getPrice)
            .flatMap(this::mapToRupiah)
            .collect(Collectors.toList())
            .map(this::mapToPriceListResponse);
    }

    @Override
    public Mono<Response<String>> deleteProductByName(ProductDeleteRequest productDeleteRequest) {
        return productRepository
            .deleteProductByName(productDeleteRequest.getName())
            .map(MongoCode.SUCCESS::equals)
            .map(this::mapToSuccessResponse);
    }

    @Override
    public Mono<Response<Long>> getProductCount() {
        return productRepository
            .count()
            .map(this::mapToProductCountResponse);
    }

    private Mono<String> mapToRupiah(Double price) {
        return Mono.fromCallable(() -> "Rp." + price);
    }

    private ListResponse<String> mapToPriceListResponse(List<String> prices) {
        return ListResponse.<String>builder()
                .code(ResponseCode.SUCCESS_CODE.getCode())
                .message(ResponseCode.SUCCESS_CODE.getMessage())
                .content(prices)
                .build();
    }

    private Response<Product> mapToProductResponse(Product product) {
        return Response.<Product>builder()
                .code(ResponseCode.SUCCESS_CODE.getCode())
                .message(ResponseCode.SUCCESS_CODE.getMessage())
                .value(product)
                .build();
    }

    private ListResponse<Product> mapToProductListResponse(List<Product> products) {
        return ListResponse.<Product>builder()
                .code(ResponseCode.SUCCESS_CODE.getCode())
                .message(ResponseCode.SUCCESS_CODE.getMessage())
                .content(products)
                .build();
    }

    private Product validateProduct(Product product) {
        if (product.getQuantity() < 0) {
            throw new ProductValidationException("Quantity Have To Be Larger Than Zero");
        } else if (product.getPrice() < 0.0) {
            throw new ProductValidationException("Price Have To Be Larger Than Zero");
        } else if (product.getName().trim().isEmpty()) {
            throw new ProductValidationException("Name Cannot Be Empty");
        }
        return product;
    }

    private Response<String> mapToSuccessResponse(Boolean success) {
        Response<String> response = new Response<>();
        if (success) {
            response.setValue("Success");
            response.setMessage(ResponseCode.SUCCESS_CODE.getMessage());
            response.setCode(ResponseCode.SUCCESS_CODE.getCode());
        } else {
            response.setValue("Error");
            response.setMessage(ResponseCode.SERVER_ERROR_CODE.getMessage());
            response.setCode(ResponseCode.SERVER_ERROR_CODE.getCode());
        }
        return response;
    }

    private Response<Long> mapToProductCountResponse(long count) {
        return Response.<Long>builder()
            .value(count)
            .code(ResponseCode.SUCCESS_CODE.getCode())
            .message(ResponseCode.SUCCESS_CODE.getMessage())
            .build();
    }

    private void cacheProduct(Product product) {
        productReactiveRedisOperations
            .opsForValue()
            .set(product.getName(), product)
            .subscribe();
    }

    private Mono<Product> findProductName(String name) {
        return productReactiveRedisOperations
            .opsForValue()
            .get(name)
            .switchIfEmpty(Mono.defer(() -> this.findProductFromRepository(name)));
    }

    private Mono<Product> findProductFromRepository(String name) {
        return productRepository
            .findFirstByName(name);
    }
}
