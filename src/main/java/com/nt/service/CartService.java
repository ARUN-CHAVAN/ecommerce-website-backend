package com.nt.service;


import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.nt.entity.Cart;
import com.nt.repository.CartRepository;

@Service
public class CartService {

    private final CartRepository cartRepository;
    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Cart addToCart(Long userId, Long productId) {

        Optional<Cart> existingCart = cartRepository
                .findByUserIdAndProductId(userId, productId);

        if (existingCart.isPresent()) {
            Cart cart = existingCart.get();
            cart.setQuantity(cart.getQuantity() + 1);
            return cartRepository.save(cart);

        } else {
            Cart newCart = new Cart();
            newCart.setUserId(userId);
            newCart.setProductId(productId);
            newCart.setQuantity(1);
            return cartRepository.save(newCart);
        }
    }

    public List<Cart> getCartByUser(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    public void removeFromCart(Long cartId) {
        cartRepository.deleteById(cartId);
    }
    public Cart decreaseQuantity(Long userId, Long productId) {

        Optional<Cart> existingCart =
            cartRepository.findByUserIdAndProductId(userId, productId);

        if (existingCart.isPresent()) {
            Cart cart = existingCart.get();

            if (cart.getQuantity() > 1) {
                cart.setQuantity(cart.getQuantity() - 1);
                return cartRepository.save(cart);
            } else {
                cartRepository.delete(cart);
                return null;
            }
        }

        return null;
    }
    public void clearCartByUser(Long userId) {
        List<Cart> items = cartRepository.findByUserId(userId);

        if (items.isEmpty()) {
            System.out.println("Cart already empty");
            return;
        }

        cartRepository.deleteAll(items);
    }
}