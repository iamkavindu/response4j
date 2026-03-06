/**
 * Spring {@code ResponseBodyAdvice} integration for automatic success-response wrapping.
 *
 * <p>{@link io.github.response4j.spring.advice.Response4jResponseBodyAdvice} implements Spring's
 * {@code ResponseBodyAdvice} interface to intercept the return value of every controller method
 * before serialization. When the method or its declaring class is annotated with
 * {@link io.github.response4j.core.annotation.SuccessResponse}, the advice delegates to
 * {@link io.github.response4j.core.mapper.ApiResponseMapper} to wrap the value in an
 * {@link io.github.response4j.core.model.ApiResponse} envelope. When {@code wrap = false} is set
 * on the annotation, or when the return value is already an {@code ApiResponse}, the body is
 * passed through unchanged.
 *
 * <p>The advice is registered automatically by
 * {@link io.github.response4j.spring.autoconfigure.Response4jAutoConfiguration} and requires no
 * additional configuration.
 *
 * @see io.github.response4j.core.annotation.SuccessResponse
 * @see io.github.response4j.core.model.ApiResponse
 * @see io.github.response4j.spring.autoconfigure
 * @since 0.1.0
 */
package io.github.response4j.spring.advice;
