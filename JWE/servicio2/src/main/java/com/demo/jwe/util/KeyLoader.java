package com.demo.jwe.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Utilidad para cargar claves RSA
 */
public class KeyLoader {

    /**
     * Carga una clave pública RSA desde un archivo PEM
     */
    public static RSAPublicKey loadPublicKey(String path) throws Exception {
        String keyPEM = readKeyFile(path);
        
        // Remover headers y footers del PEM
        keyPEM = keyPEM
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        
        byte[] encoded = Base64.getDecoder().decode(keyPEM);
        
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    /**
     * Carga una clave privada RSA desde un archivo PEM
     */
    public static RSAPrivateKey loadPrivateKey(String path) throws Exception {
        String keyPEM = readKeyFile(path);
        
        // Remover headers y footers del PEM
        keyPEM = keyPEM
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        
        byte[] encoded = Base64.getDecoder().decode(keyPEM);
        
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    /**
     * Lee el contenido de un archivo de clave
     */
    private static String readKeyFile(String path) throws IOException {
        Resource resource;
        
        // Intentar cargar desde el sistema de archivos primero
        Path filePath = Paths.get(path);
        if (Files.exists(filePath)) {
            resource = new FileSystemResource(filePath);
        } else {
            // Si no existe, intentar cargar desde el classpath
            resource = new ClassPathResource(path);
        }
        
        if (!resource.exists()) {
            throw new IOException("No se encontró el archivo de clave: " + path);
        }
        
        return new String(resource.getInputStream().readAllBytes());
    }
}
