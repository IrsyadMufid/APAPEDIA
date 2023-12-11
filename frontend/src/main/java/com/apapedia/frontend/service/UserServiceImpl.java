package com.apapedia.frontend.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.apapedia.frontend.dto.user.request.UpdateUserRequestDTO;
import com.apapedia.frontend.dto.user.response.ReadUserResponseDTO;

@Service
public class UserServiceImpl implements UserService {
    private final WebClient webClient;

    public UserServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8081")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public ReadUserResponseDTO getUserById(String id, String token) {
        var response = this.webClient
                .get()
                .uri("/api/user/" + id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(ReadUserResponseDTO.class)
                .block();
        
        return response;
    }

    @Override
    public ReadUserResponseDTO editUserSeller(String id, String token, UpdateUserRequestDTO user) {
        var response = this.webClient
                .put()
                .uri("/api/user/edit-user-seller/" + id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .bodyValue(user)
                .retrieve()
                .bodyToMono(ReadUserResponseDTO.class)
                .block();
        
        return response;
    }
}
