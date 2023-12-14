package com.apapedia.order.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.util.Map;
import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import com.apapedia.order.dto.CatalogResponse;
import com.apapedia.order.dto.ChartDataDTO;
import com.apapedia.order.model.Cart;
import com.apapedia.order.model.CartItem;
import com.apapedia.order.model.Order;
import com.apapedia.order.model.OrderItem;
import com.apapedia.order.repository.CartDb;
import com.apapedia.order.repository.CartItemDb;
import com.apapedia.order.repository.OrderDb;
import com.apapedia.order.repository.OrderItemDb;
import com.apapedia.order.setting.Setting;

import jakarta.transaction.Transactional;

@Service
public class OrderService {
    @Autowired
    OrderDb orderDb;

    @Autowired
    CartDb cartDb;

    @Autowired
    CartItemDb cartItemDb;

    @Autowired
    OrderItemDb orderItemDb;


    private final WebClient webClient;
    private final WebClient webClientOrder;
    private final WebClient webClientUser;
    private final String catalogServiceBaseUrl = Setting.CLIENT_CATALOG_SERVICE;
    private final String orderServiceBaseUrl = Setting.CLIENT_ORDER_SERVICE;
    private final String userServiceBaseUrl = Setting.CLIENT_USER_SERVICE;


    public OrderService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(catalogServiceBaseUrl).build();
        this.webClientOrder = webClientBuilder.baseUrl(orderServiceBaseUrl).build();
        this.webClientUser = webClientBuilder.baseUrl(userServiceBaseUrl).build();
        
    }

    // Order #6 (dari cart, buat ngecek aja)
    public List<Order> realOrder(Cart cart, UUID customer, String token) {
        try {
            Integer sellerCount = checkSellerCount(cart).size();
            List<Order> listOrder = new ArrayList<>();
            
            if (sellerCount == 0) {
                throw new RuntimeException("Your cart is empty!");
            }

            if (!(sellerCount > 1)) {
                listOrder.add(createOrderFromCart(cart, customer, token));
                return listOrder;
            }
            
            return createOrderFromCart2(cart, customer, token);
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    // Order #6 (kalo dalam 1 cart itemnya ada dari >1 seller)
    @Transactional
    public List<Order> createOrderFromCart2(Cart cart, UUID customer, String token) {
        List<CartItem> items = cart.getCartItems();  
        List<UUID> sellers = checkSellerCount(cart);
        List<List<CartItem>> separated = new ArrayList<>();
        List<Order> listOrder = new ArrayList<>();
        
        for (UUID id : sellers) {
        String url = "/catalog/item-seller/" + id;
        List<CatalogResponse> listCatalogResponse = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToFlux(CatalogResponse.class)
                .collectList()
                .block();

        List<CartItem> cartItemsForSeller = items.stream()
                .filter(cartItem -> listCatalogResponse.stream()
                        .anyMatch(catalogResponse -> catalogResponse.getId().equals(cartItem.getProductId())))
                .collect(Collectors.toList());
        
            separated.add(cartItemsForSeller);
        }

        for (List<CartItem> list : separated) {
            Order order = new Order();
            order.setCreatedAt(LocalDateTime.now());
            order.setCustomer(customer);
            order.setStatus(0);
            order.setTotalPrice(0);
            orderDb.save(order);
            for (CartItem i : list) {
                OrderItem orderItem = new OrderItem();
                orderItem.setProductId(i.getProductId());
                orderItem.setOrder(order);
                orderItemDb.save(orderItem);
                String url = "/catalog/" + i.getProductId();
                CatalogResponse catalogResponse = webClient.get()
                        .uri(url)
                        .retrieve()
                        .bodyToMono(CatalogResponse.class)
                        .block();
                orderItem.setProductName(catalogResponse.getProductName()); //fetch catalog
                orderItem.setProductPrice(catalogResponse.getPrice()); //fetch catalog
                orderItem.setQuantity(i.getQuantity());
                orderItemDb.save(orderItem);
                order.getOrderItems().add(orderItem);
                order.setSeller(catalogResponse.getSellerId());
                order.setTotalPrice(order.getTotalPrice() + (catalogResponse.getPrice()*i.getQuantity()));


                String updateStockUrl = "/catalog/decrease-stock/" + catalogResponse.getId() + "?quantity=" + orderItem.getQuantity();
                webClient.post()
                        .uri(updateStockUrl)
                        .retrieve()
                        .toBodilessEntity()
                        .block(); 

            }
            orderDb.save(order);
            reducecustomerBalance(order, token);
            listOrder.add(order);
        } 

        if (cart.getCartItems() != null) {
            String clearCartUrl = "/order/clear-cart/" + cart.getId();
            webClientOrder.delete()
                    .uri(clearCartUrl)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        }
     
        return listOrder;
    }

    // Order #6 (kalo dari cart dan seller 1)
    @Transactional
    public Order createOrderFromCart(Cart cart, UUID customer, String token) {
        List<CartItem> items = cart.getCartItems();  
        Order order = new Order();
        order.setCreatedAt(LocalDateTime.now());
        order.setCustomer(customer);
        order.setStatus(0);
        order.setTotalPrice(0);
        orderDb.save(order);
        
        for (CartItem i : items) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(i.getProductId());
            orderItem.setOrder(order);            
            orderItemDb.save(orderItem);
            String url = "/catalog/" + i.getProductId();
            CatalogResponse catalogResponse = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(CatalogResponse.class)
                    .block();

            orderItem.setProductName(catalogResponse.getProductName()); //fetch catalog
            orderItem.setProductPrice(catalogResponse.getPrice()); //fetch catalog
            orderItem.setQuantity(i.getQuantity());
            orderItemDb.save(orderItem);
            order.getOrderItems().add(orderItem);
            order.setSeller(catalogResponse.getSellerId());
            order.setTotalPrice(order.getTotalPrice() + (catalogResponse.getPrice()*i.getQuantity()));


            String updateStockUrl = "/catalog/decrease-stock/" + catalogResponse.getId() + "?quantity=" + orderItem.getQuantity();
            webClient.post()
                    .uri(updateStockUrl)
                    .retrieve()
                    .toBodilessEntity()
                    .block(); 
        }

        orderDb.save(order); 
        reducecustomerBalance(order, token);

        String clearCartUrl = "/order/clear-cart/" + cart.getId();
        webClientOrder.delete()
                .uri(clearCartUrl)
                .retrieve()
                .toBodilessEntity()
                .block(); 
        return order;
    }  

    // Order #6 (kalo klik order langsung)
    @Transactional
    public Order createOrderDirect(UUID productId, Integer quantity, UUID customer, String token) {
        Order order = new Order();
        order.setCreatedAt(LocalDateTime.now());
        order.setCustomer(customer);
        order.setStatus(0);
        orderDb.save(order);

        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(productId);
        orderItemDb.save(orderItem);

        String url = "/catalog/" + productId;
        CatalogResponse catalogResponse = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(CatalogResponse.class)
                .block();

        orderItem.setOrder(order);
        orderItem.setProductName(catalogResponse.getProductName());
        orderItem.setProductPrice(catalogResponse.getPrice());
        orderItem.setQuantity(quantity);
        orderItemDb.save(orderItem);
        order.getOrderItems().add(orderItem);
        order.setTotalPrice(catalogResponse.getPrice()*quantity);
        order.setSeller(catalogResponse.getSellerId());

        String updateStockUrl = "/catalog/decrease-stock/" + productId + "?quantity=" + quantity;
        webClient.post()
                .uri(updateStockUrl)
                .retrieve()
                .toBodilessEntity()
                .block();

        orderDb.save(order);  
        reducecustomerBalance(order, token);
        return order;
    } 

    // Order #7
    public List<Order> getOrderByCustomer(UUID customer) {
        return orderDb.findByCustomer(customer);
    }

    // Order #8
    public List<Order> getOrderBySeller(UUID seller) {
        return orderDb.findBySeller(seller);
    }

    // buat dapetin list seller apa aja yg itemnya ada di cart
    public List<UUID> checkSellerCount(Cart cart) {
        List<UUID> sellers = new ArrayList<>();
        for (CartItem ci : cart.getCartItems()) {
            String url = "/catalog/" + ci.getProductId();
            CatalogResponse catalogResponse = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(CatalogResponse.class)
                    .block();
            sellers.add(catalogResponse.getSellerId());
            
        }
        List<UUID> filtered = sellers.stream() 
                                      .distinct() 
                                      .collect(Collectors.toList()); 
        return filtered;
    }
    
    // Order #9 dan handle Order #10
    public Order changeStatus(UUID id, Integer status, String token){
        Order order = orderDb.findById(id).get();
        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());

        if (status == 5){
            increaseSellerBalance(order, token);
        }

        orderDb.save(order);
        return order;
    }

    private void reducecustomerBalance(Order order, String authToken) {
        UUID userId = order.getCustomer();
        long orderTotal = order.getTotalPrice(); 
        String userUrl = "/api/customer/" + userId + "/subtract-balance/" + orderTotal;

        try {
            webClientUser.put()
            .uri(userUrl)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
            .retrieve()
            .toBodilessEntity()
            .block(); 
    
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing user balance", e);
        }
    }
    
    private void increaseSellerBalance(Order order, String authToken) {
        UUID sellerId = order.getSeller(); 
        long orderTotal = order.getTotalPrice(); 
        String sellerUrl = "/api/seller/" + sellerId + "/add-balance/" + orderTotal;
    
        try {
            webClientUser.get()
                    .uri(sellerUrl)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
    
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing seller balance", e);
        }
    }

    public List<ChartDataDTO> getQuantitySoldPerDay(UUID sellerId) {
        // Fetch orders for the specified seller from the repository
        List<Order> orders = orderDb.findBySeller(sellerId);
     
        // Group orders by date and sum the quantity sold
        Map<LocalDate, Integer> quantitySoldPerDay = orders.stream()
                .filter(order -> order.getCreatedAt() != null) // Filter out orders without a created date
                .collect(Collectors.groupingBy(
                       order -> order.getCreatedAt().toLocalDate(),
                       Collectors.summingInt(order -> order.getOrderItems().stream()
                               .mapToInt(OrderItem::getQuantity)
                               .sum())
                ));
     
        // Create a list of ChartDataDTO objects
        List<ChartDataDTO> chartDataList = quantitySoldPerDay.entrySet().stream()
                .map(entry -> new ChartDataDTO(LocalDateTime.of(entry.getKey(), LocalTime.MIDNIGHT), entry.getValue()))
                .collect(Collectors.toList());
     
        // Sort the list by date
        Collections.sort(chartDataList, new Comparator<ChartDataDTO>() {
            @Override
            public int compare(ChartDataDTO o1, ChartDataDTO o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
     
        return chartDataList;
     }
     
}
