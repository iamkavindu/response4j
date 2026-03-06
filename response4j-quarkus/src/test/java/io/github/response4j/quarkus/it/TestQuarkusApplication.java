package io.github.response4j.quarkus.it;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class TestQuarkusApplication {
    public static void main(String[] args) {
        Quarkus.run(args);
    }
}
