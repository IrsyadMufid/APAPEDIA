package com.apapedia.user.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.apapedia.user.dto.request.AuthRequestDTO;
import com.apapedia.user.dto.request.CreateUserRequestDTO;
import com.apapedia.user.dto.request.SSOLoginRequestDTO;
import com.apapedia.user.dto.response.JwtResponseDTO;
import com.apapedia.user.model.UserModel;
import com.apapedia.user.repository.UserDb;
import com.apapedia.user.security.JwtGenerator;
import com.apapedia.user.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AuthService authService;

    @Autowired
    JwtGenerator jwtGenerator;

    @Autowired
    UserDb userDb;

    @PostMapping(value = "/sign-up")
    public ResponseEntity<UserModel> addNewUser(@Valid @RequestBody CreateUserRequestDTO userDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body has invalid type or missing field");
        } else {
            var user = authService.register(userDTO);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    @PostMapping(value = "/log-in")
    public ResponseEntity<JwtResponseDTO> authenticate(@Valid @RequestBody AuthRequestDTO authRequestDTO, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body has invalid type or missing field");
        } else {
            try {
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                            authRequestDTO.getUsername(), authRequestDTO.getPassword(), null);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String jwtToken = authService.login(authRequestDTO);
                return new ResponseEntity<>(new JwtResponseDTO(jwtToken), HttpStatus.OK);
            } catch (AuthenticationException e) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
        }
    }

    @PostMapping(value = "/log-in-sso")
    public ResponseEntity<JwtResponseDTO> authenticateSSO(@Valid @RequestBody SSOLoginRequestDTO ssoLoginRequestDTO, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body has invalid type or missing field");
        } else {
            try {
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                            ssoLoginRequestDTO.getUsername(), null, null);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String jwtToken = authService.loginSSO(ssoLoginRequestDTO);
                return new ResponseEntity<>(new JwtResponseDTO(jwtToken), HttpStatus.OK);
            } catch (AuthenticationException e) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
        }
    }

    @PostMapping(value = "/log-out")
    public ResponseEntity<String> logout() {
        SecurityContextHolder.clearContext();
        return new ResponseEntity<>("Logout success", HttpStatus.OK);
    }

}
