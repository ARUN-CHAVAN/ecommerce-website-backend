package com.nt.dto;

import java.util.List;

import lombok.Data;

@Data
public class OrderResponse {

    private Long orderId;
    private Double totalAmount;
    private String status;
    private String createdAt;
    private List<OrderItemResponse> items;
}