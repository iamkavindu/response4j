module io.github.response4j.spring {

    requires io.github.response4j.core;

    // spring.webmvc (ResponseBodyAdvice), spring.web (MediaType, HttpStatusCode, @RestControllerAdvice),
    // and spring.core (MethodParameter, @NonNull) must all be declared explicitly so that Maven
    // places each JAR on --module-path rather than leaving it on the unnamed classpath.
    requires spring.webmvc;
    requires spring.web;
    requires spring.core;
    requires spring.context;
    requires spring.boot.autoconfigure;
    requires jakarta.servlet;

    exports io.github.response4j.spring.advice;
    exports io.github.response4j.spring.autoconfigure;
    exports io.github.response4j.spring.handler;
}
