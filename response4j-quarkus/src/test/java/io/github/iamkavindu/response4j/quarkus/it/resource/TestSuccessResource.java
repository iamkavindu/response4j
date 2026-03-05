package io.github.iamkavindu.response4j.quarkus.it.resource;

import io.github.iamkavindu.response4j.annotation.SuccessResponse;
import io.github.iamkavindu.response4j.model.ApiResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;

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

    @GET
    @Path("/already-wrapped")
    @SuccessResponse(status = 200, message = "Ignored")
    public Response alreadyWrapped() {
        ApiResponse<String> body = ApiResponse.ok("wrapped");
        return Response.ok(body).build();
    }
}
