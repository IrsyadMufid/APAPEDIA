package com.apapedia.frontend.service;

import com.apapedia.frontend.dto.user.request.UpdateUserRequestDTO;
import com.apapedia.frontend.dto.user.response.ReadUserResponseDTO;

public interface UserService {
    ReadUserResponseDTO getUserById(String id, String token);

    ReadUserResponseDTO editUserSeller(String id, String token, UpdateUserRequestDTO user);

    String deleteUser(String id, String token);

    ReadUserResponseDTO subtractBalance(String id, String token, int balance);
}
