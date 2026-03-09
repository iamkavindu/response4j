package io.github.response4j.quarkus.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
class QuarkusIT {

    @Test
    void methodLevelSuccessResponse_isWrappedWithApiResponse() {
        given().when()
                .get("/test/success/method")
                .then()
                .statusCode(200)
                .contentType(containsString("application/json"))
                .body("status", equalTo(200))
                .body("message", equalTo("Method level OK"))
                .body("timestamp", notNullValue())
                .body("data.value", equalTo("method"));
    }

    @Test
    void classLevelSuccessResponse_usesClassAnnotation() {
        given().when()
                .get("/test/success/class")
                .then()
                .statusCode(201)
                .body("status", equalTo(201))
                .body("message", equalTo("Class level created"))
                .body("data.value", equalTo("class"));
    }

    @Test
    void wrapFalse_returnsRawBodyWithoutEnvelope() {
        given().when()
                .get("/test/success/wrap-false")
                .then()
                .statusCode(200)
                .body("wrapped", equalTo(false))
                .body("status", nullValue())
                .body("message", nullValue());
    }

    @Test
    void alreadyWrappedApiResponse_isPassedThrough() {
        given().when()
                .get("/test/success/already-wrapped")
                .then()
                .statusCode(200)
                .body("status", equalTo(200))
                .body("message", equalTo("Request successful"))
                .body("data", equalTo("wrapped"));
    }

    @Test
    void annotatedException_isMappedToProblemDetail() {
        given().when()
                .get("/test/error/annotated")
                .then()
                .statusCode(400)
                .contentType(containsString("application/problem+json"))
                .body("type", equalTo("https://api.example.com/problems/bad-request"))
                .body("title", equalTo("Bad Request"))
                .body("status", equalTo(400))
                .body("detail", equalTo("Request is invalid"))
                .body("instance", containsString("test/error/annotated"));
    }

    @Test
    void aboutBlankAnnotatedException_usesReasonPhraseForTitle() {
        given().when()
                .get("/test/error/about-blank")
                .then()
                .statusCode(404)
                .body("type", equalTo("about:blank"))
                .body("status", equalTo(404))
                .body("title", equalTo("Not Found"));
    }

    @Test
    void unannotatedException_usesDefault500ProblemDetail() {
        given().when()
                .get("/test/error/unannotated")
                .then()
                .statusCode(500)
                .body("type", equalTo("about:blank"))
                .body("status", equalTo(500))
                .body("title", equalTo("Internal Server Error"))
                .body("detail", equalTo("failure"));
    }
}
