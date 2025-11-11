package com.demo.jwe.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Utilidad para cargar y generar claves RSA
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

    /**
     * Genera un par de claves RSA y las guarda en archivos PEM
     */
    public static void generateAndSaveKeyPair(String privateKeyPath, String publicKeyPath) throws Exception {
        System.out.println("🔑 Generando nuevo par de claves RSA...");
        
        // Generar par de claves
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        
        // Guardar clave privada
        savePrivateKey(keyPair.getPrivate(), privateKeyPath);
        System.out.println("✅ Clave privada guardada en: " + privateKeyPath);
        
        // Guardar clave pública
        savePublicKey(keyPair.getPublic(), publicKeyPath);
        System.out.println("✅ Clave pública guardada en: " + publicKeyPath);
        
        System.out.println("🎉 Par de claves generado exitosamente");
    }

    /**
     * Guarda una clave privada en formato PEM
     */
    private static void savePrivateKey(PrivateKey privateKey, String path) throws IOException {
        Path filePath = Paths.get(path);
        Files.createDirectories(filePath.getParent());
        
        String encoded = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        String pem = "-----BEGIN PRIVATE KEY-----\n" +
                splitIntoLines(encoded, 64) +
                "\n-----END PRIVATE KEY-----";
        
        Files.writeString(filePath, pem);
    }

    /**
     * Guarda una clave pública en formato PEM
     */
    private static void savePublicKey(PublicKey publicKey, String path) throws IOException {
        Path filePath = Paths.get(path);
        Files.createDirectories(filePath.getParent());
        
        String encoded = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String pem = "-----BEGIN PUBLIC KEY-----\n" +
                splitIntoLines(encoded, 64) +
                "\n-----END PUBLIC KEY-----";
        
        Files.writeString(filePath, pem);
    }

    /**
     * Divide un string en líneas de longitud específica
     */
    private static String splitIntoLines(String str, int lineLength) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < str.length(); i += lineLength) {
            if (i > 0) {
                result.append("\n");
            }
            result.append(str, i, Math.min(i + lineLength, str.length()));
        }
        return result.toString();
    }
}
