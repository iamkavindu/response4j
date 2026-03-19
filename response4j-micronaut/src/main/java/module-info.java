module io.github.response4j.micronaut {

    requires io.github.response4j.core;

    // Micronaut 4.x jars carry Automatic-Module-Name manifest entries.
    // The convention is io.micronaut.micronaut_<artifact-id> with underscores.
    requires io.micronaut.micronaut_http;
    requires io.micronaut.micronaut_http_server;

    // Context annotations (@Factory, @Bean, @Requires, @Singleton) and inject
    // support live in micronaut-inject.
    requires io.micronaut.micronaut_inject;

    // RouteMatch comes from micronaut-router (transitive via micronaut-http-server).
    requires io.micronaut.micronaut_router;

    // Reactive pipeline used in the server filter.
    requires org.reactivestreams;
    requires reactor.core;

    requires jakarta.inject;

    exports io.github.response4j.micronaut.filter;
    exports io.github.response4j.micronaut.factory;
    exports io.github.response4j.micronaut.handler;
}
