package io.github.response4j.spring.it;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = TestSpringApplication.class)
@AutoConfigureMockMvc
class SpringIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void methodLevelSuccessResponse_isWrappedWithApiResponse() {
        try {
            mockMvc.perform(get("/test/success/method").accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(header().string("Content-Type", containsString("application/json")))
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.message").value("Method level OK"))
                    .andExpect(jsonPath("$.timestamp").isNotEmpty())
                    .andExpect(jsonPath("$.data.value").value("method"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void classLevelSuccessResponse_usesClassAnnotation() {
        try {
            mockMvc.perform(get("/test/success/class").accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.status").value(201))
                    .andExpect(jsonPath("$.message").value("Class level created"))
                    .andExpect(jsonPath("$.data.value").value("class"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void wrapFalse_returnsRawBodyWithoutEnvelope() {
        try {
            mockMvc.perform(get("/test/success/wrap-false").accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.wrapped").value(false))
                    .andExpect(jsonPath("$.status").doesNotExist())
                    .andExpect(jsonPath("$.message").doesNotExist());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void noContent_sets204AndNoBody() {
        try {
            mockMvc.perform(get("/test/success/no-content").accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent())
                    .andExpect(content().string(""));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void alreadyWrappedApiResponse_isPassedThrough() {
        try {
            mockMvc.perform(get("/test/success/already-wrapped").accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.message").value("Request successful"))
                    .andExpect(jsonPath("$.data").value("wrapped"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void annotatedException_isMappedToProblemDetail() {
        try {
            mockMvc.perform(get("/test/error/annotated").accept(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(header().string("Content-Type", containsString("application/problem+json")))
                    .andExpect(jsonPath("$.type").value("https://api.example.com/problems/bad-request"))
                    .andExpect(jsonPath("$.title").value("Bad Request"))
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.detail").value("Request is invalid"))
                    .andExpect(jsonPath("$.instance").value(containsString("/test/error/annotated")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void aboutBlankAnnotatedException_usesReasonPhraseForTitle() {
        try {
            mockMvc.perform(get("/test/error/about-blank").accept(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.type").value("about:blank"))
                    .andExpect(jsonPath("$.status").value(404))
                    .andExpect(jsonPath("$.title").value("Not Found"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void unannotatedException_usesDefault500ProblemDetail() {
        try {
            mockMvc.perform(get("/test/error/unannotated").accept(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(status().is5xxServerError())
                    .andExpect(jsonPath("$.type").value("about:blank"))
                    .andExpect(jsonPath("$.status").value(500))
                    .andExpect(jsonPath("$.title").value("Internal Server Error"))
                    .andExpect(jsonPath("$.detail").value("failure"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
