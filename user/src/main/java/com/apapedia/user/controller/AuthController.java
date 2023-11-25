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
            var user = authService.Register(userDTO);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    @PostMapping(value = "/log-in")
    public ResponseEntity<JwtResponseDTO> Authenticate(@Valid @RequestBody AuthRequestDTO authDTO, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body has invalid type or missing field");
        } else {
            String username;
            UserModel user;
            if (authDTO.getUsername().contains("@")) {
                user = userDb.findByEmail(authDTO.getUsername());
                username = user.getUsername();
            } else {
                username = authDTO.getUsername();
                user = userDb.findByUsername(username);
            }

            try {
                Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, authDTO.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String token = jwtGenerator.generateToken(authentication, user.getId(), user.getRole().toString());
                Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
                grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole().toString()));
                System.out.println(grantedAuthorities);
                return new ResponseEntity<>(new JwtResponseDTO(token), HttpStatus.OK);
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
