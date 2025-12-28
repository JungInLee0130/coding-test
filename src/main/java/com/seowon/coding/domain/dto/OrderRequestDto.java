package com.seowon.coding.domain.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderRequestDto {
    private String customerName;
    private String customerEmail;
    private List<Long> productIds;
    private List<Integer> quantities;
}