package com.apapedia.order.controller;

import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.apapedia.order.model.Cart;
import com.apapedia.order.model.CartItem;
import com.apapedia.order.repository.CartDb;
import com.apapedia.order.repository.CartItemDb;
import com.apapedia.order.service.CartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    CartService cartService;

    @Autowired
    CartDb cartDb;

    @Autowired
    CartItemDb cartItemDb;

    @PostMapping(value = "/create-cart/{userId}")
    public Cart createCart(@PathVariable("userId") UUID userId) {
        return cartService.createCart(userId);
    }

    @PostMapping(value = "/add-item/{cartId}")
    public CartItem addItemToCart(
    @PathVariable("cartId") UUID cartId,
    @RequestParam(name = "productId") UUID productId,
    @RequestParam(name = "quantity") Integer quantity) {
        return cartService.addItemToCart(cartId, productId, quantity);
    }

    @GetMapping(value = "/{userId}")
    public List<CartItem> retrieveCartItems(@PathVariable("userId") UUID userId) {
        List<CartItem> items = cartService.getCartItemsByUser(userId);
        return items;
    }

    @PostMapping(value = "/update/{cartItem}")
    public CartItem updateQuantity(
    @PathVariable("cartItem") UUID cartItem,
    @RequestParam(name = "quantity") Integer quantity) {
        CartItem ci = cartItemDb.findById(cartItem).get();
        return cartService.editQuantity(ci, quantity);
    }

    @GetMapping(value = "/delete-item/{cart}")
    public ResponseEntity<String> deleteItem(
    @PathVariable("cart") UUID cart,
    @RequestParam(name = "cartItem") UUID cartItem) {
        Cart c = cartDb.findById(cart).get();
        CartItem ci = cartItemDb.findById(cartItem).get();
        cartService.deleteCartItem(c, ci);
        return ResponseEntity.ok("success delete item in cart");
    }
}
