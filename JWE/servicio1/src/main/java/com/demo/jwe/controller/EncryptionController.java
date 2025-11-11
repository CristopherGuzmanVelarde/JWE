package com.demo.jwe.controller;

import com.demo.jwe.model.ApiResponse;
import com.demo.jwe.model.EncryptedDataDTO;
import com.demo.jwe.model.SensitiveDataDTO;
import com.demo.jwe.service.JweEncryptionService;
import com.demo.jwe.service.Service2Client;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para operaciones de cifrado
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class EncryptionController {

    private static final Logger logger = LoggerFactory.getLogger(EncryptionController.class);
    
    private final JweEncryptionService encryptionService;
    private final Service2Client service2Client;

    public EncryptionController(JweEncryptionService encryptionService, 
                               Service2Client service2Client) {
        this.encryptionService = encryptionService;
        this.service2Client = service2Client;
    }

    /**
     * Endpoint para cifrar datos y retornar el token JWE
     * 
     * POST /api/encrypt
     */
    @PostMapping("/encrypt")
    public ResponseEntity<ApiResponse<EncryptedDataDTO>> encryptData(
            @Valid @RequestBody SensitiveDataDTO data) {
        
        logger.info("📨 Petición de cifrado recibida");
        
        try {
            // Cifrar los datos
            String encryptedData = encryptionService.encrypt(data);
            
            EncryptedDataDTO response = new EncryptedDataDTO(encryptedData);
            
            return ResponseEntity.ok(
                ApiResponse.success(
                    "Datos cifrados exitosamente", 
                    response
                )
            );
            
        } catch (Exception e) {
            logger.error("❌ Error al cifrar datos", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Error al cifrar datos: " + e.getMessage()));
        }
    }

    /**
     * Endpoint para cifrar datos y enviarlos al Servicio 2
     * 
     * POST /api/encrypt-and-send
     */
    @PostMapping("/encrypt-and-send")
    public ResponseEntity<ApiResponse<String>> encryptAndSend(
            @Valid @RequestBody SensitiveDataDTO data) {
        
        logger.info("📨 Petición de cifrado y envío recibida");
        
        try {
            // Cifrar los datos
            String encryptedData = encryptionService.encrypt(data);
            
            // Enviar al Servicio 2
            String service2Response = service2Client.sendToService2(encryptedData);
            
            return ResponseEntity.ok(
                ApiResponse.success(
                    "Datos cifrados y enviados exitosamente al Servicio 2", 
                    service2Response
                )
            );
            
        } catch (Exception e) {
            logger.error("❌ Error en el proceso de cifrado y envío", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Error: " + e.getMessage()));
        }
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(
            ApiResponse.success(
                "Servicio 1 operativo", 
                "Encryption service running"
            )
        );
    }
}
