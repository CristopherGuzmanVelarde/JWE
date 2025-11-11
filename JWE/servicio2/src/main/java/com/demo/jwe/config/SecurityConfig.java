package com.demo.jwe.config;

import com.demo.jwe.util.KeyLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.interfaces.RSAPrivateKey;

/**
 * Configuración de seguridad para el Servicio 2
 * Carga la clave privada RSA necesaria para descifrar
 */
@Configuration
public class SecurityConfig {

    private final JweProperties jweProperties;

    public SecurityConfig(JweProperties jweProperties) {
        this.jweProperties = jweProperties;
    }

    /**
     * Bean que carga la clave privada RSA desde el archivo
     * Esta clave se usa para descifrar los datos
     */
    @Bean
    public RSAPrivateKey rsaPrivateKey() throws Exception {
        String keyPath = jweProperties.getPrivateKeyPath();
        System.out.println("📂 Cargando clave privada desde: " + keyPath);
        
        RSAPrivateKey privateKey = KeyLoader.loadPrivateKey(keyPath);
        
        System.out.println("✅ Clave privada cargada exitosamente");
        System.out.println("   Algoritmo: " + privateKey.getAlgorithm());
        System.out.println("   Formato: " + privateKey.getFormat());
        
        return privateKey;
    }
}
