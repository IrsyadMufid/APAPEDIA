package com.apapedia.order.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.apapedia.order.dto.ChartDataDTO;
import com.apapedia.order.model.Cart;
import com.apapedia.order.model.Order;
import com.apapedia.order.repository.CartDb;
import com.apapedia.order.repository.OrderDb;
import com.apapedia.order.service.CartService;
import com.apapedia.order.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderService orderService;

    @Autowired
    OrderDb orderDb;

    @Autowired
    CartService cartService;

    @Autowired
    CartDb cartDb;

    @PostMapping(value = "/cart-create/{cartId}")
    public List<Order> orderFromCart(
    @PathVariable("cartId") UUID cartId, HttpServletRequest request) {
        Cart cart = cartDb.findById(cartId).get();
        UUID customerId = cart.getUserId();
        String headerAuth = request.getHeader("Authorization");
        String token = headerAuth.substring(7);
        return orderService.realOrder(cart, customerId, token);
    }

    @PostMapping(value = "/direct-create/{customerId}")
    public Order orderDirect(
    @PathVariable("customerId") UUID customerId,
    @RequestParam(name = "productId") UUID productId,
    @RequestParam(name = "quantity") Integer quantity, HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        String token = headerAuth.substring(7);

        return orderService.createOrderDirect(productId, quantity, customerId, token);
    }

    @DeleteMapping("/clear-cart/{id}")
    public ResponseEntity<String> clearCart(@PathVariable("id") UUID id) {
        try {
            cartService.clearCart(cartDb.findById(id).orElse(null));
            return ResponseEntity.ok("Cart cleared successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to clear cart");
        }
    }

    // Fitur Order 9 : PUT order (ubah status order) cust & seller
    @PutMapping(value="/{id}/ubah-status")
    public Order changeStatus(@PathVariable("id") UUID id, @RequestParam("status") Integer status, HttpServletRequest request){
        String headerAuth = request.getHeader("Authorization");
        String token = headerAuth.substring(7);        
        return orderService.changeStatus(id, status, token);
    }

    @GetMapping(value = "/selling-data/{sellerId}")
    public List<ChartDataDTO> retrieveCartItems(
    @PathVariable("sellerId") UUID sellerId, 
    @RequestParam(name = "productId") UUID productId) {
        List<ChartDataDTO> data = orderService.getQuantitySoldPerDay(sellerId, productId);
        return data;
    }

    @GetMapping("/history-seller/{sellerId}")
    public List<Order> getHistoryOrderSeller(@PathVariable("sellerId") UUID sellerId) {
        return orderService.getOrderBySeller(sellerId);
    }

    @GetMapping("/{orderId}")
    public Order getOrder(@PathVariable("orderId") UUID orderId) {
        return orderDb.findById(orderId).get();
    }    
    
     
}
