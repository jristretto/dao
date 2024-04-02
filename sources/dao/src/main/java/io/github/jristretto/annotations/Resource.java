package io.github.jristretto.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A resource for injection.
 *
 * @author Pieter van den Hombergh  {@code pieter.van.den.hombergh@gmail.com}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.FIELD, ElementType.RECORD_COMPONENT, ElementType.METHOD } )
public @interface Resource {
    /**
     * The resource may have a well known name.
     */
    String value() default "";
}
