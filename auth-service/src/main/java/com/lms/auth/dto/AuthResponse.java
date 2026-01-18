package com.lms.auth.dto;
import com.lms.auth.model.User;

public class AuthResponse {
    private String token;
    private String email;
    private String role;
    User user;

    public AuthResponse(String token, String email, String role) {
        this.token = token;
        this.email = email;
        this.role = role;
    }
    public AuthResponse(String token, User user) {
        this.token = token;
        this.user = user;
    }


    public String getToken() {
        return token;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public User getUser() {
        return user;
    }

}
