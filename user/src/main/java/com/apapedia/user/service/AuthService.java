package com.apapedia.user.service;

import com.apapedia.user.dto.request.AuthRequestDTO;
import com.apapedia.user.dto.request.CreateUserRequestDTO;
import com.apapedia.user.dto.request.SSOLoginRequestDTO;

public interface AuthService {
    String register(CreateUserRequestDTO userDTO);
    String login(AuthRequestDTO authRequestDTO);
    String loginSSO(SSOLoginRequestDTO ssoLoginRequestDTO);
    String loginErrorSSO(SSOLoginRequestDTO ssoLoginRequestDTO);
}
