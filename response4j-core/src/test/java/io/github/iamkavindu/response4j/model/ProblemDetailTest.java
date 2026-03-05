package io.github.iamkavindu.response4j.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class ProblemDetailTest {

    @Test
    void of_populatesAllFields() {
        var problem = ProblemDetail.of("Not Found", 404, "User 42 not found", "/api/users/42", null);
        assertThat(problem.title()).isEqualTo("Not Found");
        assertThat(problem.status()).isEqualTo(404);
        assertThat(problem.detail()).isEqualTo("User 42 not found");
        assertThat(problem.instance()).isEqualTo("/api/users/42");
        assertThat(problem.extensions()).isNull();
    }

    @Test
    void ofErrors_putsErrorsInExtensions() {
        var errors = List.of(
                new ProblemDetailError("/email", "must be valid"), new ProblemDetailError("/age", "must be ≥ 18"));
        var problem = ProblemDetail.ofErrors("Validation Failed", 400, "Invalid input", errors);
        assertThat(problem.extensions()).isNotNull().containsKey("errors");

        @SuppressWarnings("unchecked")
        var errList = (List<ProblemDetailError>) problem.extensions().get("errors");
        assertThat(errList).hasSize(2).extracting(ProblemDetailError::pointer).containsExactly("/email", "/age");
    }

    @Test
    void builder_withAboutBlankType_setsDefaultTitle() {
        var problem = new ProblemDetail.Builder()
                .type(ProblemTypes.ABOUT_BLANK)
                .title("Not Found")
                .status(404)
                .detail("Resource missing")
                .build();
        assertThat(problem.type()).isEqualTo(ProblemTypes.ABOUT_BLANK);
    }
}
