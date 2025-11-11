package com.demo.jwe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Servicio 1 - Cifrado con JWE
 * Este servicio cifra datos sensibles y los envía al Servicio 2
 */
@SpringBootApplication
public class Servicio1Application {

    public static void main(String[] args) {
        SpringApplication.run(Servicio1Application.class, args);
        System.out.println("\n" +
                "╔══════════════════════════════════════════════════════════════╗\n" +
                "║          SERVICIO 1 - JWE ENCRYPTION INICIADO               ║\n" +
                "║                                                              ║\n" +
                "║  URL: http://localhost:8081                                  ║\n" +
                "║  Interfaz Web: http://localhost:8081/                        ║\n" +
                "║  API Docs: http://localhost:8081/api/health                  ║\n" +
                "╚══════════════════════════════════════════════════════════════╝\n");
    }
}
