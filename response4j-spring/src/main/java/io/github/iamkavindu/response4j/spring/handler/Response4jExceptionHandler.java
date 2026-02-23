package io.github.iamkavindu.response4j.spring.handler;

import io.github.iamkavindu.response4j.mapper.ProblemDetailMapper;
import io.github.iamkavindu.response4j.model.ProblemDetail;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class Response4jExceptionHandler {
    private final ProblemDetailMapper problemDetailMapper;

    public Response4jExceptionHandler(ProblemDetailMapper problemDetailMapper) {
        this.problemDetailMapper = problemDetailMapper;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleException(Exception exception) {
        ProblemDetail problemDetail = problemDetailMapper.map(exception);
        return ResponseEntity
                .status(problemDetail.getStatus())
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problemDetail);
    }
}
