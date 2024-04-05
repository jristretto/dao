/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package io.github.jristretto.dao;

import io.github.jristretto.annotations.*;
import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;
import java.util.function.Predicate;
import org.junit.jupiter.api.*;
//import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

/**
 *
 * @author Pieter van den Hombergh {@code <pieter.van.den.hombergh@gmail.com>}
 */
public class ServiceLoaderTest {

    //@Disabled("think TDD")
    @Test
    @DisplayName( "test service loader" )
    public void testMethod() {
        ServiceLoader<DAOFactory> loader = ServiceLoader
                .load( DAOFactory.class );
        Predicate<Provider> pred;
        pred = ( var p ) -> {
            Class<?> clz = p.type();
            DAOFlavor flavor = clz.getDeclaredAnnotation( DAOFlavor.class );
            return flavor.value()
                    .equals( "inmemory" );
        };
        DAOFactory get = loader.stream()
                .filter( pred )
                .findFirst()
                .get()
                .get();
        assertThat( get.getClass()
                .getName() )
                .contains( "memory" );
//        fail( "method method reached end. You know what to do." );
    }
}
