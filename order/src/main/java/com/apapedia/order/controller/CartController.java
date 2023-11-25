package com.apapedia.order.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import com.apapedia.order.model.Cart;
import com.apapedia.order.model.CartItem;
import com.apapedia.order.repository.CartDb;
import com.apapedia.order.service.CartService;

import org.hibernate.engine.internal.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    CartService cartService;

    @Autowired
    CartDb cartDb;

    @PostMapping(value = "/create-cart/{userId}")
    public Cart createCart(@PathVariable("userId") UUID userId) {
        return cartService.createCart(userId);
    }

    @GetMapping(value = "/carttes/{userId}")
    public Cart getCart(@PathVariable("userId") UUID userId) {
        List<Cart> cart = cartDb.findAll();
        for (Cart c : cart) {
            if (c.getUserId().equals(userId)) {
                return c;
            }
        }
        return null;
    }

    @PostMapping(value = "/add-item")
    public CartItem addItemToCart(
    @RequestParam(name = "cartId") UUID cartId,
    @RequestParam(name = "productId") UUID productId,
    @RequestParam(name = "quantity") Integer quantity) {
        return cartService.addItemToCart(cartId, productId, quantity);
    }

    @GetMapping(value = "/cart/{userId}")
    public List<CartItem> retrieveCartItems(@PathVariable("userId") UUID userId) {
        debug(userId);
        List<CartItem> items = cartService.getCartItemsByUser(userId);
        return items;
    }

    public String debug(UUID id) {
        List<CartItem> items = cartService.getCartItemsByUser(id);
        for (CartItem i : items) {
            System.out.println(i.getId());

        }
        return null;

    }


}
