package com.apapedia.frontend.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.apapedia.frontend.dto.order.ChartDataDTO;
import com.apapedia.frontend.dto.order.OrderDTO;
import com.apapedia.frontend.service.UserService;
import com.apapedia.frontend.setting.Setting;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/order")
public class OrderController {
    private final WebClient webClient;

    @Autowired
    private UserService userService;    

    public OrderController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(Setting.CLIENT_ORDER_SERVICE + "/order").build();
    }

    @GetMapping("/chart-sales/{id}")
    public String getSalesChart(@PathVariable String id, @RequestParam(name = "productId") UUID productId, Model model) {
        List<ChartDataDTO> listResponse = 
            webClient.get()
                .uri("/selling-data/{sellerId}?productId=" + productId)
                .retrieve()
                .bodyToFlux(ChartDataDTO.class)
                .collectList()
                .block(); 
        
        model.addAttribute("list", listResponse);

        return "/order/chart-sales";
    }

    @GetMapping("/view-all/{id}")
    public String showOrderHistory(@PathVariable String id, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        var token = (String) session.getAttribute("accessToken");

        var user = userService.getUserById(id, token);
        List<OrderDTO> orders = 
            webClient.get()
                .uri("/history-seller/" + id)
                .retrieve()
                .bodyToFlux(OrderDTO.class)
                .collectList()
                .block();

        model.addAttribute("orders", orders);       
        model.addAttribute("activeUserId", id);
        model.addAttribute("user", user);        
        return "/order/order-history-seller";
    }

    @PostMapping("/{orderId}/update-status")
    public String updateOrderStatus(
            @PathVariable String orderId,
            @RequestParam Integer status,
            Model model,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {

        HttpSession session = request.getSession();
        var token = (String) session.getAttribute("accessToken");

        // Fetch order details to check if the update is allowed
        OrderDTO order = webClient.get()
                .uri("/{orderId}", orderId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(OrderDTO.class)
                .block();

        // Perform the update using WebClient
        webClient.put()
                .uri("/{orderId}/ubah-status?status={status}", orderId, order.getStatus()+1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(Void.class)  // Assuming the response is void (bodiless)
                .block();
        
        redirectAttributes.addFlashAttribute("successMessage", "Status order dengan ID " + orderId + " berhasil di-update");
                
        // Redirect to the order history page
        return "redirect:/order/view-all/" + order.getSeller();
    

    }

    // private boolean canSellerUpdateStatus(Integer currentStatus, Integer newStatus) {
    //     // Add logic to check if the seller can update the order status
    //     // For example, allow updates from 0 to 2, and disable updates for 3, 4, 5
    //     return currentStatus >= 0 && currentStatus <= 2 && newStatus >= 0 && newStatus <= 2;
    // }
    
    
    
}
