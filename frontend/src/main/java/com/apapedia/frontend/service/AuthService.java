package com.apapedia.frontend.service;

import com.apapedia.frontend.dto.user.response.ReadUserResponseDTO;

public interface AuthService {
    public String getTokenSSO(String username, String name);
    public String getToken(String username, String password);
    ReadUserResponseDTO getActiveUser(String token);
}
