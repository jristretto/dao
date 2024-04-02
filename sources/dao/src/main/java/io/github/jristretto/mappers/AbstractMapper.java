package io.github.jristretto.mappers;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implements some methods based on the entity and cached the registering
 * mapper.
 *
 * @author Pieter van den Hombergh {@code <pieter.van.den.hombergh@gmail.com>}
 * @param <R> record type
 * @param <K> key type
 */
public abstract class AbstractMapper<R extends Record & Serializable, K extends Serializable>
        implements Mapper<R, K> {

    private static final Logger logger = Logger.getLogger( Mapper.class
            .getName() );

    private static final ConcurrentMap<Class<? extends Record>, Mapper<? extends Record, ? extends Serializable>> register = new ConcurrentHashMap<>();

    /**
     * Get a mapper for the given record based entity.
     *
     * The mapper will attempt to find the class or load it. Mappers are assumed
     * to live in the same package as the mapped entities.
     *
     * @param <X> record type
     * @param <Y> key for the entity
     * @param et the entity java Type (class)
     * @return a mapper
     */
    public static <X extends Record & Serializable, Y extends Serializable> Mapper<X, Y> mapperFor(
            Class<X> et) {
        if ( !register.containsKey( et ) ) {
            loadMapperClass( et );
        }
        return (Mapper< X, Y>) register.get( et );
    }

    /**
     * Try to load a mapper for an entity by name. If the type == String.class,
     * do nothing, because String is special.
     *
     * @param <E> generic type of entity
     * @param forEntity class
     *
     * @throws a RuntimeException when the requested mapper class cannot be
     * loaded
     */
    static <E extends Record & Serializable> void loadMapperClass(
            Class<E> forEntity) {
        String mapperName = forEntity.getName() + "Mapper";
        try {
            Class.forName( mapperName, true, forEntity.getClassLoader() );
            logger.log( Level.INFO,
                    "mapper {0} for class {1} successfully loaded",
                    new Object[]{ mapperName, forEntity.getSimpleName() } );

        } catch ( ClassNotFoundException ex ) {
            Logger.getLogger( Mapper.class.getName() )
                    .log( Level.SEVERE,
                            "could not find mapper {0} for class {1}",
                            new String[]{
                                mapperName,
                                forEntity
                                        .getSimpleName() } );
            throw new RuntimeException( ex );
        }
    }

    /**
     * Register a mapper for reuse.
     *
     * @param <X> record type
     * @param <Y> key type
     * @param em mapper to register
     */
    protected static <X extends Record & Serializable, Y extends Serializable> void register(
            Mapper<X, Y> em) {
        register.putIfAbsent( em.entityType(), em );
    }

    private final Class<R> entityType;

    /**
     * Create a mapper for a type.
     *
     * @param entityType cached entity type.
     */
    public AbstractMapper(Class<R> entityType) {
        this.entityType = entityType;
    }

    /**
     * Get the mapped type.
     *
     * @return the type
     */
    @Override
    public Class<R> entityType() {
        return entityType;
    }
}