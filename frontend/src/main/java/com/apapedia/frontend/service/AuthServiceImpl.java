package com.apapedia.frontend.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.apapedia.frontend.dto.auth.request.AuthRequestDTO;
import com.apapedia.frontend.dto.auth.response.JwtResponseDTO;
import com.apapedia.frontend.dto.user.request.SSOLoginRequestDTO;
import com.apapedia.frontend.dto.user.response.ReadUserResponseDTO;
import com.apapedia.frontend.setting.Setting;

@Service
public class AuthServiceImpl implements AuthService {
    private final WebClient webClient;

    public AuthServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(Setting.CLIENT_USER_SERVICE)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public String loginUserSSO(String username, String name) {
        var body = new SSOLoginRequestDTO(username, name);

        var response = this.webClient
                .post()
                .uri("/api/auth/log-in-sso-user")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(JwtResponseDTO.class)
                .block();
        
        var token = response.getAccessToken();

        return token;
    }

    @Override
    public String getTokenSSO(String username, String name) {
        var body = new SSOLoginRequestDTO(username, name);

        var response = this.webClient
                .post()
                .uri("/api/auth/log-in-sso")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(JwtResponseDTO.class)
                .block();
        
        var token = response.getAccessToken();

        return token;
    }

    @Override
    public String getToken(String username, String password) {
        var body = new AuthRequestDTO(username, password);

        var response = this.webClient
                .post()
                .uri("/api/auth/log-in")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(JwtResponseDTO.class)
                .block();
        
        var token = response.getAccessToken();

        return token;
    }

    @Override
    public ReadUserResponseDTO getActiveUser(String token) {
        var body = new JwtResponseDTO(token);
        
        var response = this.webClient
                .post()
                .uri("/api/auth/get-user")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(ReadUserResponseDTO.class)
                .block();
        
        return response;
    }

}
