package com.demo.jwe.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSAEncrypter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPublicKey;

/**
 * Servicio de cifrado JWE
 * Utiliza RSA-OAEP-256 para el cifrado de la clave y A256GCM para el cifrado del contenido
 */
@Service
public class JweEncryptionService {

    private static final Logger logger = LoggerFactory.getLogger(JweEncryptionService.class);
    
    private final RSAPublicKey publicKey;
    private final ObjectMapper objectMapper;

    public JweEncryptionService(RSAPublicKey publicKey) {
        this.publicKey = publicKey;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Cifra un objeto usando JWE
     * 
     * @param data El objeto a cifrar
     * @return Token JWE con los datos cifrados
     */
    public String encrypt(Object data) throws Exception {
        logger.info("🔒 Iniciando cifrado de datos");
        
        // Convertir el objeto a JSON
        String jsonPayload = objectMapper.writeValueAsString(data);
        logger.debug("📝 Payload a cifrar: {}", maskSensitiveData(jsonPayload));
        
        // Crear el header JWE con los algoritmos especificados
        JWEHeader header = new JWEHeader.Builder(
                JWEAlgorithm.RSA_OAEP_256,  // Algoritmo para cifrar la clave de contenido
                EncryptionMethod.A256GCM     // Algoritmo para cifrar el contenido
        ).build();
        
        logger.debug("🔧 Algoritmos JWE configurados:");
        logger.debug("   - Key Encryption: RSA-OAEP-256");
        logger.debug("   - Content Encryption: A256GCM");
        
        // Crear el objeto JWE
        JWEObject jweObject = new JWEObject(header, new Payload(jsonPayload));
        
        // Cifrar usando la clave pública RSA
        RSAEncrypter encrypter = new RSAEncrypter(publicKey);
        jweObject.encrypt(encrypter);
        
        // Serializar a formato compacto
        String jweToken = jweObject.serialize();
        
        logger.info("✅ Datos cifrados exitosamente");
        logger.debug("📦 Token JWE (primeros 50 caracteres): {}", 
                jweToken.substring(0, Math.min(50, jweToken.length())) + "...");
        
        return jweToken;
    }

    /**
     * Enmascara datos sensibles para los logs
     */
    private String maskSensitiveData(String data) {
        return data.replaceAll("\"cardNumber\"\\s*:\\s*\"[^\"]+\"", 
                              "\"cardNumber\":\"****-****-****-****\"");
    }
}
