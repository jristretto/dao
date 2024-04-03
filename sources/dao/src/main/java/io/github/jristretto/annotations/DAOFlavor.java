package io.github.jristretto.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows the selection of a preferred DAO Factory implementation, for instance
 * postgresql for a database backed version or "inmemory" for an in memory
 * version.
 *
 * @author Pieter van den Hombergh {@code <pieter.van.den.hombergh@gmail.com>}
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.TYPE )
public @interface DAOFlavor {

    String value() default "";
}
