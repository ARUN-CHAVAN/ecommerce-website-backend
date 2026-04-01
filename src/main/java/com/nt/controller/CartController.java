package com.nt.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nt.entity.Cart;
import com.nt.service.CartService;

@RestController
@RequestMapping("/cart")
@CrossOrigin
public class CartController {

    private final CartService cartService;
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public Cart addToCart(@RequestBody Cart cart) {
        return cartService.addToCart(cart.getUserId(),cart.getProductId());
    }

    @GetMapping("/{userId}")
    public List<Cart> getCart(@PathVariable Long userId) {
        return cartService.getCartByUser(userId);
    }

    @DeleteMapping("/{cartId}")
    public void removeCart(@PathVariable Long cartId) {
        cartService.removeFromCart(cartId);
    }
    @PutMapping("/decrease")
    public Cart decreaseQuantity(@RequestParam Long userId,
                                 @RequestParam Long productId) {
        return cartService.decreaseQuantity(userId, productId);
    }
    @DeleteMapping("/user/{userId}")
    public String clearCart(@PathVariable Long userId) {
        cartService.clearCartByUser(userId);
        return "Cart cleared";
    }
    
}