package io.github.response4j.spring.it;

import static org.hamcrest.Matchers.containsString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TestSpringApplication.class)
@AutoConfigureMockMvc
class SpringIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void annotatedException_isMappedToProblemDetail() throws Exception {
        mockMvc.perform(get("/test/error/annotated").accept(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Content-Type", containsString("application/problem+json")))
                .andExpect(jsonPath("$.type").value("https://api.example.com/problems/bad-request"))
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("Request is invalid"))
                .andExpect(jsonPath("$.instance").value("/test/error/annotated"));
    }

    @Test
    void aboutBlankAnnotatedException_usesReasonPhraseForTitle() throws Exception {
        mockMvc.perform(get("/test/error/about-blank").accept(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.title").value("Not Found"));
    }

    @Test
    void unannotatedException_usesDefault500ProblemDetail() throws Exception {
        mockMvc.perform(get("/test/error/unannotated").accept(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.title").value("Internal Server Error"))
                .andExpect(jsonPath("$.detail").value("failure"));
    }
}
