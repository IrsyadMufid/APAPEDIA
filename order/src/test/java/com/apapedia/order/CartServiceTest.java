// package com.apapedia.order;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import org.mockito.junit.MockitoJUnitRunner;
// import org.mockito.junit.jupiter.MockitoExtension;
// import reactor.core.publisher.Mono;

// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.web.reactive.function.client.WebClient;

// import com.apapedia.order.dto.CatalogResponse;
// import com.apapedia.order.model.Cart;
// import com.apapedia.order.model.CartItem;
// import com.apapedia.order.service.CartService;
// import com.apapedia.order.setting.Setting;
// import com.apapedia.order.repository.CartItemDb;
// import com.apapedia.order.repository.CartDb;

// import java.util.Optional;
// import java.util.UUID;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.anyString;
// import static org.mockito.ArgumentMatchers.eq;
// import static org.mockito.Mockito.*;
// import org.mockito.junit.jupiter.MockitoExtension;

// @SpringBootTest
// public class CartServiceTest {

//     @Mock
//     private CartDb cartDb;
   
//     @Mock
//     private CartItemDb cartItemDb;
   
//     @Mock
//     private WebClient.Builder webClientBuilder;
   
//     @Mock
//     private WebClient webClient;
   
//     @InjectMocks
//     private CartService cartService;
   
//     @BeforeEach
//     public void setUp() {
//         MockitoAnnotations.openMocks(this);
//          when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
//          when(webClientBuilder.build()).thenReturn(webClient);
//         //  cartService = new CartService(webClientBuilder);
//     }

//     @Test
//     public void testCreateCart() {
//         UUID userId = UUID.randomUUID();
//         Cart cart = new Cart();
//         cart.setUserId(userId);
//         cart.setTotalPrice(0);
    
//         // Use any() matcher to match any Cart object
//         when(cartDb.save(any(Cart.class))).thenReturn(cart);
    
//         Cart result = cartService.createCart(userId);
    
//         // Use any() matcher to match any Cart object
//         verify(cartDb, times(1)).save(result);
//         assertEquals(userId, result.getUserId());
//         assertEquals(0, result.getTotalPrice());
//     }

//     @Test
//     public void testAddItemToCart() {
//        UUID cartId = UUID.randomUUID();
//        UUID productId = UUID.randomUUID();
//        Integer quantity = 1;
    
//        Cart cart = new Cart();
//        cart.setId(cartId);
//        CartItem cartItem = new CartItem();
//        cartItem.setProductId(productId);
//        cartItem.setQuantity(quantity);
    
//        when(cartDb.findById(cartId)).thenReturn(Optional.of(cart));
//        when(cartItemDb.save(any(CartItem.class))).thenReturn(cartItem);
    
//        CartItem result = cartService.addItemToCart(cartId, productId, quantity);
    
//        verify(cartDb, times(1)).findById(cartId);
//        verify(cartItemDb, times(1)).save(any(CartItem.class));
//        assertEquals(productId, result.getProductId());
//        assertEquals(quantity, result.getQuantity());
//     }
// }
