package io.github.jristretto.mappers;

import io.github.jristretto.annotations.Generated;
import io.github.jristretto.annotations.ID;
import io.github.jristretto.dao.ComponentPair;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Mapper for DAO.
 *
 * A mapper has no other state then the final entity type.
 *
 * @author Pieter van den Hombergh {@code <pieter.van.den.hombergh@gmail.com>}
 * @param <R> record
 * @param <K> key
 */
public interface Mapper<R extends Record & Serializable, K extends Serializable> {

    /**
     * Get the function to retrieve the ID component.
     *
     * @return extractor
     */
    Function<R, K> keyExtractor();

    /**
     * Get the list of recordComponents.
     *
     * @return the RecordComponents
     */
    default List<Field> entityFields() {
        return List.of( entityType()
                .getDeclaredFields() );
    }

    /**
     * Get the mapped entity type.
     *
     * @return the type
     */
    Class<R> entityType();

    /**
     * Turn the record into a stream of ComponentPairs.
     *
     * @param r to stream
     * @return the component pairs
     */
    default Stream<ComponentPair> stream(R r) {
        var c = r.getClass()
                .getRecordComponents();
        Object[] values = asArray( r );
        return IntStream.range( 0, c.length )
                .mapToObj( i -> new ComponentPair( c[ i ], values[ i ] ) );
    }

    /**
     * Map the record to an array of Object.
     *
     * For performance, this method should be overriden.
     *
     * @param r to map
     * @return the record compoments in an array.
     */
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

    /**
     * Check if a field is generated.
     *
     * A field is generated when it has the annotation Generated or ID with
     * generated==true (the default).
     *
     * @param f to test
     * @return result of test
     */
    default boolean isGenerated(Field f) {
        ID idannotation = f.getAnnotation( ID.class );
        Generated genannotation = f.getAnnotation( Generated.class );

        return null != genannotation || ( null != idannotation && idannotation
                                         .generated() );
    }

    /**
     * Check if a field is generated.
     *
     * A field is generated when it has the annotation Generated or ID with
     * generated==true (the default).
     *
     * @param f to test
     * @return result of test
     */
    default boolean isGenerated(RecordComponent rc) {
        ID idannotation = rc.getAnnotation( ID.class );
        Generated genannotation = rc.getAnnotation( Generated.class );

        return null != genannotation || ( null != idannotation && idannotation
                                         .generated() );
    }

    /**
     * Stream the entity with generated components and values removed.
     *
     * @param entity to stream
     * @return a stream.
     */
    default List<ComponentPair> dropGeneratedFields(R entity) {
        return stream( entity )
                .filter( rcp -> !isGenerated( rcp.component() ) )
                .toList();
    }
}
