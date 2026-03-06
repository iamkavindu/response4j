package io.github.response4j.core.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.response4j.core.annotation.SuccessResponse;
import java.lang.annotation.Annotation;
import org.junit.jupiter.api.Test;

class ApiResponseMapperTest {

    private final ApiResponseMapper mapper = new ApiResponseMapper();

    @Test
    void map_withAnnotation_wrapsData() {
        var ann = successResponse(200, "OK", true);
        var result = mapper.map("payload", ann);
        assertThat(result).isNotNull();
        assertThat(result.status()).isEqualTo(200);
        assertThat(result.data()).isEqualTo("payload");
    }

    @Test
    void map_withAnnotation_nullDataAnd204_returnsEmpty() {
        var ann = successResponse(204, "No Content", true);
        var result = mapper.map(null, ann);
        assertThat(result).isNotNull();
        assertThat(result.status()).isEqualTo(204);
    }

    @Test
    void map_withAnnotation_wrapFalse_returnsNull() {
        var ann = successResponse(200, "OK", false);
        assertThat(mapper.map("payload", ann)).isNull();
    }

    @Test
    void map_noAnnotation_nonNullData_returns200Ok() {
        var result = mapper.map("data", null);
        assertThat(result.status()).isEqualTo(200);
        assertThat(result.data()).isEqualTo("data");
    }

    @Test
    void map_noAnnotation_nullData_returns204() {
        var result = mapper.map(null, null);
        assertThat(result.status()).isEqualTo(204);
    }

    private SuccessResponse successResponse(int status, String message, boolean wrap) {
        return new SuccessResponse() {
            public Class<? extends Annotation> annotationType() {
                return SuccessResponse.class;
            }

            public int status() {
                return status;
            }

            public String message() {
                return message;
            }

            public boolean wrap() {
                return wrap;
            }
        };
    }
}
