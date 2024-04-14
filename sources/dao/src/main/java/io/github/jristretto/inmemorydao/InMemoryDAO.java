package io.github.jristretto.inmemorydao;

import io.github.jristretto.dao.DAO;
import io.github.jristretto.mappers.AbstractMapper;
import io.github.jristretto.mappers.Mapper;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.RecordComponent;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Memory based implementation.
 *
 * @author Pieter van den Hombergh {@code <pieter.van.den.hombergh@gmail.com>}
 * @param <R> record type
 * @param <K> key type
 */
public class InMemoryDAO<R extends Record & Serializable, K extends Serializable>
        implements DAO<R, K> {

    private final Class<R> entityType;
    private final Mapper<R, K> mapper;

    private final ConcurrentMap<K, R> storage = new ConcurrentHashMap<>();
    private final String storageFileName;

    public InMemoryDAO(Class<R> entityType) {
        this.entityType = entityType;
        mapper = AbstractMapper.mapperFor( entityType );

        this.storageFileName = entityType.getCanonicalName() + ".ser";
        if ( Boolean.getBoolean( "memoryDao.persist" ) ) {
            if ( Files.exists( Paths.get( this.storageFileName ) ) ) {
                System.out.println( "loaded " + storageFileName );
                this.load( this.storageFileName );
            }

            Thread saveThread = new Thread( () -> persistToDisk() );
            Runtime.getRuntime()
                    .addShutdownHook( saveThread );
        }
    }

    private static final AtomicInteger serialNumbers = new AtomicInteger();

    @Override
    public int lastId() {
        return serialNumbers.get();
    }

    @Override
    public int nextId() {
        return serialNumbers.incrementAndGet();
    }

    @Override
    public Optional<R> get(K id) {
        return Optional.ofNullable( storage.get( id ) );
    }

    @Override
    public List<R> getAll() {
        Collection<R> values = storage.values();
        return List.copyOf( values );
    }

    @Override
    public Optional<R> save(R e) {
        R ra = applyGenerators( e );
        K key = getMapper()
                .keyExtractor()
                .apply( ra );
        storage.put( key, ra );
        return Optional.of( ra );
    }

    @Override
    public R update(R e) {
        K key = getMapper()
                .keyExtractor()
                .apply( e );
        storage.replace( key, e );
        return e;
    }

    @Override
    public void deleteEntity(R e) {
        K key = getMapper()
                .keyExtractor()
                .apply( e );
        deleteById( key );
    }

    @Override
    public void deleteById(K k) {
        if ( null == k ) {
            return;
        }
        storage.remove( k );
    }

    @Override
    public int size() {
        return storage.size();
    }

    /**
     * Delete where keyValues match mapping.
     *
     * @param keyValues to search
     * @return the list of deleted values.
     */
    @Override
    public List<R> selectWhere(Object... keyValues) {
        EqualMask equalMask = equalMask( keyValues );
        Predicate<R> maskedEqual = r -> maskedEqual( r, equalMask );
        return this.storage.values()
                .stream()
                .filter( maskedEqual )
                .toList();
    }

    private void persistToDisk() {
        if ( storage.isEmpty() ) {
            return; // nothing to do
        }
        try (
                ObjectOutputStream out = new ObjectOutputStream(
                        new FileOutputStream( storageFileName ) ); ) {
            for ( R value : storage.values() ) {
                out.writeObject( value );
            }
        } catch ( FileNotFoundException ex ) {
            Logger.getLogger( InMemoryDAO.class.getName() )
                    .log( Level.SEVERE, null, ex );
        } catch ( IOException ex ) {
            Logger.getLogger( InMemoryDAO.class.getName() )
                    .log( Level.SEVERE, null, ex );
        }
    }

    private void load(String aStorageName) {
        dropAll();

        try ( ObjectInputStream in = new ObjectInputStream(
                new FileInputStream( aStorageName ) ) ) {
            while ( true ) {
                R r = mapper.entityType()
                        .cast( in.readObject() );
                this.storage.put( mapper.keyExtractor()
                        .apply( r ), r );
            }

        } catch ( FileNotFoundException ex ) {
            Logger.getLogger( InMemoryDAO.class.getName() )
                    .log( Level.SEVERE, null, ex );
        } catch ( IOException | ClassNotFoundException ex ) {
            Logger.getLogger( InMemoryDAO.class.getName() )
                    .log( Level.SEVERE, null, ex );
        }
        System.out.println( "read from file " + storage.size() );
    }

    /**
     * For those fields that are annotated as being generated and have a value
     * indicating that generation is wanted, apply the generator.
     *
     * @param e to be adapted.
     */
    public R applyGenerators(R e) {
        RecordComponent[] rc = entityType.getRecordComponents();
        Object[] asArray = getMapper()
                .asArray( e );
        for ( int i = 0; i < rc.length; i++ ) {
            if ( asArray[ i ] == null && mapper.isGenerated( rc[ i ] ) ) {
                var s = (Serializable) e;
                Supplier<? extends Serializable> supplier = generatorMap.get(
                        rc[ i ].getType() );
                if ( null != supplier ) {
                    var value = supplier.get();
                    asArray[ i ] = value;
                }
            }

        }
        return mapper.newEntity( asArray );
    }

    private static class SerialLongGenerator implements Supplier<  Long> {

        private AtomicLong value = new AtomicLong();

        public SerialLongGenerator() {
        }

        long nextValue() {
            return value.incrementAndGet();
        }

        void presetTo(long v) {
            value.set( v );
        }

        @Override
        public Long get() {
            return nextValue();
        }

    }

    private static class SerialIntegerGenerator implements Supplier<  Integer> {

        private AtomicInteger value = new AtomicInteger();

        public SerialIntegerGenerator() {
        }

        int nextValue() {
            return value.incrementAndGet();
        }

        void presetTo(int v) {
            value.set( v );
        }

        @Override
        public Integer get() {
            return nextValue();
        }

    }

    /**
     * Helper for masked equal test.
     */
    record EqualMask<X extends Record & Serializable>(boolean[] mask,
            Object[] values) {

        EqualMask  {
            if ( mask.length != values.length ) {
                throw new IllegalArgumentException(
                        "arguments must have same length" );
            }
        }

        @Override
        public String toString() {
            return Arrays.toString( mask ) + "\n" + Arrays.toString( values );
        }

        public int hashCode() {
            return Arrays.hashCode( mask ) << 31 + Arrays.hashCode( values );
        }

        public boolean equals(Object obj) {
            if ( !( obj instanceof EqualMask other ) ) {
                return false;
            }
            return Arrays.equals( values, other.values ) && Arrays
                    .equals( mask, other.mask );
        }
    }

    /**
     * Build an equal mask fro the given keyValues.
     *
     * @param keyValues to use
     * @return the mask.
     */
    EqualMask equalMask(Object... keyValues) {
        RecordComponent[] recordComponents = entityType.getRecordComponents();
        boolean[] b = new boolean[ recordComponents.length ];
        Object[] testedValues = new Object[ recordComponents.length ];
        Set<String> componentNames = getMapper()
                .componentNames();
        Map<String, Integer> index = getMapper()
                .componentIndex();
        for ( int i = 0; i < keyValues.length; i += 2 ) {
            String name = (String) keyValues[ i ];
            Object value = keyValues[ i + 1 ];
            if ( componentNames.contains( name ) ) {
                int j = index.get( name );
                b[ j ] = true;
                testedValues[ j ] = value;
            }
        }
        return new EqualMask( b, testedValues );
    }

    /**
     * Test for equal based on equalMask. The test short circuits on first
     * unequal component.
     *
     * @param r record to test
     * @param equalMask to efficiently test
     * @return test result
     */
    boolean maskedEqual(R r, EqualMask equalMask) {
        Object[] asArray = mapper.asArray( r );

        for ( int i = 0; i < equalMask.mask.length; i++ ) {
            if ( equalMask.mask[ i ] && !Objects.equals( asArray[ i ],
                    equalMask.values[ i ] ) ) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Mapper<R, K> getMapper() {
        return AbstractMapper.mapperFor( entityType );
    }

    @Override
    public void dropAll() {
        storage.clear();
        serialNumbers.set( 0 );
    }

    static final Map<Class<? extends Serializable>, Supplier<? extends Serializable>> generatorMap
            = new HashMap<>();

    static {
        generatorMap.put( Long.class, new SerialLongGenerator() );
        generatorMap.put( Integer.class, new SerialIntegerGenerator() );
        generatorMap.put( LocalDate.class, () -> LocalDate.now() );
    }
}
