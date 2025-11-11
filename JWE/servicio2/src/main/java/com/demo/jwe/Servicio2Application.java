package com.demo.jwe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Servicio 2 - Descifrado con JWE
 * Este servicio recibe datos cifrados y los descifra
 */
@SpringBootApplication
public class Servicio2Application {

    public static void main(String[] args) {
        SpringApplication.run(Servicio2Application.class, args);
        System.out.println("\n" +
                "╔══════════════════════════════════════════════════════════════╗\n" +
                "║          SERVICIO 2 - JWE DECRYPTION INICIADO               ║\n" +
                "║                                                              ║\n" +
                "║  URL: http://localhost:8082                                  ║\n" +
                "║  API: http://localhost:8082/api/decrypt                      ║\n" +
                "║  Health: http://localhost:8082/api/health                    ║\n" +
                "╚══════════════════════════════════════════════════════════════╝\n");
    }
}
