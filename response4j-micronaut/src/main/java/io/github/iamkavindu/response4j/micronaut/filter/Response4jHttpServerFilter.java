package io.github.iamkavindu.response4j.micronaut.filter;

import io.github.iamkavindu.response4j.annotation.SuccessResponse;
import io.github.iamkavindu.response4j.mapper.ApiResponseMapper;
import io.github.iamkavindu.response4j.model.ApiResponse;
import io.micronaut.http.HttpAttributes;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import io.micronaut.web.router.RouteMatch;
import jakarta.inject.Inject;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

/**
 * Micronaut {@link HttpServerFilter} that wraps route responses in {@link ApiResponse} when
 * the route or controller is annotated with {@link SuccessResponse}.
 * <p>
 * Passes through existing {@code ApiResponse} bodies unchanged. When {@code SuccessResponse}
 * is absent or the route match is unknown, leaves the response unchanged.
 */
@Filter("/**")
public class Response4jHttpServerFilter implements HttpServerFilter {

    private final ApiResponseMapper apiResponseMapper;

    /**
     * Creates the filter with the given {@link ApiResponseMapper}.
     *
     * @param apiResponseMapper the mapper for converting controller return values to ApiResponse
     */
    @Inject
    public Response4jHttpServerFilter(ApiResponseMapper apiResponseMapper) {
        this.apiResponseMapper = apiResponseMapper;
    }

    /**
     * Wraps the response body in {@link ApiResponse} when the route has {@link SuccessResponse}.
     * Updates the response status from the mapped ApiResponse.
     *
     * @param request the HTTP request
     * @param chain   the filter chain
     * @return a publisher that emits the response (possibly wrapped)
     */
    @Override
    public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request, ServerFilterChain chain) {
        return Flux.from(chain.proceed(request))
                .map(response -> wrapIfRequired(request, response));
    }

    @SuppressWarnings("unchecked")
    private MutableHttpResponse<?> wrapIfRequired(HttpRequest<?> request, MutableHttpResponse<?> response) {

        if (response.getBody().orElse(null) instanceof ApiResponse<?>) {return response;}

        RouteMatch<?> routeMatch = request.getAttribute(HttpAttributes.ROUTE_MATCH, RouteMatch.class)
                .orElse(null);

        if (routeMatch == null) {return response;}

        SuccessResponse annotation = resolveAnnotation(routeMatch);
        if (annotation == null) {return response;}

        Object body = response.getBody().orElse(null);
        ApiResponse<?> apiResponse = apiResponseMapper.map(body, annotation);

        if (apiResponse != null) {
            return ((MutableHttpResponse<Object>) response)
                    .status(apiResponse.getStatus())
                    .body(apiResponse);
        }
        return response;
    }

    private SuccessResponse resolveAnnotation(RouteMatch<?> routeMatch) {
        return routeMatch.synthesize(SuccessResponse.class);
    }

    /**
     * Returns {@link #HIGHEST_PRECEDENCE} so this filter runs first in the chain.
     *
     * @return the filter order
     */
    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}
