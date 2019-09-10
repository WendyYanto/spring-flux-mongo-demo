package com.example.mongoflux.demo.code.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor

public class ListResponse<T> implements Serializable {
    private static final long serialVersionUID = 4348235908366704811L;
    private int code;
    private String message;
    private List<T> content;
}
