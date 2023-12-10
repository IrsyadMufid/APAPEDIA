package com.apapedia.user.service;

import com.apapedia.user.dto.request.AuthRequestDTO;
import com.apapedia.user.dto.request.CreateUserRequestDTO;
import com.apapedia.user.dto.request.SSOLoginRequestDTO;
import com.apapedia.user.model.UserModel;

public interface AuthService {
    UserModel register(CreateUserRequestDTO userDTO);
    String login(AuthRequestDTO authRequestDTO);
    String loginSSO(SSOLoginRequestDTO ssoLoginRequestDTO);
}
