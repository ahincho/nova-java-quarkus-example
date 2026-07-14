package pe.edu.nova.java.examples.greeting;

import io.quarkus.test.junit.QuarkusIntegrationTest;

/**
 * Tests de integracion en modo packaged (corre contra el uber-jar).
 * Re-ejecuta los tests definidos en {@link GreetingResourceTest} pero
 * contra la app empaquetada (no contra {@code quarkusDev}).
 * <p>
 Util para validar que el uber-jar generado por {@code ./gradlew build}
 arranca correctamente y los endpoints responden igual que en modo dev.
 */
@QuarkusIntegrationTest
class GreetingResourceIT extends GreetingResourceTest {
    // Hereda todos los tests de GreetingResourceTest. Quarkus los corre
    // automaticamente contra la version packaged de la app.
}