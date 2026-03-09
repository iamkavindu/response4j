/**
 * Spring Boot auto-configuration that registers response4j beans into the application context.
 *
 * <p>{@link io.github.response4j.spring.autoconfigure.Response4jAutoConfiguration} is a standard
 * Spring Boot {@code @AutoConfiguration} class activated via the {@code AutoConfiguration.imports}
 * file. It instantiates and exposes the following beans:
 *
 * <ul>
 *   <li>{@link io.github.response4j.core.mapper.ApiResponseMapper} — maps controller return values
 *       to {@link io.github.response4j.core.model.ApiResponse} envelopes.
 *   <li>{@link io.github.response4j.core.mapper.ProblemDetailMapper} — maps exceptions to
 *       {@link io.github.response4j.core.model.ProblemDetail} representations.
 *   <li>{@link io.github.response4j.spring.advice.Response4jResponseBodyAdvice} — the
 *       {@code ResponseBodyAdvice} that intercepts outbound controller responses.
 *   <li>{@link io.github.response4j.spring.handler.Response4jExceptionHandler} — the
 *       {@code @ControllerAdvice} that handles exceptions globally.
 * </ul>
 *
 * <p>No manual bean declaration is required in application code; adding the
 * {@code response4j-spring} dependency on the classpath is sufficient for Spring Boot's
 * auto-configuration mechanism to activate this class.
 *
 * @see io.github.response4j.spring.advice
 * @see io.github.response4j.spring.handler
 * @since 0.1.0
 */
package io.github.response4j.spring.autoconfigure;
