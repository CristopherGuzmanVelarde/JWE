package com.demo.jwe.config;

import com.demo.jwe.util.KeyLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.security.interfaces.RSAPublicKey;

/**
 * Configuración de seguridad para el Servicio 1
 * Carga la clave pública RSA necesaria para cifrar
 */
@Configuration
public class SecurityConfig {

    private final JweProperties jweProperties;

    public SecurityConfig(JweProperties jweProperties) {
        this.jweProperties = jweProperties;
    }

    /**
     * Bean que carga la clave pública RSA desde el archivo
     * Esta clave se usa para cifrar los datos
     */
    @Bean
    public RSAPublicKey rsaPublicKey() throws Exception {
        String keyPath = jweProperties.getPublicKeyPath();
        System.out.println("📂 Cargando clave pública desde: " + keyPath);
        
        RSAPublicKey publicKey = KeyLoader.loadPublicKey(keyPath);
        
        System.out.println("✅ Clave pública cargada exitosamente");
        System.out.println("   Algoritmo: " + publicKey.getAlgorithm());
        System.out.println("   Formato: " + publicKey.getFormat());
        
        return publicKey;
    }

    /**
     * RestTemplate para comunicación HTTP con otros servicios
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
