/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package io.github.jristretto.dao;

import io.github.jristretto.annotations.Generated;
import io.github.jristretto.annotations.ID;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 *
 * @author Pieter van den Hombergh {@code <pieter.van.den.hombergh@gmail.com>}
 * @param <R> record
 * @param <K> key
 */
public interface Mapper<R extends Record & Serializable, K extends Serializable> {

    Function<R, K> keyExtractor();

    default List<Field> entityFields() {
        return List.of( entityType()
                .getDeclaredFields() );
    }

    Class<R> entityType();

//    Set<Field> generatedFields();

    Set<RecordComponent> generatedComponents();

    default Stream<ComponentPair> stream(R r) {
        var c = r.getClass()
                .getRecordComponents();
        Object[] values = asArray( r );
        return IntStream.range( 0, c.length )
                .mapToObj( i -> new ComponentPair( c[ i ], values[ i ] ) );
    }

    default Object[] asArray(R r) {

        var c = r.getClass()
                .getRecordComponents();
        Object[] result = new Object[ c.length ];
        try {
            for ( int i = 0; i < result.length; i++ ) {
                Method accessor = c[ i ].getAccessor();
                accessor.setAccessible( true );
                result[ i ] = accessor.invoke( r );
            }
        } catch ( IllegalAccessException | InvocationTargetException ex ) {

        }

        return result;
    }

    default boolean isGenerated(Field f) {
        ID idannotation = f.getAnnotation( ID.class );
        Generated genannotation = f.getAnnotation( Generated.class );

        return null != genannotation || ( null != idannotation && idannotation
                                         .generated() );
    }

    default boolean isGenerated(RecordComponent rc) {
        ID idannotation = rc.getAnnotation( ID.class );
        Generated genannotation = rc.getAnnotation( Generated.class );

        return null != genannotation || ( null != idannotation && idannotation
                                         .generated() );
    }
}
