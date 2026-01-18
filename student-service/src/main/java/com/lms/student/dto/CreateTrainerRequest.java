package com.lms.student.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateTrainerRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String email;

    private String expertise;

    // getters & setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getExpertise() { return expertise; }
    public void setExpertise(String expertise) { this.expertise = expertise; }
}
