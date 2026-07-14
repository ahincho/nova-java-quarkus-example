package pe.edu.nova.java.examples.greeting;

import pe.edu.nova.java.libs.api.standard.response.ApiResponse;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Recurso JAX-RS de ejemplo que demuestra la integracion con
 * {@code nova-java-api-standard-quarkus-extension}.
 * <p>
 * Los metodos retornan {@link ApiResponse}{@code <Greeting>} (contrato
 * framework-agnostic de nova-api-standard). El extension se encarga de:
 * <ul>
 *   <li>Mapear excepciones no controladas a {@code ApiResponse} JSON
 *       consistente (via {@code ApiExceptionMapper}).</li>
 *   <li>Configurar el ObjectMapper para serializar {@link java.time.Instant}
 *       como ISO-8601 (via {@code ApiObjectMapperCustomizer}).</li>
 * </ul>
 * El consumidor NO escribe codigo de manejo de errores: si {@link #hello}
 * lanza {@code IllegalArgumentException}, el cliente recibe 400 + JSON con
 * la forma {@code ApiResponse} con {@code errors[].code = "BAD_REQUEST"}.
 */
@Path("/hello")
@Produces(MediaType.APPLICATION_JSON)
public class GreetingResource {

    @GET
    public ApiResponse<Greeting> hello() {
        return ApiResponse.ok(Greeting.of("Hello from Quarkus REST"));
    }

    /**
     * Endpoint que lanza una excepcion para demostrar que el
     * {@code ApiExceptionMapper} del extension la intercepta y la
     * serializa como ApiResponse JSON (400 BAD_REQUEST).
     */
    @GET
    @Path("/error")
    public ApiResponse<Greeting> error() {
        throw new IllegalArgumentException("name must not be empty");
    }

    /**
     * Endpoint con path param que valida el input. Demuestra que cualquier
     * {@code IllegalArgumentException} (subclase incluida, e.g.
     * {@link NumberFormatException}) se mapea consistentemente a 400.
     */
    @GET
    @Path("/{name}")
    public ApiResponse<Greeting> greet(@PathParam("name") String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        return ApiResponse.ok(Greeting.of("Hello, " + name + "!"));
    }
}