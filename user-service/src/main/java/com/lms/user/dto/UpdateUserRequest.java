package com.lms.user.dto;

public class UpdateUserRequest {
    private String displayName;
    private String roles;

    public UpdateUserRequest() {}

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getRoles() { return roles; }
    public void setRoles(String roles) { this.roles = roles; }
}
