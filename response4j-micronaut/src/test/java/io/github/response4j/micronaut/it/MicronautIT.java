package io.github.response4j.micronaut.it;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@MicronautTest
class MicronautIT {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void annotatedException_isMappedToProblemDetail() {
        var ex = assertThrows(HttpClientResponseException.class, () -> client.toBlocking()
                .exchange(
                        HttpRequest.GET("/test/error/annotated").accept(MediaType.APPLICATION_JSON_TYPE),
                        String.class));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    void aboutBlankAnnotatedException_usesReasonPhraseForTitle() {
        var ex = assertThrows(HttpClientResponseException.class, () -> client.toBlocking()
                .exchange(
                        HttpRequest.GET("/test/error/about-blank").accept(MediaType.APPLICATION_JSON_TYPE),
                        String.class));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }

    @Test
    void unannotatedException_usesDefault500ProblemDetail() {
        var ex = assertThrows(HttpClientResponseException.class, () -> client.toBlocking()
                .exchange(
                        HttpRequest.GET("/test/error/unannotated").accept(MediaType.APPLICATION_JSON_TYPE),
                        String.class));
        assertEquals(500, ex.getStatus().getCode());
    }
}
