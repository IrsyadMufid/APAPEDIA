package com.apapedia.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

import com.apapedia.order.model.Cart;
import com.apapedia.order.model.CartItem;
import com.apapedia.order.repository.CartDb;
import com.apapedia.order.repository.CartItemDb;
import com.apapedia.order.repository.OrderDb;
import com.apapedia.order.service.CartService;
import com.apapedia.order.service.OrderService;
import com.apapedia.order.setting.Setting;

@SpringBootTest
class OrderApplicationTests {

	@Mock
	OrderDb orderDb;

	@Mock
	CartDb cartDb;

	@Mock
	CartItemDb cartItemDb;

	@Mock
	WebClient.Builder webClientBuilder;

	@Mock
	WebClient webClient;
	
	@Mock
	Setting setting;

	@InjectMocks
	OrderService orderService;

	@InjectMocks
	CartService cartService;

	private UUID cartId;
	private UUID userId;
	private UUID cartItemId1;
	private UUID cartItemId2;

	@BeforeEach
	public void setUp() {
		when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
		when(webClientBuilder.build()).thenReturn(webClient);
		orderService = new OrderService(webClientBuilder);

		cartId = UUID.randomUUID();
		userId = UUID.randomUUID();
		Integer totalPrice = 0;

		List<Cart> listCart = new ArrayList<>();
		List<CartItem> listCartItems = new ArrayList<>();
		
		Cart cart = new Cart(cartId, userId, totalPrice, listCartItems);

		UUID productId1 = UUID.randomUUID();		
		UUID productId2 = UUID.randomUUID();

		cartItemId1 = UUID.randomUUID();		
		cartItemId2 = UUID.randomUUID();

		CartItem cartItem1 = new CartItem(cartItemId1, productId1, cart, 2);		
		CartItem cartItem2 = new CartItem(cartItemId2, productId2, cart, 2);
		listCartItems.add(cartItem1);		
		listCartItems.add(cartItem2);

		Cart cartNew = new Cart(cartId, userId, totalPrice, listCartItems);
		listCart.add(cartNew);

		Mockito.when(cartDb.findAll()).thenReturn(listCart);		
		Mockito.when(cartItemDb.findAll()).thenReturn(listCartItems);

		// Mock the delete method to remove from the list
		Mockito.doAnswer(invocation -> {
			CartItem deletedItem = invocation.getArgument(0);
			listCartItems.remove(deletedItem);
			return null;
		}).when(cartItemDb).delete(Mockito.any(CartItem.class));

		when(cartDb.findAll()).thenReturn(listCart);
		when(cartItemDb.findAll()).thenReturn(listCartItems);


		// Mock the behavior of the setting field
        Mockito.when(Setting.CLIENT_CATALOG_SERVICE).thenReturn("http://example.com/catalog");  

    }

    @Test
    public void testCreateCart() {
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setTotalPrice(0);
    
        // Use any() matcher to match any Cart object
        when(cartDb.save(any(Cart.class))).thenReturn(cart);
    
        Cart result = cartService.createCart(userId);
    
        // Use any() matcher to match any Cart object
        verify(cartDb, times(1)).save(result);
        assertEquals(userId, result.getUserId());
        assertEquals(0, result.getTotalPrice());
    }

    @Test
    void testDeleteCartItem() {
        // Perform the test for CartService using the setup attributes
        Cart cart = cartDb.findById(cartId).get();
		UUID cartItemId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        CartItem cartItem = new CartItem(cartItemId, productId, cart, 2);

        List<CartItem> listCartItems = new ArrayList<>();
        listCartItems.add(cartItem);

        Mockito.when(cartItemDb.findAll()).thenReturn(listCartItems);

        // Assuming that deleteCartItem method updates the cartItemDb
        cartService.deleteCartItem(cart, cartItem);

        // Verify the interactions or state changes as needed
        Mockito.verify(cartItemDb, Mockito.times(1)).delete(cartItem);
    }
}