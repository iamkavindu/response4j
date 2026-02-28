package io.github.iamkavindu.response4j.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link ProblemDetailError} record.
 */
class ProblemDetailErrorTest {

    @Test
    void shouldCreateErrorWithPointerAndDetail() {
        ProblemDetailError error = new ProblemDetailError("/email", "must be a valid email address");
        
        assertEquals("/email", error.pointer());
        assertEquals("must be a valid email address", error.detail());
    }

    @Test
    void shouldSupportJsonPointerFormat() {
        ProblemDetailError error = new ProblemDetailError("/user/profile/age", "must be at least 18");
        
        assertTrue(error.pointer().startsWith("/"));
        assertEquals("must be at least 18", error.detail());
    }

    @Test
    void shouldSupportFieldNameFormat() {
        ProblemDetailError error = new ProblemDetailError("username", "is already taken");
        
        assertEquals("username", error.pointer());
        assertEquals("is already taken", error.detail());
    }

    @Test
    void shouldBeImmutable() {
        ProblemDetailError error1 = new ProblemDetailError("/field", "error message");
        ProblemDetailError error2 = new ProblemDetailError("/field", "error message");
        
        assertEquals(error1, error2);
        assertEquals(error1.hashCode(), error2.hashCode());
    }
}
