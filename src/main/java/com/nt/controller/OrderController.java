package com.nt.controller;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.dto.OrderResponse;
import com.nt.security.JwtUtil;
import com.nt.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

     

    @GetMapping
    public List<OrderResponse> getOrders(HttpServletRequest request) {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            throw new RuntimeException("Missing token");
        }

        String token = header.substring(7);

        JwtUtil jwtUtil = new JwtUtil();
        Long userId = jwtUtil.extractUserId(token);

        return orderService.getOrdersByUser(userId);
    }
    
    @PostMapping
    public ResponseEntity<String> placeOrder(HttpServletRequest request) {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            throw new RuntimeException("Missing token");
        }

        String token = header.substring(7);

        JwtUtil jwtUtil = new JwtUtil();
        Long userId = jwtUtil.extractUserId(token);

        orderService.placeOrder(userId);

        return ResponseEntity.ok("Order placed successfully");
    }
   
}
