package com.apapedia.order.service;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.apapedia.order.dto.CatalogResponse;
import com.apapedia.order.model.Cart;
import com.apapedia.order.model.CartItem;
import com.apapedia.order.repository.CartDb;
import com.apapedia.order.repository.CartItemDb;
import jakarta.transaction.Transactional;



@Service
public class CartService {
    
    @Autowired
    CartDb cartDb;

    @Autowired
    CartItemDb cartItemDb;

    private final WebClient webClient;
    private final String catalogServiceBaseUrl = "http://localhost:8083/";

    public CartService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(catalogServiceBaseUrl).build();
    }

    // Order #1
    public Cart createCart(UUID userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setTotalPrice(0);
        System.out.println("sblm save created");
        cartDb.save(cart);
        System.out.println("created");
        return cart; 
    }

    // Order #2
    @Transactional
    public CartItem addItemToCart(UUID cartId, UUID productId, Integer quantity) {
        Cart cart = cartDb.findById(cartId).get();

        boolean itemExists = false;

        if (cart.getCartItems().size() > 0) {
            itemExists = cart.getCartItems().stream()
            .anyMatch(cartItem -> cartItem.getProductId().equals(productId));
        }

        String url = "/catalog/" + productId;
        CatalogResponse catalogResponse = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(CatalogResponse.class)
                .block();

        if (itemExists) {
            for (CartItem ci : cart.getCartItems()) {
                if (ci.getProductId().equals(productId)) {
                    ci.setQuantity(ci.getQuantity()+quantity);
                    cart.setTotalPrice(cart.getTotalPrice() + ci.getQuantity()*catalogResponse.getPrice());
                    cartItemDb.save(ci);
                    return ci;
                }
            }
        }

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProductId(productId);
        cartItem.setQuantity(quantity);

        cartItemDb.save(cartItem);

        cart.getCartItems().add(cartItem);
        cart.setTotalPrice(cart.getTotalPrice() + cartItem.getQuantity()*catalogResponse.getPrice()); //perlu fetch catalog
        cartDb.save(cart);

        return cartItem;
    }

    // Order #4
    public List<CartItem> getCartItemsByUser(UUID userId) {
        Optional<Cart> cartOptional = cartDb.findByUserId(userId);
        
        return cartOptional.map(Cart::getCartItems).orElse(Collections.emptyList());
    }

    // Order #3
    public CartItem editQuantity(CartItem ci ,Integer qnt) {
        ci.setQuantity(qnt);
        cartItemDb.save(ci);
        return ci;
    } 

    // Order #5
    public void deleteCartItem(Cart cart, CartItem cartItem) {
        List<CartItem> cartItems = cart.getCartItems();
        for (CartItem ci : cartItems) {
            if (ci.equals(cartItem)) {
                cartItems.remove(ci);
                cartItemDb.delete(ci);
                cartDb.save(cart);
            }
        }
    }

    @Transactional
    public void clearCart(Cart cart) {
        Iterator<CartItem> iterator = cart.getCartItems().iterator();
        while (iterator.hasNext()) {
            CartItem ci = iterator.next();
            iterator.remove(); 
    
            cartItemDb.delete(ci);
        }
        cart.setTotalPrice(0);
        cartDb.save(cart);
    }
}
