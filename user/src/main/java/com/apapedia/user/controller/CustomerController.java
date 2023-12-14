package com.apapedia.user.controller;

import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.apapedia.user.model.RoleEnum;
import com.apapedia.user.model.UserModel;
import com.apapedia.user.repository.UserDb;
import com.apapedia.user.service.AuthService;
import com.apapedia.user.service.UserService;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    
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
            if (user.getRole().equals(RoleEnum.Customer)) {
                return user;
            } else {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "You must be customer to get info for " + id + "!");
            }
        } catch (NoSuchElementException | IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Id Customer " + id + " not found");
        }
    }

    @PutMapping(value = "/{id}/add-balance/{balance}")
    public ResponseEntity<String> addBalance(@PathVariable("id") String id, @PathVariable("balance") long balance) {
        try {
            var user = userService.findUserById(UUID.fromString(id));
            if (user.getRole().equals(RoleEnum.Customer)) {
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
            if (user.getRole().equals(RoleEnum.Customer)) {
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

    @PutMapping(value = "/{id}/add-cart/{cartId}")
    public ResponseEntity<String> addCart(@PathVariable("id") String id, @PathVariable("cartId") String cartId) {
        try {
            var user = userService.findCustomerById(UUID.fromString(id));
            if (user.getRole().equals(RoleEnum.Customer)) {
                user.setCartId(UUID.fromString(cartId));
                return ResponseEntity.ok("Cart has been added");
            }
            else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The ID is not a customer");
            }
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Id User " + id + " not found");
        }
    }

    @DeleteMapping(value = "/{id}/delete-cart")
    public ResponseEntity<String> deleteCart(@PathVariable("id") String id) {
        try {
            var user = userService.findCustomerById(UUID.fromString(id));
            if (user.getRole().equals(RoleEnum.Customer)) {
                user.setCartId(null);
                return ResponseEntity.ok("Cart has been deleted");
            }
            else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The ID is not a customer");
            }
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Id User " + id + " not found");
        }
    }

}
