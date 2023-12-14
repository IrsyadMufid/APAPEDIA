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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.apapedia.user.dto.request.AuthRequestDTO;
import com.apapedia.user.dto.request.CreateUserRequestDTO;
import com.apapedia.user.dto.request.SSOLoginRequestDTO;
import com.apapedia.user.dto.request.TokenRequestDTO;
import com.apapedia.user.dto.response.JwtResponseDTO;
import com.apapedia.user.dto.response.UserResponseDTO;
import com.apapedia.user.model.Customer;
import com.apapedia.user.model.Seller;
import com.apapedia.user.model.UserModel;
import com.apapedia.user.repository.UserDb;
import com.apapedia.user.security.JwtGenerator;
import com.apapedia.user.service.AuthService;
import io.micrometer.common.lang.Nullable;
import jakarta.validation.Valid;

@CrossOrigin
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

    public static final String RESPONSE_INVALID = "Request body has invalid type or missing field";
    @PostMapping(value = "/sign-up")
    public ResponseEntity<String> addNewUser(@Valid @RequestBody CreateUserRequestDTO userDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, RESPONSE_INVALID);} 
        else {
            var user = authService.register(userDTO);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    @PostMapping(value = "/log-in-sso-user")
    public ResponseEntity<JwtResponseDTO> userLoginSSO(@Valid @RequestBody SSOLoginRequestDTO ssoLoginRequestDTO, @Nullable BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, RESPONSE_INVALID);
        } else {
            try {
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                            ssoLoginRequestDTO.getUsername(), null, null);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String jwtToken = authService.loginErrorSSO(ssoLoginRequestDTO);
                return new ResponseEntity<>(new JwtResponseDTO(jwtToken), HttpStatus.OK);
            } catch (AuthenticationException e) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
        }
    }

    @Nullable
    @PostMapping(value = "/log-in")
    public ResponseEntity<JwtResponseDTO> authenticate(@Valid @RequestBody AuthRequestDTO authRequestDTO, @Nullable BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, RESPONSE_INVALID);
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
    public ResponseEntity<JwtResponseDTO> authenticateSSO(@Valid @RequestBody SSOLoginRequestDTO ssoLoginRequestDTO, @Nullable BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, RESPONSE_INVALID);
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

    @PostMapping(value = "/check-token")
    public ResponseEntity<String> checkToken(@RequestBody String token) {
        if (jwtGenerator.validateToken(token)) {
            return new ResponseEntity<>("Token is valid", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Token is invalid", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(value = "/get-user")
    public ResponseEntity<UserResponseDTO> getUser(@RequestBody TokenRequestDTO tokenRequestDTO) {
        String token = tokenRequestDTO.getAccessToken();
        String username = jwtGenerator.getUsernameFromJWT(token);
        UserModel user = userDb.findByUsername(username);
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole().toString()));
        var userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(user.getId());
        userResponseDTO.setName(user.getName());
        userResponseDTO.setUsername(user.getUsername());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setAddress(user.getAddress());
        userResponseDTO.setBalance(user.getBalance());
        userResponseDTO.setRole(user.getRole().toString());
        
        if (user.getRole().toString().equals("Customer")) {
            if (user instanceof Customer) {
                var customer = (Customer) user;
                userResponseDTO.setCartId(customer.getCartId());
            } else {
                throw new IllegalArgumentException("User is not a customer");
            }
        } else if (user.getRole().toString().equals("Seller")) {
            if (user instanceof Seller) {
                var seller = (Seller) user;
                userResponseDTO.setCategory(seller.getCategory());
            } else {
                throw new IllegalArgumentException("User is not a seller");
            }
        }
        return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
    }

}
