package io.github.response4j.core.model;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class ApiResponseTest {

    @Test
    void canonicalConstructor_storesAllFields() {
        var timestamp = Instant.now();
        var response = new ApiResponse<>(200, "OK", timestamp, "data");
        assertThat(response.status()).isEqualTo(200);
        assertThat(response.message()).isEqualTo("OK");
        assertThat(response.timestamp()).isEqualTo(timestamp);
        assertThat(response.data()).isEqualTo("data");
    }

    @Test
    void canonicalConstructor_allowsNullData() {
        var response = new ApiResponse<>(204, "No content", Instant.now(), null);
        assertThat(response.data()).isNull();
        assertThat(response.status()).isEqualTo(204);
    }
}
