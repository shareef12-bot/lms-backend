package com.lms.auth.dto;


import com.lms.auth.model.Role;

public class GoogleLoginRequest {

    // ✅ MUST MATCH FRONTEND
    private String idToken;

    private Role role;

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
