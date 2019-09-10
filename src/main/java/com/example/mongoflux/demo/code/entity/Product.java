package com.example.mongoflux.demo.code.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
@Data
@Document(collection = "product")
public class Product {
    private String name;
    private Integer quantity;
    private Double price;
}
