package com.apapedia.user.controller;

import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.apapedia.user.model.RoleEnum;
import com.apapedia.user.model.UserModel;
import com.apapedia.user.repository.UserDb;
import com.apapedia.user.service.AuthService;
import com.apapedia.user.service.UserService;

@RestController
@RequestMapping("/api/seller")
public class SellerController {
    
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    AuthService authService;

    @Autowired
    UserDb userDb;

    @GetMapping(value = "/{id}")
    private UserModel getUserById(@PathVariable("id") String id) {
        try {
            UserModel user = userService.findUserById(UUID.fromString(id));
            if (user.getRole().equals(RoleEnum.Seller)) {
                return user;
            } else {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "You must be seller to get info for " + id + "!");
            }
        } catch (NoSuchElementException | IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Id Seller " + id + " not found");
        }
    }

    @PutMapping(value = "/{id}/edit-user")
    public ResponseEntity<String> editUser(@PathVariable("id") String id, @RequestParam(value = "name", required = false) String name, @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "email", required = false) String email, @RequestParam(value = "password", required = false) String password, @RequestParam(value = "address", required = false) String address) {
        try {
            var user = userService.findUserById(UUID.fromString(id));
            if (user.getRole().equals(RoleEnum.Seller)) {
                if (user.getPassword().equals(password)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password cannot be the same");
                }
                else {
                    if (name == null) {
                        name = user.getName();
                    }
                    if (username == null) {
                        username = user.getUsername();
                    }
                    if (email == null) {
                        email = user.getEmail();
                    }
                    if (password == null) {
                        password = user.getPassword();
                    }
                    if (address == null) {
                        address = user.getAddress();
                    }
                    userService.editUser(user, name, username, email, password, address);
                }
                return ResponseEntity.ok("User has been edited");
            }
            else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The ID is not a seller");
            }
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Id User " + id + " not found");
        }
    }

    @PutMapping(value = "/{id}/add-balance/{balance}")
    public ResponseEntity<String> addBalance(@PathVariable("id") String id, @PathVariable("balance") long balance) {
        try {
            var user = userService.findUserById(UUID.fromString(id));
            if (user.getRole().equals(RoleEnum.Seller)) {
                if (user.getBalance() + balance < 0 || balance < 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Balance cannot be negative");
                } else if (balance == 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Balance cannot be zero");
                } else {
                    userService.addBalanceUser(id, balance);
                    String responseMessage = String.format("Changed %d to %s --> total: %d", balance, user.getUsername(),
                            user.getBalance());
                    return ResponseEntity.ok(responseMessage);
                }
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The ID is not a seller");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id User " + id + " not found");
        }
    }

    @PutMapping(value = "/{id}/subtract-balance/{balance}")
    public ResponseEntity<String> subtractBalance(@PathVariable("id") String id, @PathVariable("balance") long balance) {
        try {
            var user = userService.findUserById(UUID.fromString(id));
            if (user.getRole().equals(RoleEnum.Seller)) {
                if (user.getBalance() + balance < 0 || balance < 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Balance cannot be negative");
                } else if (balance == 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Balance cannot be zero");
                } else {
                    userService.subtractBalanceUser(id, balance);
                    String responseMessage = String.format("Changed %d to %s --> total: %d", balance, user.getUsername(),
                            user.getBalance());
                    return ResponseEntity.ok(responseMessage);
                }
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The ID is not a seller");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id User " + id + " not found");
        }
    }
}
