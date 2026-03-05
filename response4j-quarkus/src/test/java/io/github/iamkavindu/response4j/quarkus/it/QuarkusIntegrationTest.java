package io.github.iamkavindu.response4j.quarkus.it;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

@QuarkusTest
class QuarkusIntegrationTest {

    @Test
    void methodLevelSuccessResponse_isWrappedWithApiResponse() {
        given()
                .when().get("/test/success/method")
                .then()
                .statusCode(200)
                .contentType(containsString("application/json"));
    }

    @Test
    void classLevelSuccessResponse_usesClassAnnotation() {
        given()
                .when().get("/test/success/class")
                .then()
                .statusCode(201);
    }

    @Test
    void wrapFalse_returnsRawBodyWithoutEnvelope() {
        given()
                .when().get("/test/success/wrap-false")
                .then()
                .statusCode(200);
    }

    @Test
    void alreadyWrappedApiResponse_isPassedThrough() {
        given()
                .when().get("/test/success/already-wrapped")
                .then()
                .statusCode(200);
    }

    @Test
    void annotatedException_isMappedToProblemDetail() {
        given()
                .when().get("/test/error/annotated")
                .then()
                .statusCode(400)
                .contentType(containsString("application/problem+json"));
    }

    @Test
    void aboutBlankAnnotatedException_usesReasonPhraseForTitle() {
        given()
                .when().get("/test/error/about-blank")
                .then()
                .statusCode(404);
    }

    @Test
    void unannotatedException_usesDefault500ProblemDetail() {
        given()
                .when().get("/test/error/unannotated")
                .then()
                .statusCode(500);
    }
}

