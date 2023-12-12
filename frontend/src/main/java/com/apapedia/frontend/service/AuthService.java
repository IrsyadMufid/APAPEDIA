package com.apapedia.frontend.service;

import com.apapedia.frontend.dto.user.response.ReadUserResponseDTO;

public interface AuthService {
    String loginUserSSO(String username, String name);
    String getTokenSSO(String username, String name);
    String getToken(String username, String password);
    ReadUserResponseDTO getActiveUser(String token);
}
