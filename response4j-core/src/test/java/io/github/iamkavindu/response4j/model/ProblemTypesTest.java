package io.github.iamkavindu.response4j.model;

import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link ProblemTypes} constants.
 */
class ProblemTypesTest {

    @Test
    void aboutBlankShouldBeCorrectUri() {
        assertEquals(URI.create("about:blank"), ProblemTypes.ABOUT_BLANK);
    }

    @Test
    void aboutBlankShouldBeAboutScheme() {
        assertEquals("about", ProblemTypes.ABOUT_BLANK.getScheme());
        assertEquals("blank", ProblemTypes.ABOUT_BLANK.getSchemeSpecificPart());
    }

    @Test
    void ianaBaseShouldBeCorrectString() {
        assertEquals("https://iana.org/assignments/http-problem-types#", ProblemTypes.IANA_BASE);
    }
}
