package apapedia.order.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import apapedia.order.model.Cart;
import apapedia.order.model.CartItem;
import apapedia.order.repository.CartDb;
import apapedia.order.repository.CartItemDb;
import reactor.core.publisher.Mono;
import jakarta.transaction.Transactional;



@Service
public class CartService {
    
    @Autowired
    CartDb cartDb;

    @Autowired
    CartItemDb cartItemDb;

    // private final String mockUrl = System.getenv("MOCK_URL");

    // private final WebClient webClient;

    public CartService() {
        // this.webClient = webClientBuilder.baseUrl(mockUrl).build();
    }

    public Cart createCart(UUID userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setTotalPrice(0);
        cartDb.save(cart);
        return cart; 
    }

    

    @Transactional
    public CartItem addItemToCart(UUID cartId, UUID productId, Integer quantity) {
        // Create a new CartItem
        // Cart cart = getById(cartId);


        Cart cart = cartDb.findById(cartId).get();
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProductId(productId);
        cartItem.setQuantity(quantity);

        cartItemDb.save(cartItem);

        cart.getCartItems().add(cartItem);
        cart.setTotalPrice(quantity);
        cartDb.save(cart);

        return cartItem;

    }

    public List<CartItem> getCartItemsByUser(UUID userId) {
        Optional<Cart> cartOptional = cartDb.findByUserId(userId);
        
        return cartOptional.map(Cart::getCartItems).orElse(Collections.emptyList());
    }

    // public Mono<String> getCatalogId() {
    //     return this.webClient.get().uri("/api/catalog-id/64ff7a9a-821b-4ce8-ab7a-61c13f45484a").retrieve().bodyToMono(String.class);
    // }

    // public Mono<String> getUserId() {
    //     return this.webClient.get().uri("/api/user-id/c48b005e-d2c4-450e-a0ed-6cabf557a3e6").retrieve().bodyToMono(String.class);
    // }

}
