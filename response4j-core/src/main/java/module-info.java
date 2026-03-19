module io.github.response4j.core {

    // Jackson annotations are an optional dependency (used only for JSON serialisation hints).
    requires static com.fasterxml.jackson.annotation;

    // Public API — users interact with the library exclusively through annotations.
    exports io.github.response4j.core.annotation;

    // Internal implementation — restricted to the framework integration modules.
    // User code should not reference ApiResponse or the mapper classes directly;
    // annotate controller methods with @SuccessResponse instead.
    exports io.github.response4j.core.model to
            io.github.response4j.spring,
            io.github.response4j.quarkus,
            io.github.response4j.micronaut;

    exports io.github.response4j.core.mapper to
            io.github.response4j.spring,
            io.github.response4j.quarkus,
            io.github.response4j.micronaut;
}
