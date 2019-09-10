package com.example.mongoflux.demo.code.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ProductValidationException extends RuntimeException {
    private static final long serialVersionUID = -4990975462756714400L;

    public ProductValidationException(String message) {
        super(message);
    }
}
