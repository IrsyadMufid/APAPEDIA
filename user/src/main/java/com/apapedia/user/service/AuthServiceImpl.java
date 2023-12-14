package com.apapedia.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.apapedia.user.dto.request.AuthRequestDTO;
import com.apapedia.user.dto.request.CreateUserRequestDTO;
import com.apapedia.user.dto.request.SSOLoginRequestDTO;
import com.apapedia.user.model.Customer;
import com.apapedia.user.model.RoleEnum;
import com.apapedia.user.model.Seller;
import com.apapedia.user.model.UserModel;
import com.apapedia.user.repository.CustomerDb;
import com.apapedia.user.repository.SellerDb;
import com.apapedia.user.repository.UserDb;
import com.apapedia.user.security.JwtGenerator;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    UserDb userDb;

    @Autowired
    SellerDb sellerDb;

    @Autowired
    CustomerDb customerDb;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtGenerator jwtGenerator;

    @Override
    public String register(CreateUserRequestDTO createUserDTO) {
        if (createUserDTO != null) {
            if (userDb.findByUsername(createUserDTO.getUsername() ) != null) {
                return "User already exists with the username.";
            }
            if (userDb.findByEmail(createUserDTO.getEmail()) != null) {
                return "User already exists with the email.";
            }
            if (userDb.findByPassword(passwordEncoder.encode(createUserDTO.getPassword())) != null) {
                return "User already exists with the password.";
            }
            if (createUserDTO.getRole().equals("Seller")) {
                var seller = new Seller();
                seller.setName(createUserDTO.getName());
                seller.setUsername(createUserDTO.getUsername());
                seller.setPassword(passwordEncoder.encode(createUserDTO.getPassword()));
                seller.setEmail(createUserDTO.getEmail());
                seller.setAddress(createUserDTO.getAddress());
                seller.setRole(RoleEnum.Seller);
                seller.setCategory(createUserDTO.getCategory());
                sellerDb.save(seller);
                return "Seller created";
            } else if (createUserDTO.getRole().equals("Customer")) {
                var customer = new Customer();
                customer.setName(createUserDTO.getName());
                customer.setUsername(createUserDTO.getUsername());
                customer.setPassword(passwordEncoder.encode(createUserDTO.getPassword()));
                customer.setEmail(createUserDTO.getEmail());
                customer.setAddress(createUserDTO.getAddress());
                customer.setRole(RoleEnum.Customer);
                customerDb.save(customer);
                return "Customer created";
            }
        }
        return null;
    }

    @Override
    public String login(AuthRequestDTO authRequestDTO) {
        String password = authRequestDTO.getPassword();
        String username;
        UserModel user;
        if (authRequestDTO.getUsername().contains("@")) {
            user = userDb.findByEmail(authRequestDTO.getUsername());
        } else {
            username = authRequestDTO.getUsername();
            user = userDb.findByUsername(username);
        }
        if (user == null) {
            return "User not found";
        }

        var encoder = new BCryptPasswordEncoder();

        if (encoder.matches(password, user.getPassword())) {
            return jwtGenerator.generateToken(user.getUsername(), user.getId(), user.getRole().toString());
        } 
        else if (password.equals(user.getPassword())) {
            return jwtGenerator.generateToken(user.getUsername(), user.getId(), user.getRole().toString());
        }
        else {
            return "Wrong password";
        }
    }

    @Override
    public String loginSSO(SSOLoginRequestDTO ssoLoginRequestDTO) {
        String username = ssoLoginRequestDTO.getUsername();
        String name = ssoLoginRequestDTO.getName();

        var seller = sellerDb.findByUsername(username);

        if (seller == null) {
            seller = new Seller();
            seller.setName(name);
            seller.setPassword("seller");
            seller.setUsername(username);
            seller.setEmail(username + "@ui.ac.id");
            seller.setAddress("Placeholder Alamat");
            seller.setRole(RoleEnum.Seller);
            seller.setCategory("Official Store");
            sellerDb.save(seller);
        }

        return jwtGenerator.generateToken(username, seller.getId(), seller.getRole().toString());
    }

    @Override
    public String loginErrorSSO(SSOLoginRequestDTO ssoLoginRequestDTO) {
        String username = ssoLoginRequestDTO.getUsername();

        var seller = sellerDb.findByUsername(username);

        if (seller == null) {
            return null;
        } else if (seller.isDeleted()) {
            return null;
        }
        else {
            return jwtGenerator.generateToken(username, seller.getId(), seller.getRole().toString());
        }
    }
}
