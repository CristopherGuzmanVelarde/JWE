package com.demo.jwe.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Componente para generar claves RSA automáticamente
 * Solo se ejecuta si no existen las claves
 */
@Component
@Profile("!test")
public class KeyGenerator implements CommandLineRunner {

    private static final String PRIVATE_KEY_PATH = "keys/private-key.pem";
    private static final String PUBLIC_KEY_PATH = "keys/public-key.pem";

    @Override
    public void run(String... args) throws Exception {
        // Verificar si las claves ya existen
        if (!Files.exists(Paths.get(PUBLIC_KEY_PATH)) || 
            !Files.exists(Paths.get(PRIVATE_KEY_PATH))) {
            
            System.out.println("\n⚠️  No se encontraron las claves RSA");
            System.out.println("🔑 Generando nuevo par de claves...\n");
            
            KeyLoader.generateAndSaveKeyPair(PRIVATE_KEY_PATH, PUBLIC_KEY_PATH);
            
            System.out.println("\n⚠️  IMPORTANTE: Copia la clave privada al Servicio 2");
            System.out.println("   Comando Windows:");
            System.out.println("   copy " + PRIVATE_KEY_PATH + " ..\\servicio2\\keys\\private-key.pem\n");
            
        } else {
            System.out.println("✅ Claves RSA encontradas");
        }
    }
}
