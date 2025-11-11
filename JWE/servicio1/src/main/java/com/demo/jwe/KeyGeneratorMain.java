package com.demo.jwe;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

/**
 * Generador de claves RSA para la demo JWE
 * Ejecutar este programa primero para generar las claves
 */
public class KeyGeneratorMain {

    private static final String PRIVATE_KEY_PATH = "keys/private-key.pem";
    private static final String PUBLIC_KEY_PATH = "keys/public-key.pem";

    public static void main(String[] args) {
        try {
            System.out.println("🔑 Generando par de claves RSA...\n");
            
            // Generar par de claves
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            
            // Guardar clave privada
            savePrivateKey(keyPair.getPrivate(), PRIVATE_KEY_PATH);
            System.out.println("✅ Clave privada guardada en: " + PRIVATE_KEY_PATH);
            
            // Guardar clave pública
            savePublicKey(keyPair.getPublic(), PUBLIC_KEY_PATH);
            System.out.println("✅ Clave pública guardada en: " + PUBLIC_KEY_PATH);
            
            System.out.println("\n🎉 Par de claves generado exitosamente");
            System.out.println("\n⚠️  IMPORTANTE: Copia la clave privada al Servicio 2");
            System.out.println("   Comando Windows:");
            System.out.println("   copy " + PRIVATE_KEY_PATH + " ..\\servicio2\\keys\\private-key.pem");
            
        } catch (Exception e) {
            System.err.println("❌ Error al generar claves: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void savePrivateKey(PrivateKey privateKey, String path) throws IOException {
        Path filePath = Paths.get(path);
        Files.createDirectories(filePath.getParent());
        
        String encoded = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        String pem = "-----BEGIN PRIVATE KEY-----\n" +
                splitIntoLines(encoded, 64) +
                "\n-----END PRIVATE KEY-----";
        
        Files.writeString(filePath, pem);
    }

    private static void savePublicKey(PublicKey publicKey, String path) throws IOException {
        Path filePath = Paths.get(path);
        Files.createDirectories(filePath.getParent());
        
        String encoded = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String pem = "-----BEGIN PUBLIC KEY-----\n" +
                splitIntoLines(encoded, 64) +
                "\n-----END PUBLIC KEY-----";
        
        Files.writeString(filePath, pem);
    }

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
