package com.example.mongoflux.demo.code.repository;

import com.example.mongoflux.demo.code.entity.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository public interface ProductRepository extends ReactiveMongoRepository<Product, Integer> {
    Mono<Product> findFirstByName(String name);
    Mono<Long> deleteProductByName(String name);
}
