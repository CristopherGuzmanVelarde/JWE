package com.demo.jwe.controller;

import com.demo.jwe.model.ApiResponse;
import com.demo.jwe.model.EncryptedDataDTO;
import com.demo.jwe.service.JweDecryptionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controlador REST para operaciones de descifrado
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class DecryptionController {

    private static final Logger logger = LoggerFactory.getLogger(DecryptionController.class);
    
    private final JweDecryptionService decryptionService;

    public DecryptionController(JweDecryptionService decryptionService) {
        this.decryptionService = decryptionService;
    }

    /**
     * Endpoint para descifrar datos cifrados con JWE
     * 
     * POST /api/decrypt
     */
    @PostMapping("/decrypt")
    public ResponseEntity<ApiResponse<Map<String, Object>>> decryptData(
            @Valid @RequestBody EncryptedDataDTO encryptedData) {
        
        logger.info("📨 Petición de descifrado recibida desde Servicio 1");
        
        try {
            // Descifrar los datos
            Map<String, Object> decryptedData = decryptionService.decrypt(
                    encryptedData.getEncryptedData()
            );
            
            return ResponseEntity.ok(
                ApiResponse.success(
                    "Datos descifrados exitosamente", 
                    decryptedData
                )
            );
            
        } catch (Exception e) {
            logger.error("❌ Error al descifrar datos", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Error al descifrar datos: " + e.getMessage()));
        }
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(
            ApiResponse.success(
                "Servicio 2 operativo", 
                "Decryption service running"
            )
        );
    }
}
