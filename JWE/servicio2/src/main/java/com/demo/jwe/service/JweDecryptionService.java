package com.demo.jwe.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.crypto.RSADecrypter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPrivateKey;
import java.util.Map;

/**
 * Servicio de descifrado JWE
 * Utiliza la clave privada RSA para descifrar tokens JWE
 */
@Service
public class JweDecryptionService {

    private static final Logger logger = LoggerFactory.getLogger(JweDecryptionService.class);
    
    private final RSAPrivateKey privateKey;
    private final ObjectMapper objectMapper;

    public JweDecryptionService(RSAPrivateKey privateKey) {
        this.privateKey = privateKey;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Descifra un token JWE
     * 
     * @param jweToken Token JWE a descifrar
     * @return Objeto descifrado como Map
     */
    public Map<String, Object> decrypt(String jweToken) throws Exception {
        logger.info("🔓 Iniciando descifrado de datos");
        logger.debug("📦 Token JWE recibido (primeros 50 caracteres): {}", 
                jweToken.substring(0, Math.min(50, jweToken.length())) + "...");
        
        // Parse del token JWE
        JWEObject jweObject = JWEObject.parse(jweToken);
        
        logger.debug("🔧 Algoritmos JWE detectados:");
        logger.debug("   - Key Encryption: {}", jweObject.getHeader().getAlgorithm());
        logger.debug("   - Content Encryption: {}", jweObject.getHeader().getEncryptionMethod());
        
        // Descifrar usando la clave privada RSA
        RSADecrypter decrypter = new RSADecrypter(privateKey);
        jweObject.decrypt(decrypter);
        
        // Obtener el payload descifrado
        String jsonPayload = jweObject.getPayload().toString();
        
        logger.info("✅ Datos descifrados exitosamente");
        logger.debug("📝 Payload descifrado: {}", maskSensitiveData(jsonPayload));
        
        // Convertir JSON a Map
        @SuppressWarnings("unchecked")
        Map<String, Object> result = objectMapper.readValue(jsonPayload, Map.class);
        
        return result;
    }

    /**
     * Enmascara datos sensibles para los logs
     */
    private String maskSensitiveData(String data) {
        return data.replaceAll("\"cardNumber\"\\s*:\\s*\"[^\"]+\"", 
                              "\"cardNumber\":\"****-****-****-****\"");
    }
}
