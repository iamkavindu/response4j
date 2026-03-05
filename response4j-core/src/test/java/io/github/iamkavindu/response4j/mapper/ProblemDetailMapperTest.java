package io.github.iamkavindu.response4j.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.iamkavindu.response4j.annotation.ProblemExtension;
import io.github.iamkavindu.response4j.annotation.ProblemResponse;
import io.github.iamkavindu.response4j.model.ProblemDetail;
import io.github.iamkavindu.response4j.model.ProblemTypes;
import java.net.URI;
import org.junit.jupiter.api.Test;

class ProblemDetailMapperTest {

    private final ProblemDetailMapper mapper = new ProblemDetailMapper();

    @ProblemResponse(
            status = 400,
            title = "Bad Request",
            type = "https://api.example.com/problems/bad-request",
            detail = "Request is invalid",
            includeExceptionMessage = false)
    static class FullyAnnotatedException extends RuntimeException {
        FullyAnnotatedException() {
            super("ignored message");
        }
    }

    @ProblemResponse(status = 404, title = "", type = "about:blank", detail = "", includeExceptionMessage = false)
    static class AboutBlankNoTitleException extends RuntimeException {
        AboutBlankNoTitleException() {
            super("ignored");
        }
    }

    @ProblemResponse(
            status = 422,
            title = "",
            type = "https://api.example.com/problems/validation",
            detail = "",
            includeExceptionMessage = true)
    static class BlankTitleAndDetailUseDefaultsException extends RuntimeException {
        BlankTitleAndDetailUseDefaultsException(String message) {
            super(message);
        }
    }

    @ProblemResponse(status = 499, title = "", type = "about:blank", detail = "", includeExceptionMessage = true)
    static class UnknownStatusAboutBlankException extends RuntimeException {
        UnknownStatusAboutBlankException(String message) {
            super(message);
        }
    }

    @ProblemResponse(
            status = 400,
            title = "Custom Error",
            type = "https://api.example.com/problems/custom",
            detail = "custom error with extensions",
            includeExceptionMessage = false)
    @ProblemExtension(key = "docs", value = "https://api.example.com/docs/errors/custom")
    @ProblemExtension(key = "supportEmail", value = "support@example.com")
    static class WithExtensionsException extends RuntimeException {
        WithExtensionsException() {
            super("has extensions");
        }
    }

    static class UnannotatedException extends RuntimeException {
        UnannotatedException(String message) {
            super(message);
        }
    }

    static class UnannotatedNullMessageException extends RuntimeException {
        UnannotatedNullMessageException() {
            super("");
        }
    }

    @Test
    void map_withProblemResponse_usesAnnotationValues() {
        var ex = new FullyAnnotatedException();

        ProblemDetail pd = mapper.map(ex, "/test/instance");

        assertThat(pd.status()).isEqualTo(400);
        assertThat(pd.title()).isEqualTo("Bad Request");
        assertThat(pd.type()).isEqualTo(URI.create("https://api.example.com/problems/bad-request"));
        assertThat(pd.detail()).isEqualTo("Request is invalid");
        assertThat(pd.instance()).isEqualTo("/test/instance");
        assertThat(pd.extensions()).isNull();
    }

    @Test
    void map_withAboutBlankAndBlankTitle_usesHttpReasonPhrase() {
        var ex = new AboutBlankNoTitleException();

        ProblemDetail pd = mapper.map(ex, "/resource/42");

        assertThat(pd.type()).isEqualTo(ProblemTypes.ABOUT_BLANK);
        assertThat(pd.status()).isEqualTo(404);
        assertThat(pd.title()).isEqualTo("Not Found");
        assertThat(pd.detail()).isEmpty();
        assertThat(pd.instance()).isEqualTo("/resource/42");
    }

    @Test
    void map_withBlankTitleAndDetail_usesClassNameAndExceptionMessage() {
        var ex = new BlankTitleAndDetailUseDefaultsException("validation failed for field x");

        ProblemDetail pd = mapper.map(ex, "/validation");

        assertThat(pd.status()).isEqualTo(422);
        assertThat(pd.title()).isEqualTo(BlankTitleAndDetailUseDefaultsException.class.getSimpleName());
        assertThat(pd.detail()).isEqualTo("validation failed for field x");
        assertThat(pd.type()).isEqualTo(URI.create("https://api.example.com/problems/validation"));
        assertThat(pd.instance()).isEqualTo("/validation");
    }

    @Test
    void map_withUnknownStatusAndAboutBlank_usesHttpErrorTitle() {
        var ex = new UnknownStatusAboutBlankException("something odd");

        ProblemDetail pd = mapper.map(ex, "/odd");

        assertThat(pd.status()).isEqualTo(499);
        assertThat(pd.type()).isEqualTo(ProblemTypes.ABOUT_BLANK);
        assertThat(pd.title()).isEqualTo("HTTP Error 499");
        assertThat(pd.detail()).isEqualTo("something odd");
    }

    @Test
    void map_withProblemExtensions_addsAllExtensions() {
        var ex = new WithExtensionsException();

        ProblemDetail pd = mapper.map(ex, "/ext");

        assertThat(pd.extensions())
                .isNotNull()
                .containsEntry("docs", "https://api.example.com/docs/errors/custom")
                .containsEntry("supportEmail", "support@example.com");
    }

    @Test
    void map_withoutProblemResponse_usesDefault500AndAboutBlank() {
        var ex = new UnannotatedException("default failure");

        ProblemDetail pd = mapper.map(ex, "/default");

        assertThat(pd.status()).isEqualTo(500);
        assertThat(pd.type()).isEqualTo(ProblemTypes.ABOUT_BLANK);
        assertThat(pd.title()).isEqualTo("Internal Server Error");
        assertThat(pd.detail()).isEqualTo("default failure");
        assertThat(pd.instance()).isEqualTo("/default");
        assertThat(pd.extensions()).isNull();
    }

    @Test
    void map_withoutProblemResponse_andNullMessage_usesEmptyDetail() {
        var ex = new UnannotatedNullMessageException();

        ProblemDetail pd = mapper.map(ex, "/null-message");
        assertThat(pd.detail()).isEmpty();
    }

    @Test
    void map_withoutInstance_overloadSetsNullInstance() {
        var ex = new UnannotatedException("no instance");

        ProblemDetail pd = mapper.map(ex);
        assertThat(pd.instance()).isNull();
        assertThat(pd.status()).isEqualTo(500);
    }
}
