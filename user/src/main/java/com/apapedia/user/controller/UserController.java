package com.apapedia.user.controller;

import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.apapedia.user.dto.UserMapper;
import com.apapedia.user.dto.request.UpdateUserRequestDTO;
import com.apapedia.user.model.RoleEnum;
import com.apapedia.user.model.UserModel;
import com.apapedia.user.repository.UserDb;
import com.apapedia.user.security.JwtGenerator;
import com.apapedia.user.service.AuthService;
import com.apapedia.user.service.UserService;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    AuthService authService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserDb userDb;

    @Autowired
    JwtGenerator jwtGenerator;

    @GetMapping(value = "/{id}")
    private UserModel getUserById(@PathVariable("id") String id) {
        try {
            return userService.findUserById(UUID.fromString(id));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Id User " + id + " not found");
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") String id) {
        try {
            var user = userService.findUserById(UUID.fromString(id));
            userService.deleteUser(user);
            return ResponseEntity.ok("User with id " + id + " has been deleted");
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Id User " + id + " not found");
        }
    }

    @PutMapping(value = "/edit-user/{id}")
    public ResponseEntity<UpdateUserRequestDTO> editUser(@PathVariable("id") String id, @RequestBody UpdateUserRequestDTO updateUserRequestDTO) {
        try {
            var user = userService.findUserById(UUID.fromString(id));
            userService.editUser(user, updateUserRequestDTO);
            return ResponseEntity.ok(updateUserRequestDTO);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Id User " + id + " not found");
        }
    }

    @PutMapping(value = "/edit-user-seller/{id}")
    public ResponseEntity<UpdateUserRequestDTO> editUserSeller(@PathVariable("id") String id, @RequestBody UpdateUserRequestDTO updateUserRequestDTO) {
        try {
            var user = userService.findSellerById(UUID.fromString(id));
            userService.editUserSeller(user, updateUserRequestDTO);
            return ResponseEntity.ok(updateUserRequestDTO);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Id User " + id + " not found");
        }
    }

    @PutMapping(value = "/add-balance/{id}/{balance}")
    public ResponseEntity<String> addBalance(@PathVariable("id") String id, @PathVariable("balance") long balance) {
        try {
            var user = userService.findUserById(UUID.fromString(id));
            if (user.getBalance() + balance < 0 || balance < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Balance cannot be negative");
            } else if (balance == 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Balance cannot be zero");
            } else {
                userService.addBalanceUser(id, balance);
                String responseMessage = String.format("Added %d to %s --> current balance: %d", balance, user.getUsername(),
                        user.getBalance());
                return ResponseEntity.ok(responseMessage);
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id User " + id + " not found");
        }
    }

    @PutMapping(value = "/subtract-balance/{id}/{balance}")
    public ResponseEntity<String> subtractBalance(@PathVariable("id") String id, @PathVariable("balance") long balance) {
        try {
            var user = userService.findUserById(UUID.fromString(id));
            if (user.getBalance() == 0 || user.getBalance() - balance < 0 || balance < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Balance is zero or negative");
            } else {
                userService.subtractBalanceUser(id, balance);
                String responseMessage = String.format("Subtracted %d to %s --> current balance: %d", balance, user.getUsername(),
                        user.getBalance());
                return ResponseEntity.ok(responseMessage);
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id User " + id + " not found");
        }
    }

    @PutMapping(value = "/add-cart/{id}/{cartId}")
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

    @DeleteMapping(value = "/delete-cart/{id}")
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
