package com.apapedia.frontend.service;

public interface AuthService {
    public String getTokenSSO(String username, String name);
    public String getToken(String username, String password);
}
