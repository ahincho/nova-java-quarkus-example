package pe.edu.nova.java.examples.greeting;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

/**
 * Tests de integracion del {@link GreetingResource} consumiendo
 * {@code nova-java-api-standard-quarkus-extension}.
 * <p>
 Estos tests validan que el extension se descubre correctamente via
 * CDI/Jandex en una app Quarkus y aplica la serializacion esperada.
 * Son la unica fuente de verdad para "el extension funciona end-to-end"
 * (los tests unitarios en el repo del extension son solo logica pura,
 * sin contexto Quarkus, ver README del extension).
 */
@QuarkusTest
class GreetingResourceTest {

    @Test
    void helloReturnsSuccessApiResponseWithGreetingPayload() {
        given()
            .when().get("/hello")
            .then()
                .statusCode(200)
                .contentType("application/json")
                .body("success", is(true))
                .body("status", is(200))
                .body("data.message", equalTo("Hello from Quarkus REST"))
                .body("data.generatedAt", notNullValue())
                .body("errors.size()", is(0));
    }

    @Test
    void errorEndpointMapsIllegalArgumentExceptionToBadRequestApiResponse() {
        given()
            .when().get("/hello/error")
            .then()
                .statusCode(400)
                .contentType("application/json")
                .body("success", is(false))
                .body("status", is(400))
                .body("data", nullValue())
                .body("errors[0].code", equalTo("BAD_REQUEST"))
                .body("errors[0].message", equalTo("name must not be empty"));
    }

    @Test
    void pathParamEndpointReturnsPersonalizedGreeting() {
        given()
            .when().get("/hello/World")
            .then()
                .statusCode(200)
                .body("success", is(true))
                .body("status", is(200))
                .body("data.message", equalTo("Hello, World!"));
    }

    @Test
    void blankPathParamMapsToBadRequest() {
        // PathParam blank (e.g. "   ") se valida en el resource y lanza
        // IllegalArgumentException, que el mapper mapea a 400 BAD_REQUEST.
        // NOTA: JAX-RS no URL-decodea path params automaticamente en
        // Quarkus REST (resteasy-reactive), asi que pasamos el valor
        // como path param tipado (no como string en la URL) para evitar
        // el re-encoding de RestAssured.
        given()
            .pathParam("name", "   ")
            .when().get("/hello/{name}")
            .then()
                .statusCode(400)
                .body("success", is(false))
                .body("status", is(400))
                .body("errors[0].code", equalTo("BAD_REQUEST"));
    }
}