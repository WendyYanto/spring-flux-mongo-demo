package com.example.mongoflux.demo.code.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

public class Response<T> implements Serializable {
    private static final long serialVersionUID = -2146181089492014495L;
    private int code;
    private String message;
    private T value;
}
