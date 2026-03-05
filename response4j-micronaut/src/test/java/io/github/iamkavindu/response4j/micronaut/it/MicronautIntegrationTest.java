package io.github.iamkavindu.response4j.micronaut.it;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@MicronautTest
class MicronautIntegrationTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void methodLevelSuccessResponse_isWrappedWithApiResponse() {
        var response = client.toBlocking().exchange(
                HttpRequest.GET("/test/success/method").accept(MediaType.APPLICATION_JSON_TYPE),
                String.class
        );
        assert response.getStatus() == HttpStatus.OK;
        assert response.getContentType().orElse(null) != null;
    }

    @Test
    void classLevelSuccessResponse_usesClassAnnotation() {
        var response = client.toBlocking().exchange(
                HttpRequest.GET("/test/success/class").accept(MediaType.APPLICATION_JSON_TYPE),
                String.class
        );
        assert response.getStatus() == HttpStatus.CREATED;
    }

    @Test
    void wrapFalse_returnsRawBodyWithoutEnvelope() {
        var response = client.toBlocking().exchange(
                HttpRequest.GET("/test/success/wrap-false").accept(MediaType.APPLICATION_JSON_TYPE),
                String.class
        );
        assert response.getStatus() == HttpStatus.OK;
    }

    @Test
    void alreadyWrappedApiResponse_isPassedThrough() {
        var response = client.toBlocking().exchange(
                HttpRequest.GET("/test/success/already-wrapped").accept(MediaType.APPLICATION_JSON_TYPE),
                String.class
        );
        assert response.getStatus() == HttpStatus.OK;
    }

    @Test
    void annotatedException_isMappedToProblemDetail() {
        var response = client.toBlocking().exchange(
                HttpRequest.GET("/test/error/annotated").accept(MediaType.APPLICATION_JSON_TYPE),
                String.class
        );
        assert response.getStatus() == HttpStatus.BAD_REQUEST;
    }

    @Test
    void aboutBlankAnnotatedException_usesReasonPhraseForTitle() {
        var response = client.toBlocking().exchange(
                HttpRequest.GET("/test/error/about-blank").accept(MediaType.APPLICATION_JSON_TYPE),
                String.class
        );
        assert response.getStatus() == HttpStatus.NOT_FOUND;
    }

    @Test
    void unannotatedException_usesDefault500ProblemDetail() {
        var response = client.toBlocking().exchange(
                HttpRequest.GET("/test/error/unannotated").accept(MediaType.APPLICATION_JSON_TYPE),
                String.class
        );
        assert response.getStatus().getCode() == 500;
    }
}

