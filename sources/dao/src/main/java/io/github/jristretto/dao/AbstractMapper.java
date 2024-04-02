/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.github.jristretto.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.RecordComponent;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import static java.util.stream.Collectors.toSet;
import java.util.stream.Stream;

/**
 *
 * @author Pieter van den Hombergh {@code <pieter.van.den.hombergh@gmail.com>}
 */
public abstract class AbstractMapper<R extends Record & Serializable, K extends Serializable>
        implements Mapper<R, K> {

    static final ConcurrentMap<Class<? extends Record>, Mapper<? extends Record, ? extends Serializable>> register = new ConcurrentHashMap<>();

    public static <X extends Record & Serializable, Y extends Serializable> Mapper<X, Y> mapperFor(
            Class<X> et) {
        if ( !register.containsKey( et ) ) {
//            loadMapperClass( et );
        }
        return (Mapper< X, Y>) register.get( et );
    }

    protected static <X extends Record & Serializable, Y extends Serializable> void register(
            Mapper<X, Y> em) {
        register.putIfAbsent( em.entityType(), em );
    }
    final Class<R> entityType;

    public AbstractMapper(Class<R> entityType) {
        this.entityType = entityType;
    }

    @Override
    public Class<R> entityType() {
        return entityType;
    }

    private Set<Field> generatedFields = null;

//    @Override
    public Set<Field> generatedFields() {
        if ( null == generatedFields ) {
            Set<Field> set = entityFields()
                    .stream()
                    .filter( f -> isGenerated( f ) )
                    .collect( toSet() );
            generatedFields = Set.copyOf( set );
        }
        return generatedFields;

    }

    private Set<RecordComponent> generatedComponents = null;

    @Override
    public Set<RecordComponent> generatedComponents() {
        if ( null == generatedComponents ) {
            var set = Stream.of( entityType.getRecordComponents() )

                    .filter( f -> isGenerated( f ) )
                    .collect( toSet() );
            generatedComponents = Set.copyOf( set );
        }
        return generatedComponents;

    }

}
