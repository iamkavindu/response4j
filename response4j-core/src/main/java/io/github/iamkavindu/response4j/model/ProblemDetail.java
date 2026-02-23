package io.github.iamkavindu.response4j.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.net.URI;
import java.util.Map;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProblemDetail {

    private URI type;

    private String title;

    private int status;

    private String detail;

    private String instance;

    @JsonAnyGetter
    private Map<String, Object> extensions;

    public static ProblemDetail of(int status, String title, String detail) {
        return ProblemDetail.builder()
                .type(URI.create("about:blank"))
                .status(status)
                .title(title)
                .detail(detail)
                .build();
    }
}
