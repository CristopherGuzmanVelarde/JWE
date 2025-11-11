package com.demo.jwe.model;

/**
 * DTO para enviar datos cifrados al Servicio 2
 */
public class EncryptedDataDTO {
    
    /**
     * Token JWE que contiene los datos cifrados
     */
    private String encryptedData;
    
    public EncryptedDataDTO() {
    }
    
    public EncryptedDataDTO(String encryptedData) {
        this.encryptedData = encryptedData;
    }
    
    public String getEncryptedData() {
        return encryptedData;
    }
    
    public void setEncryptedData(String encryptedData) {
        this.encryptedData = encryptedData;
    }
}
