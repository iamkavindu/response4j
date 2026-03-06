package io.github.response4j.core.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ApiResponseTest {

    @Test
    void ok_setStatus200AndWrapsData() {
        var response = ApiResponse.ok("hello");
        assertThat(response.status()).isEqualTo(200);
        assertThat(response.data()).isEqualTo("hello");
        assertThat(response.message()).isEqualTo("Request successful");
        assertThat(response.timestamp()).isNotNull().isBeforeOrEqualTo(Instant.now());
    }

    @Test
    void created_setsStatus201() {
        var response = ApiResponse.created(Map.of("id", 1));
        assertThat(response.status()).isEqualTo(201);
    }

    @Test
    void noContent_setsStatus204AndEmptyData() {
        var response = ApiResponse.noContent();
        assertThat(response.status()).isEqualTo(204);
        assertThat(response.data()).isInstanceOf(Map.class);
        assertThat((Map<?, ?>) response.data()).isEmpty();
    }

    @Test
    void empty_setsCustomStatusAndMessage() {
        var response = ApiResponse.empty(202, "Accepted");
        assertThat(response.status()).isEqualTo(202);
        assertThat(response.message()).isEqualTo("Accepted");
    }

    @Test
    void of_customStatusMessageData() {
        var response = ApiResponse.of(206, "Partial", List.of(1, 2, 3));
        assertThat(response.status()).isEqualTo(206);
        assertThat(response.message()).isEqualTo("Partial");
        assertThat(response.data()).containsExactly(1, 2, 3);
    }

    @Test
    void timestamp_isImmutableInstant() {
        var before = Instant.now().minusMillis(100);
        var response = ApiResponse.ok("x");
        assertThat(response.timestamp()).isAfter(before);
    }
}
