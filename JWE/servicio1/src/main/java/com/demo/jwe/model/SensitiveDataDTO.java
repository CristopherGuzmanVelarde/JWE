package com.demo.jwe.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO para datos sensibles del usuario
 * Estos datos serán cifrados con JWE antes de ser enviados
 */
public class SensitiveDataDTO {
    
    @NotBlank(message = "El nombre es requerido")
    private String name;
    
    @NotBlank(message = "El email es requerido")
    @Email(message = "Email inválido")
    private String email;
    
    @NotBlank(message = "El número de tarjeta es requerido")
    private String cardNumber;
    
    public SensitiveDataDTO() {
    }
    
    public SensitiveDataDTO(String name, String email, String cardNumber) {
        this.name = name;
        this.email = email;
        this.cardNumber = cardNumber;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getCardNumber() {
        return cardNumber;
    }
    
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
}
