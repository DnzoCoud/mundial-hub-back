package com.unbosque.mundial_hub.dto;

public class LoginResponseDTO {
    private String token;
    private boolean success;
    private String message;
    private String fullName;

    public LoginResponseDTO(String token, boolean success, String message, String fullName) {
        this.token = token;
        this.success = success;
        this.message = message;
        this.fullName = fullName;
    }

    // Getters y setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
}