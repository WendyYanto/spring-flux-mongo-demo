package com.example.mongoflux.demo.code.service;

import com.example.mongoflux.demo.code.entity.ListResponse;
import com.example.mongoflux.demo.code.entity.Product;
import com.example.mongoflux.demo.code.entity.Response;
import com.example.mongoflux.demo.code.exception.ProductValidationException;
import com.example.mongoflux.demo.code.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Mono<ListResponse<Product>> getProducts() {
        return productRepository
                .findAll()
                .collectList()
                .map(this::mapToProductListResponse);
    }

    @Override
    public Mono<Product> findProductByName() {
        return null;
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

    private Mono<String> mapToRupiah(Double price) {
        return Mono.fromCallable(() -> "Rp." + price);
    }

    private ListResponse<String> mapToPriceListResponse(List<String> prices) {
        return ListResponse.<String>builder()
                .code(200)
                .message("Success")
                .content(prices)
                .build();
    }

    private Response<Product> mapToProductResponse(Product product) {
        return Response.<Product>builder()
                .value(product)
                .message("Success")
                .code(200)
                .build();
    }

    private ListResponse<Product> mapToProductListResponse(List<Product> products) {
        return ListResponse.<Product>builder()
                .content(products)
                .message("Success")
                .code(200)
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
}
