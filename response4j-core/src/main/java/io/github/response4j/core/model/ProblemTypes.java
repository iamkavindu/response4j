package io.github.response4j.core.model;

import java.net.URI;

/**
 * Constants for well-known problem type URIs registered in the
 * IANA HTTP Problem Types registry, as defined by RFC 9457 Section 4.2.
 * <p>
 * These constants provide a type-safe, discoverable way to reference standard
 * problem type URIs instead of using raw strings. As the IANA registry grows,
 * additional constants will be added here.
 *
 * @see <a href="https://www.rfc-editor.org/rfc/rfc9457#section-4.2">RFC 9457 Section 4.2</a>
 * @see <a href="https://www.iana.org/assignments/http-problem-types">IANA HTTP Problem Types Registry</a>
 */
public final class ProblemTypes {

    private ProblemTypes() {}

    /**
     * Base URI for IANA-registered problem types.
     * <p>
     * This can be used as a prefix for constructing problem type URIs registered
     * in the IANA HTTP Problem Types registry.
     */
    public static final String IANA_BASE = "https://iana.org/assignments/http-problem-types#";

    /**
     * The {@code about:blank} problem type URI.
     * <p>
     * Per RFC 9457 Section 4.2.1, this indicates that no specific problem type
     * documentation is available. When this type is used, the {@code title} field
     * MUST be the HTTP reason phrase for the status code (e.g., "Not Found" for 404,
     * "Internal Server Error" for 500).
     *
     * @see <a href="https://www.rfc-editor.org/rfc/rfc9457#section-4.2.1">RFC 9457 Section 4.2.1</a>
     */
    public static final URI ABOUT_BLANK = URI.create("about:blank");

    // Future IANA registrations can be added here as the registry grows.
    // Example: public static final URI VALIDATION_FAILED = URI.create(IANA_BASE + "validation");
}
