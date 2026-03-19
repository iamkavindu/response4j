package io.github.response4j.quarkus.it.resource;

import java.util.Map;

import io.github.response4j.core.annotation.SuccessResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/test/success")
@SuccessResponse(status = 201, message = "Class level created")
@Produces(MediaType.APPLICATION_JSON)
public class TestSuccessResource {

    @GET
    @Path("/method")
    @SuccessResponse(status = 200, message = "Method level OK")
    public Map<String, Object> methodLevel() {
        return Map.of("value", "method");
    }

    @GET
    @Path("/class")
    public Map<String, Object> classLevel() {
        return Map.of("value", "class");
    }

    @GET
    @Path("/wrap-false")
    @SuccessResponse(status = 200, message = "Ignored", wrap = false)
    public Map<String, Object> wrapFalse() {
        return Map.of("wrapped", false);
    }
}
