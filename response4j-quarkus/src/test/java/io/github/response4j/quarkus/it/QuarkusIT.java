package io.github.response4j.quarkus.it;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import static io.restassured.RestAssured.given;

@QuarkusTest
class QuarkusIT {

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
