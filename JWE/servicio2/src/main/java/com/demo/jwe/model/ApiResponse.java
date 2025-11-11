package com.demo.jwe.model;

/**
 * DTO para respuestas de la API
 */
public class ApiResponse<T> {
    
    private String status;
    private String message;
    private T decryptedData;
    
    public ApiResponse() {
    }
    
    public ApiResponse(String status, String message, T decryptedData) {
        this.status = status;
        this.message = message;
        this.decryptedData = decryptedData;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public T getDecryptedData() {
        return decryptedData;
    }
    
    public void setDecryptedData(T decryptedData) {
        this.decryptedData = decryptedData;
    }
    
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>("success", message, data);
    }
    
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>("error", message, null);
    }
}
