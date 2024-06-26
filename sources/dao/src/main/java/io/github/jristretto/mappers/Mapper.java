package io.github.jristretto.mappers;

import io.github.jristretto.annotations.Generated;
import io.github.jristretto.annotations.ID;
import io.github.jristretto.dao.ComponentPair;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toCollection;
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
     * Get the record components from a cache. The array is not supposed to be
     * modified by the user.
     *
     * This method exists to reduce array creation for each
     * class.getRecordComponents call.
     *
     *
     * @return the components.
     */
    RecordComponent[] recordComponents();

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
        RecordComponent[] c = recordComponents();
        Object[] values = asArray( r );
        return IntStream.range( 0, c.length )
                .mapToObj( i -> new ComponentPair( c[ i ], values[ i ] ) );
    }

    /**
     * Map the record to an array of Object.
     *
     * For performance, this method should be overridden.
     *
     * @param r to map
     * @return the record components in an array.
     */
    default Object[] asArray(R r) {

        var c = recordComponents();
        Object[] result = new Object[ c.length ];
        try {
            for ( int i = 0; i < result.length; i++ ) {
                Method accessor = c[ i ].getAccessor();
                accessor.setAccessible( true );
                result[ i ] = accessor.invoke( r );
            }
        } catch ( IllegalAccessException | InvocationTargetException neveroccurs ) {
            // this code branch can never be reached.
        }
        return result;
    }

    /**
     * Check if a field is generated.
     *
     * A field is generated when it has the annotation Generated or ID with
     * generated==true (the default).
     *
     * @param rc to test
     * @return result of test
     */
    default boolean isGenerated(RecordComponent rc) {
        Generated genannotation = rc.getAnnotation( Generated.class );
        if ( null != genannotation ) {
            return true;
        }

        ID idAnnotation = rc.getAnnotation( ID.class );
        if ( null == idAnnotation ) {
            return false;
        }
        return idAnnotation.generated();
    }

    /**
     * Stream the entity with generated components and values removed.
     *
     * @param entity to stream
     * @return a stream.
     */
    default List<ComponentPair> dropGeneratedFields(R entity) {
        Predicate<ComponentPair> fieldStays = rcp
                -> null != rcp.value() || !isGenerated( rcp.component() );
        return stream( entity )
                .filter( fieldStays )
                .toList();
    }

    /**
     * Get the positions of the components in the record as a map.
     *
     * @return the map.
     */
    Map<String, Integer> componentIndex();

    /**
     * Get the set of names.
     *
     * The returned set is ordered in the order of the record definition.
     *
     * @return a set
     */
    Set<String> componentNames();

    /**
     * Construct a new record of type R given the components.
     *
     * @param components to use
     * @return new R.
     */
    abstract R newEntity(Object[] components);
}
