module io.github.response4j.quarkus {

    requires io.github.response4j.core;

    // Jakarta EE spec modules (all ship as proper named JPMS modules).
    requires jakarta.ws.rs;
    requires jakarta.cdi;
    requires jakarta.inject;

    exports io.github.response4j.quarkus.filter;
    exports io.github.response4j.quarkus.mapper;
    exports io.github.response4j.quarkus.producer;
}
