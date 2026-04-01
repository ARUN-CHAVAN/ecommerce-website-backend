package com.nt.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.nt.dto.OrderItemResponse;
import com.nt.dto.OrderResponse;
import com.nt.entity.Cart;
import com.nt.entity.Order;
import com.nt.entity.OrderItem;
import com.nt.entity.Product;
import com.nt.repository.CartRepository;
import com.nt.repository.OrderItemRepository;
import com.nt.repository.OrderRepository;
import com.nt.repository.ProductRepository;

import jakarta.transaction.Transactional;
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            CartRepository cartRepository,ProductRepository productRepository) {
this.orderRepository = orderRepository;
this.orderItemRepository = orderItemRepository;
this.cartRepository = cartRepository;
this.productRepository=productRepository;
}

   
    public List<OrderResponse> getOrdersByUser(Long userId) {

        List<Order> orders = orderRepository.findByUserId(userId);
        List<OrderResponse> responseList = new ArrayList<>();

        for (Order order : orders) {

            List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
            List<OrderItemResponse> itemResponses = new ArrayList<>();

            for (OrderItem item : items) {

                Product product = productRepository
                        .findById(item.getProductId())
                        .orElse(null);

                OrderItemResponse itemRes = new OrderItemResponse();
                itemRes.setProductId(item.getProductId());
                itemRes.setProductName(product != null ? product.getName() : "Unknown");
                itemRes.setQuantity(item.getQuantity());
                if(product !=null) {
                	itemRes.setPrice(product.getPrice());
                }

                itemResponses.add(itemRes);
            }

            OrderResponse orderRes = new OrderResponse();
            orderRes.setOrderId(order.getId());
            orderRes.setTotalAmount(order.getTotalAmount());
            orderRes.setStatus(order.getStatus());
            if(order.getCreatedAt() != null) {
            orderRes.setCreatedAt(order.getCreatedAt().toString());
            }
            orderRes.setItems(itemResponses);

            responseList.add(orderRes);
        }

        return responseList;
    }
    
    @Transactional
    public void placeOrder(Long userId) {

        List<Cart> cartItems = cartRepository.findByUserId(userId);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setStatus("PLACED");
        order.setCreatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        double total=0;
        for (Cart cart : cartItems) {
        	Product product=productRepository.findById(cart.getProductId()).orElse(null);
        	if(product!=null) {
        		total=total + (product.getPrice()*cart.getQuantity());
        	}
            OrderItem item = new OrderItem();
            item.setOrderId(savedOrder.getId());
            item.setProductId(cart.getProductId());
            item.setQuantity(cart.getQuantity());

            orderItemRepository.save(item);
        }
       savedOrder.setTotalAmount(total);
       orderRepository.save(savedOrder);
        
        cartRepository.deleteByUserId(userId);
    }
   
}
