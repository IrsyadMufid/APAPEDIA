package com.apapedia.user.service;

import com.apapedia.user.dto.request.CreateUserRequestDTO;
import com.apapedia.user.model.UserModel;

public interface AuthService {
    UserModel Register(CreateUserRequestDTO userDTO);
}
