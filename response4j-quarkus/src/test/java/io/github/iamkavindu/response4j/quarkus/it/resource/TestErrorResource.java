package io.github.iamkavindu.response4j.quarkus.it.resource;

import io.github.iamkavindu.response4j.annotation.ProblemResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/test/error")
public class TestErrorResource {

    @ProblemResponse(
            status = 400,
            title = "Bad Request",
            type = "https://api.example.com/problems/bad-request",
            detail = "Request is invalid",
            includeExceptionMessage = false
    )
    public static class AnnotatedBadRequestException extends RuntimeException {
    }

    @ProblemResponse(
            status = 404,
            title = "",
            type = "about:blank",
            detail = "",
            includeExceptionMessage = false
    )
    public static class AboutBlankNotFoundException extends RuntimeException {
    }

    public static class UnannotatedException extends RuntimeException {
        public UnannotatedException(String message) {
            super(message);
        }
    }

    @GET
    @Path("/annotated")
    public String annotated() {
        throw new AnnotatedBadRequestException();
    }

    @GET
    @Path("/about-blank")
    public String aboutBlank() {
        throw new AboutBlankNotFoundException();
    }

    @GET
    @Path("/unannotated")
    public String unannotated() {
        throw new UnannotatedException("failure");
    }
}

