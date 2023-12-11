package com.apapedia.frontend.service;

import com.apapedia.frontend.dto.user.request.UpdateUserRequestDTO;
import com.apapedia.frontend.dto.user.response.ReadUserResponseDTO;

public interface UserService {
    public ReadUserResponseDTO getUserById(String id, String token);

    public ReadUserResponseDTO editUserSeller(String id, String token, UpdateUserRequestDTO user);
}
