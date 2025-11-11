package com.demo.jwe.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Propiedades de configuración para JWE
 * Estas propiedades se cargan desde application.yml y pueden ser
 * sobrescritas con variables de entorno
 */
@Configuration
@ConfigurationProperties(prefix = "jwe")
public class JweProperties {
    
    /**
     * Ruta al archivo de clave privada RSA
     * Puede ser sobrescrita con la variable de entorno: JWE_PRIVATE_KEY_PATH
     */
    private String privateKeyPath;
    
    public String getPrivateKeyPath() {
        return privateKeyPath;
    }
    
    public void setPrivateKeyPath(String privateKeyPath) {
        this.privateKeyPath = privateKeyPath;
    }
}
