package pe.edu.nova.java.examples.greeting;

import java.time.Instant;

/**
 * DTO de respuesta para los endpoints de saludo.
 * <p>
 * Record inmutable serializado como JSON por Jackson (SmallRye JSON-B via quarkus-jackson).
 * El timestamp se serializa como ISO-8601 gracias al ApiObjectMapperCustomizer
 * que registra JavaTimeModule y deshabilita WRITE_DATES_AS_TIMESTAMPS
 * (definido en nova-java-api-standard-quarkus-extension).
 */
public record Greeting(String message, Instant generatedAt) {

    public static Greeting of(String message) {
        return new Greeting(message, Instant.now());
    }
}