package com.seowon.coding.domain.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderRequest {
    private String customerName;
    private String customerEmail;
    private List<ProductRequest> products;
}