package com.demo.jwe.service;

import com.demo.jwe.config.JweProperties;
import com.demo.jwe.model.EncryptedDataDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Servicio para comunicación con el Servicio 2
 */
@Service
public class Service2Client {

    private static final Logger logger = LoggerFactory.getLogger(Service2Client.class);
    
    private final RestTemplate restTemplate;
    private final JweProperties jweProperties;

    public Service2Client(RestTemplate restTemplate, JweProperties jweProperties) {
        this.restTemplate = restTemplate;
        this.jweProperties = jweProperties;
    }

    /**
     * Envía datos cifrados al Servicio 2 para descifrar
     * 
     * @param encryptedData Token JWE con los datos cifrados
     * @return Respuesta del Servicio 2
     */
    public String sendToService2(String encryptedData) {
        String url = jweProperties.getService2().getUrl() + 
                    jweProperties.getService2().getDecryptEndpoint();
        
        logger.info("📤 Enviando datos cifrados al Servicio 2");
        logger.debug("🌐 URL destino: {}", url);
        
        try {
            // Preparar el request
            EncryptedDataDTO requestBody = new EncryptedDataDTO(encryptedData);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<EncryptedDataDTO> request = new HttpEntity<>(requestBody, headers);
            
            // Enviar al Servicio 2
            ResponseEntity<String> response = restTemplate.postForEntity(
                    url, 
                    request, 
                    String.class
            );
            
            logger.info("✅ Respuesta recibida del Servicio 2");
            logger.debug("📥 Status: {}", response.getStatusCode());
            
            return response.getBody();
            
        } catch (Exception e) {
            logger.error("❌ Error al comunicarse con el Servicio 2: {}", e.getMessage());
            throw new RuntimeException("Error al comunicarse con el Servicio 2: " + e.getMessage(), e);
        }
    }
}
