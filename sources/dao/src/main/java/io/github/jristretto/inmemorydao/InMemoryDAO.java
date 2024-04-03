/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
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

    private final Map<K, R> storage = new HashMap<>();
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
        K key = getMapper()
                .keyExtractor()
                .apply( e );
        storage.put( key, e );
        return Optional.of( e );
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
            out.writeObject( this.storage );
        } catch ( FileNotFoundException ex ) {
            Logger.getLogger(InMemoryDAO.class.getName() )
                    .log( Level.SEVERE, null, ex );
        } catch ( IOException ex ) {
            Logger.getLogger(InMemoryDAO.class.getName() )
                    .log( Level.SEVERE, null, ex );
        }
    }

    private void load(String aStorageName) {
        try ( ObjectInputStream in = new ObjectInputStream(
                new FileInputStream( aStorageName ) ) ) {
            this.storage.clear();
            Map<K, R> readMap = (Map<K, R>) in.readObject();

            this.storage.putAll( readMap );

        } catch ( FileNotFoundException ex ) {
            Logger.getLogger(InMemoryDAO.class.getName() )
                    .log( Level.SEVERE, null, ex );
        } catch ( IOException | ClassNotFoundException ex ) {
            Logger.getLogger(InMemoryDAO.class.getName() )
                    .log( Level.SEVERE, null, ex );
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

}
