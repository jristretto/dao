package io.github.jristretto.inmemorydao;

import io.github.jristretto.annotations.ID;
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
import java.util.function.Consumer;
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

    private final SerialGenerator<K> keyGenerator;
    private int keyIndex;

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
        this.keyGenerator = computeGenerator();
        this.keyIndex = keyIndex();
    }

    private static final AtomicInteger serialNumbers = new AtomicInteger();

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
        K key = extractKey( ra );
        storage.put( key, ra );
        return Optional.of( ra );
    }

    K extractKey(R ra) {
        K key = getMapper()
                .keyExtractor()
                .apply( ra );
        return key;
    }

    public R update(R e) {
        K key = extractKey( e );
        storage.replace( key, e );
        return e;
    }

    @Override
    public void deleteEntity(R e) {
        K key = extractKey( e );
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
        Predicate<R> maskedEqual = r -> equalMask.maskedEqual( mapper
                .asArray( r ) );
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
     * @return a new entity with generated fields filled in.
     */
    public R applyGenerators(R e) {
        Object[] componentArray = getMapper().asArray( e );
        if ( null == keyGenerator ) {
            return mapper.newEntity( componentArray );
        }
        if ( null == componentArray[ keyIndex ] ) {
            componentArray[ keyIndex ] = keyGenerator.get();
        } else {
            keyGenerator.accept( (K) componentArray[ keyIndex ] );
        }
        return mapper.newEntity( componentArray );
    }

    @SuppressWarnings( " unchecked" )
    private SerialGenerator<K> computeGenerator() {
        RecordComponent keyComponent = mapper.recordComponents()[ keyIndex ];
        ID annotation = keyComponent.getAnnotation( ID.class );
        if ( null == annotation ) {
            return null;
        }
        Class<?> compType = keyComponent.getType();

        if ( compType == Integer.class ) {
            return (SerialGenerator<K>) new SerialIntegerGenerator();
        } else if ( compType == Long.class ) {
            return (SerialGenerator<K>) new SerialLongGenerator();
        }
        return null;
    }

    private int keyIndex() {
        RecordComponent[] recordComponents = mapper.recordComponents();
        for ( int i = 0; i < recordComponents.length; i++ ) {
            if ( null != recordComponents[ i ].getAnnotation( ID.class ) ) {
                return i;
            }
        }
        return 0;
    }

    interface SerialGenerator<S> extends Supplier<S>, Consumer<S> {
    }

    private static final class SerialLongGenerator implements
            SerialGenerator<Long> {

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

        @Override
        public void accept(Long t) {
            presetTo( Math.max( value.get(), t ) );
        }
    }

    private static final class SerialIntegerGenerator implements
            SerialGenerator<Integer> {

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

        @Override
        public void accept(Integer t) {
            presetTo( Math.max( value.get(), t.intValue() ) );
        }

    }

    /**
     * Helper for masked equal test.
     */
    public record EqualMask<X extends Record & Serializable>(boolean[] mask,
            Object[] values) {

        public EqualMask  {
            if ( mask.length != values.length ) {
                throw new IllegalArgumentException(
                        "arguments must have same length" );
            }
        }

        /**
         * Test for equal based on equalMask. The test short circuits on first
         * unequal component.
         *
         * @param components of record to test
         * @return test result
         */
        public boolean maskedEqual(Object[] components) {
            for ( int i = 0; i < this.mask.length; i++ ) {
                if ( mask[ i ] && !Objects.equals( components[ i ],
                        values[ i ] ) ) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Build an equal mask from the given keyValues. The key values are used to
     * test the records for equality of the named record component to the values
     * given.
     *
     *
     * @param keyValues (String, Object)+ to use
     * @return the mask.
     */
    EqualMask equalMask(Object... keyValues) {
        RecordComponent[] recordComponents = mapper.recordComponents();
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
        return equalMask.maskedEqual( asArray );
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
