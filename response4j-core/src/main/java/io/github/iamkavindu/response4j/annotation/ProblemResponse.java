package io.github.iamkavindu.response4j.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ProblemResponse {
    int status() default 500;
    String title() default "";
    String type() default "about:blank";
    String detail() default "";
    boolean includeExceptionMessage() default true;
}
