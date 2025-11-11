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
     * Ruta al archivo de clave pública RSA
     * Puede ser sobrescrita con la variable de entorno: JWE_PUBLIC_KEY_PATH
     */
    private String publicKeyPath;
    
    /**
     * Configuración del Servicio 2
     */
    private Service2 service2 = new Service2();
    
    public String getPublicKeyPath() {
        return publicKeyPath;
    }
    
    public void setPublicKeyPath(String publicKeyPath) {
        this.publicKeyPath = publicKeyPath;
    }
    
    public Service2 getService2() {
        return service2;
    }
    
    public void setService2(Service2 service2) {
        this.service2 = service2;
    }
    
    public static class Service2 {
        private String url;
        private String decryptEndpoint;
        
        public String getUrl() {
            return url;
        }
        
        public void setUrl(String url) {
            this.url = url;
        }
        
        public String getDecryptEndpoint() {
            return decryptEndpoint;
        }
        
        public void setDecryptEndpoint(String decryptEndpoint) {
            this.decryptEndpoint = decryptEndpoint;
        }
    }
}
